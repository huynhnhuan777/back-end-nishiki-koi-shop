package org.example.nishiki_koi_shop.model.dto;

import lombok.Builder;
import lombok.Data;
import org.example.nishiki_koi_shop.model.entity.OrderFish;

import java.time.LocalDate;

@Data
@Builder
public class OrderFishDto {
    private long orderFishId;
    private long userId;
    private long totalAmount;
    private OrderFish.Status status;
    private LocalDate deliveryDate;
    private String paymentMethod;
    private LocalDate createdDate;

    public static OrderFishDto from(OrderFish orderFish) {
        return OrderFishDto.builder()
                .orderFishId(orderFish.getOrderFishId())
                .userId(orderFish.getUser().getUserId())
                .totalAmount(orderFish.getTotalAmount())
                .status(orderFish.getStatus())
                .deliveryDate(orderFish.getDeliveryDate())
                .paymentMethod(orderFish.getPaymentMethod())
                .createdDate(orderFish.getCreatedDate())
                .build();
    }
}
