package org.example.nishiki_koi_shop.model.payload;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderFishDetailForm {
    private long orderFishId;
    private long fishId;
    private Integer quantity;
    private BigDecimal price;
}
