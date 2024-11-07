package org.example.nishiki_koi_shop.repository;

import org.example.nishiki_koi_shop.model.entity.OrderFishDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderFishDetailRepository extends JpaRepository<OrderFishDetail, Long> {
}
