package org.example.nishiki_koi_shop.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.nishiki_koi_shop.model.dto.OrderFishDetailDto;
import org.example.nishiki_koi_shop.model.entity.Fish;
import org.example.nishiki_koi_shop.model.entity.OrderFish;
import org.example.nishiki_koi_shop.model.entity.OrderFishDetail;
import org.example.nishiki_koi_shop.model.entity.User;
import org.example.nishiki_koi_shop.model.payload.OrderFishDetailForm;
import org.example.nishiki_koi_shop.repository.FishRepository;
import org.example.nishiki_koi_shop.repository.OrderFishDetailRepository;
import org.example.nishiki_koi_shop.repository.OrderFishRepository;
import org.example.nishiki_koi_shop.repository.UserRepository;
import org.example.nishiki_koi_shop.service.OrderFishDetailService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class OrderFishDetailServiceImpl implements OrderFishDetailService {

    private final OrderFishDetailRepository orderFishDetailRepository;
    private final FishRepository fishRepository;
    private final OrderFishRepository orderFishRepository;
    private final UserRepository userRepository;

    @Override
    public OrderFishDetailDto createOrderFishDetail(OrderFishDetailForm orderFishDetailForm) {
        log.info("Creating order fish detail for OrderFishId: {} and FishId: {}", orderFishDetailForm.getOrderFishId(), orderFishDetailForm.getFishId());

        Fish fish = fishRepository.findById(orderFishDetailForm.getFishId()).orElseThrow(() -> {
            log.error("Fish not found with ID: {}", orderFishDetailForm.getFishId());
            return new RuntimeException("Fish not found");
        });

        OrderFish orderFish = orderFishRepository.findById(orderFishDetailForm.getOrderFishId()).orElseThrow(() -> {
            log.error("OrderFish not found with ID: {}", orderFishDetailForm.getOrderFishId());
            return new RuntimeException("OrderFish not found");
        });

        OrderFishDetail orderFishDetail = OrderFishDetail.builder()
                .fish(fish)
                .orderFish(orderFish)
                .quantity(orderFishDetailForm.getQuantity())
                .price(orderFishDetailForm.getPrice())
                .build();

        // Lưu đối tượng vào cơ sở dữ liệu
        OrderFishDetail savedOrderFishDetail = orderFishDetailRepository.save(orderFishDetail);
        log.info("OrderFishDetail created successfully with ID: {}", savedOrderFishDetail.getOrderFishDetailId());

        // Chuyển đối tượng đã lưu thành DTO và trả về
        return OrderFishDetailDto.from(savedOrderFishDetail);
    }

    @Override
    public OrderFishDetailDto getOrderFishDetailById(long id) {
        log.info("Fetching OrderFishDetail with ID: {}", id);

        Optional<OrderFishDetail> orderFishDetailOptional = orderFishDetailRepository.findById(id);
        if (orderFishDetailOptional.isPresent()) {
            log.info("OrderFishDetail found with ID: {}", id);
            return OrderFishDetailDto.from(orderFishDetailOptional.get());
        }

        log.error("OrderFishDetail not found with ID: {}", id);
        throw new RuntimeException("OrderFishDetail not found");
    }

    @Override
    public OrderFishDetailDto getOrderFishDetailById(long id, Principal principal) {
        log.info("Fetching OrderFishDetail with ID: {} for user: {}", id, principal.getName());

        // Lấy email từ Principal
        String email = principal.getName();

        // Tìm người dùng hiện tại từ email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("User not found with email: {}", email);
                    return new EntityNotFoundException("User không tồn tại với email: " + email);
                });

        // Lấy OrderFishDetail từ cơ sở dữ liệu
        OrderFishDetail orderFishDetail = orderFishDetailRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("OrderFishDetail not found with ID: {}", id);
                    return new RuntimeException("OrderFishDetail not found");
                });

        // Kiểm tra quyền truy cập: chỉ người tạo mới có quyền xem chi tiết
        if (orderFishDetail.getOrderFish().getUser().getUserId() != user.getUserId()) {
            log.error("Access denied for user: {} to OrderFishDetail ID: {}", email, id);
            throw new AccessDeniedException("Bạn không có quyền truy cập thông tin này.");
        }

        // Chuyển đổi OrderFishDetail thành DTO và trả về
        log.info("OrderFishDetail fetched successfully with ID: {}", id);
        return OrderFishDetailDto.from(orderFishDetail);
    }

    @Override
    public List<OrderFishDetailDto> getAllOrderFishDetails() {
        log.info("Fetching all OrderFishDetails");

        List<OrderFishDetail> orderFishDetails = orderFishDetailRepository.findAll();
        log.info("Fetched {} OrderFishDetails", orderFishDetails.size());

        return orderFishDetails.stream()
                .map(OrderFishDetailDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public OrderFishDetailDto updateOrderFishDetail(long id, OrderFishDetailForm orderFishDetailForm) {
        log.info("Updating OrderFishDetail with ID: {}", id);

        OrderFishDetail orderFishDetail = orderFishDetailRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException(("OrderFishDetail not found with ID: " + id)));

        orderFishDetail.setQuantity(orderFishDetailForm.getQuantity());
        orderFishDetail.setPrice(orderFishDetailForm.getPrice());

        OrderFishDetail updatedOrderFishDetail = orderFishDetailRepository.save(orderFishDetail);
        log.info("OrderFishDetail updated successfully with ID: {}", updatedOrderFishDetail.getOrderFishDetailId());

        return OrderFishDetailDto.from(updatedOrderFishDetail);
    }
}
