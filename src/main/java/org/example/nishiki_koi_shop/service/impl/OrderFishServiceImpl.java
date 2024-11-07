package org.example.nishiki_koi_shop.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.nishiki_koi_shop.model.dto.OrderFishDto;
import org.example.nishiki_koi_shop.model.dto.OrderTourDto;
import org.example.nishiki_koi_shop.model.entity.Cart;
import org.example.nishiki_koi_shop.model.entity.OrderFish;
import org.example.nishiki_koi_shop.model.entity.OrderTour;
import org.example.nishiki_koi_shop.model.entity.User;
import org.example.nishiki_koi_shop.model.payload.OrderFishForm;
import org.example.nishiki_koi_shop.repository.CartRepository;
import org.example.nishiki_koi_shop.repository.OrderFishRepository;
import org.example.nishiki_koi_shop.repository.UserRepository;
import org.example.nishiki_koi_shop.service.OrderFishService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class OrderFishServiceImpl implements OrderFishService {

    private final OrderFishRepository orderFishRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    @Override
    @Transactional
    public OrderFishDto createOrderFish(OrderFishForm orderFishForm, Principal principal) {
        // Lấy thông tin người dùng từ email
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        log.info("Creating order for user: {}", user.getUsername());

        // Kiểm tra xem người dùng có giỏ hàng và có sản phẩm trong giỏ hàng hay không
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("No cart found for user"));

        // Kiểm tra nếu giỏ hàng không có sản phẩm
        if (cart.getItems().isEmpty()) {
            log.warn("User {} has no items in the cart", user.getUsername());
            throw new RuntimeException("Your cart is empty. Please add items to your cart before placing an order.");
        }

        // Tạo đơn hàng (OrderFish)
        OrderFish orderFish = OrderFish.builder()
                .user(user)
                .totalAmount(orderFishForm.getTotalAmount())
                .deliveryDate(orderFishForm.getDeliveryDate())
                .paymentMethod(orderFishForm.getPaymentMethod())
                .createdDate(LocalDate.now())
                .status(OrderFish.Status.PENDING) // Trạng thái mặc định
                .build();

        // Lưu đơn hàng vào cơ sở dữ liệu
        OrderFish savedOrderFish = orderFishRepository.save(orderFish);
        log.info("Order created with ID: {}", savedOrderFish.getOrderFishId());

        // Sau khi tạo đơn hàng thành công, xóa các mục trong giỏ hàng của người dùng
        cartRepository.deleteByUserId(user.getId());
        log.info("Cart cleared for user: {}", user.getUsername());

        return OrderFishDto.from(savedOrderFish);
    }

    @Override
    public OrderFishDto getOrderFishById(long id, Principal principal) {
        OrderFish orderFish = orderFishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderFish not found"));

        if (!orderFish.getUser().getEmail().equals(principal.getName())) {
            log.warn("Unauthorized access attempt for order ID: {}", id);
            throw new SecurityException("Unauthorized access");
        }

        log.info("Order retrieved with ID: {}", id);
        return OrderFishDto.from(orderFish);
    }

    @Override
    public List<OrderFishDto> getOrderFishByUserId(long id, Principal principal) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!user.getEmail().equals(principal.getName())) {
            log.warn("Unauthorized access attempt for user ID: {}", id);
            throw new SecurityException("Unauthorized access");
        }

        List<OrderFish> orders = orderFishRepository.findByUserId(id);
        return orders.stream().map(OrderFishDto::from).collect(Collectors.toList());
    }

    @Override
    public List<OrderFishDto> getAllOrderFishes() {
        log.info("Retrieving all order fishes");
        return orderFishRepository.findAll()
                .stream()
                .map(OrderFishDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public OrderFishDto getOrderFishById(long id) {
        return orderFishRepository.findById(id)
                .map(OrderFishDto::from)
                .orElseThrow(() -> new RuntimeException("OrderFish not found"));
    }

    @Override
    public OrderFishDto updateOrderFish(long id, OrderFishForm orderFishForm) {
        OrderFish orderFish = orderFishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderFish not found"));

        orderFish.setTotalAmount(orderFishForm.getTotalAmount());
        orderFish.setDeliveryDate(orderFishForm.getDeliveryDate());
        orderFish.setPaymentMethod(orderFishForm.getPaymentMethod());
        orderFish.setStatus(orderFishForm.getStatus());

        OrderFish updatedOrderFish = orderFishRepository.save(orderFish);
        log.info("Order updated with ID: {}", updatedOrderFish.getOrderFishId());
        return OrderFishDto.from(updatedOrderFish);
    }
}
