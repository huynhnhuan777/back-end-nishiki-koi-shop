package org.example.nishiki_koi_shop.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.nishiki_koi_shop.model.dto.OrderTourDetailDto;
import org.example.nishiki_koi_shop.model.entity.OrderTour;
import org.example.nishiki_koi_shop.model.entity.OrderTourDetail;
import org.example.nishiki_koi_shop.model.entity.Tour;
import org.example.nishiki_koi_shop.model.entity.User;
import org.example.nishiki_koi_shop.model.payload.OrderTourDetailForm;
import org.example.nishiki_koi_shop.repository.OrderTourDetailRepository;
import org.example.nishiki_koi_shop.repository.OrderTourRepository;
import org.example.nishiki_koi_shop.repository.TourRepository;
import org.example.nishiki_koi_shop.repository.UserRepository;
import org.example.nishiki_koi_shop.service.OrderTourDetailService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderTourDetailServiceImpl implements OrderTourDetailService {

    private final OrderTourDetailRepository orderTourDetailRepository;
    private final OrderTourRepository orderTourRepository;
    private final TourRepository tourRepository;
    private final UserRepository userRepository;

    @Override
    public OrderTourDetailDto createOrderTourDetail(OrderTourDetailForm orderTourDetailForm) {
        log.info("Creating OrderTourDetail with form: {}", orderTourDetailForm);

        Tour tour = tourRepository.findById(orderTourDetailForm.getTourId())
                .orElseThrow(() -> new EntityNotFoundException("Tour not found with id: " + orderTourDetailForm.getTourId()));

        OrderTour orderTour = orderTourRepository.findById(orderTourDetailForm.getOrderTourId())
                .orElseThrow(() -> new EntityNotFoundException("OrderTour not found with id: " + orderTourDetailForm.getOrderTourId()));

        OrderTourDetail orderTourDetail = OrderTourDetail.builder()
                .numberOfPeople(orderTourDetailForm.getNumberOfPeople())
                .price(orderTourDetailForm.getPrice())
                .orderTour(orderTour)
                .tour(tour)
                .build();

        orderTourDetail = orderTourDetailRepository.save(orderTourDetail);
        log.info("OrderTourDetail created with ID: {}", orderTourDetail.getOrderTourDetailId());

        return OrderTourDetailDto.from(orderTourDetail);
    }

    @Override
    public OrderTourDetailDto getOrderTourDetailById(long id, Principal principal) {
        // Lấy email từ Principal
        String email = principal.getName();

        // Tìm người dùng hiện tại từ email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User không tồn tại với email: " + email));

        // Lấy OrderTourDetail từ cơ sở dữ liệu
        OrderTourDetail orderTourDetail = orderTourDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order không tìm thấy với id: " + id));

        // Kiểm tra quyền truy cập: chỉ người tạo mới có quyền xem chi tiết
        if (orderTourDetail.getOrderTour().getUser().getUserId() != user.getUserId()) {
            throw new AccessDeniedException("Bạn không có quyền truy cập thông tin này.");
        }

        // Chuyển đổi OrderTourDetail thành OrderTourDetailDto và trả về
        return OrderTourDetailDto.from(orderTourDetail);
    }

    @Override
    public OrderTourDetailDto getOrderTourDetailById(long id) {
        log.info("Fetching OrderTourDetail with id: {}", id);

        OrderTourDetail orderTourDetail = orderTourDetailRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("OrderTourDetail not found with id: " + id));

        return OrderTourDetailDto.from(orderTourDetail);
    }

    @Override
    public List<OrderTourDetailDto> getAllOrderTourDetails() {
        log.info("Fetching all OrderTourDetails");

        return orderTourDetailRepository.findAll().stream()
                .map(OrderTourDetailDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public OrderTourDetailDto updateOrderTourDetail(long id, OrderTourDetailForm form) {
        log.info("Updating OrderTourDetail with id: {}", id);

        OrderTourDetail orderTourDetail = orderTourDetailRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("OrderTourDetail not found with id: " + id));

        Tour tour = tourRepository.findById(form.getTourId())
                .orElseThrow(() -> new EntityNotFoundException("Tour not found with id: " + form.getTourId()));

        orderTourDetail.setNumberOfPeople(form.getNumberOfPeople());
        orderTourDetail.setPrice(form.getPrice());
        orderTourDetail.setTour(tour);

        OrderTourDetail updatedDetail = orderTourDetailRepository.save(orderTourDetail);
        log.info("OrderTourDetail updated with ID: {}", updatedDetail.getOrderTourDetailId());

        return OrderTourDetailDto.from(updatedDetail);
    }
}
