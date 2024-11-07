package org.example.nishiki_koi_shop.model.dto;

import lombok.Builder;
import lombok.Data;
import org.example.nishiki_koi_shop.model.entity.OrderFishDetail;

import java.math.BigDecimal;

@Builder
@Data
public class OrderFishDetailDto {
    private long orderFishDetailId;
    private long orderFishId;
    private long fishId;
    private Integer quantity;
    private BigDecimal price;

    public static OrderFishDetailDto from(OrderFishDetail orderFishDetail) {
        return OrderFishDetailDto.builder()
                .orderFishDetailId(orderFishDetail.getOrderFishDetailId())
                .orderFishId(orderFishDetail.getOrderFish().getOrderFishId()) // Assuming `OrderFish` has `getOrderFishId()`
                .fishId(orderFishDetail.getFish().getFishId()) // Assuming `Fish` has `getFishId()`
                .quantity(orderFishDetail.getQuantity())
                .price(orderFishDetail.getPrice())
                .build();
    }
}
