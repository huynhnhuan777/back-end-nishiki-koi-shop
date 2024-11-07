package org.example.nishiki_koi_shop.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "order_fish_detail")
public class OrderFishDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderFishDetailId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_fish_id", nullable = false)
    private OrderFish orderFish;

    // Tham chiếu đến loại cá
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fish_id", nullable = false)
    private Fish fish;

    private Integer quantity;

    private BigDecimal price;

}
