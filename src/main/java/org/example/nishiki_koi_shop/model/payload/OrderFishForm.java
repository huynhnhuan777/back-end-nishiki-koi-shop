package org.example.nishiki_koi_shop.model.payload;

import lombok.Data;
import org.example.nishiki_koi_shop.model.entity.OrderFish;

import java.time.LocalDate;

@Data
public class OrderFishForm {
    private long userId;
    private long totalAmount;
    private OrderFish.Status status;
    private LocalDate deliveryDate;
    private String paymentMethod;
    private LocalDate createdDate;
}
