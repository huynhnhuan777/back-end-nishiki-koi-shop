package org.example.nishiki_koi_shop.service;

import org.example.nishiki_koi_shop.model.dto.OrderFishDto;
import org.example.nishiki_koi_shop.model.payload.OrderFishForm;

import java.security.Principal;
import java.util.List;

public interface OrderFishService {
    // user
    OrderFishDto createOrderFish(OrderFishForm orderFishForm, Principal principal);
    OrderFishDto getOrderFishById(long id, Principal principal);
    List<OrderFishDto> getOrderFishByUserId(long id, Principal principal);
    //manager
    List<OrderFishDto> getAllOrderFishes();
    OrderFishDto getOrderFishById(long id);
    OrderFishDto updateOrderFish(long id, OrderFishForm orderFishForm);
}
