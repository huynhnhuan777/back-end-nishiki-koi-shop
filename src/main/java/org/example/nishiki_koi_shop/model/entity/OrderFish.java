package org.example.nishiki_koi_shop.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "order_fish")
public class OrderFish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderFishId;

    private long totalAmount;
    @Enumerated(EnumType.STRING)
    private Status status;
    private LocalDate deliveryDate;
    private String paymentMethod;
    private LocalDate createdDate;

    public enum Status {
        PENDING, APPROVED, REJECTED, CANCELLED
    }

    @ManyToOne(fetch = FetchType.LAZY) // Quan hệ với User (nếu cần)
    @JoinColumn(name = "user_id", nullable = false) // Cột tham chiếu tới bảng User
    private User user;

    @OneToOne(mappedBy = "orderFish", cascade = CascadeType.ALL, fetch = FetchType.LAZY) // Quan hệ với OrderFishDetail
    private OrderFishDetail orderFishDetail;
}
