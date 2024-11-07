package org.example.nishiki_koi_shop.service;

import org.example.nishiki_koi_shop.model.dto.OrderFishDetailDto;
import org.example.nishiki_koi_shop.model.payload.OrderFishDetailForm;

import java.security.Principal;
import java.util.List;

public interface OrderFishDetailService {
    OrderFishDetailDto createOrderFishDetail(OrderFishDetailForm orderFishDetailForm);
    OrderFishDetailDto getOrderFishDetailById(long id, Principal principal);
    OrderFishDetailDto getOrderFishDetailById(long id);
    List<OrderFishDetailDto> getAllOrderFishDetails();
    OrderFishDetailDto updateOrderFishDetail(long id, OrderFishDetailForm orderFishDetailForm);
}
