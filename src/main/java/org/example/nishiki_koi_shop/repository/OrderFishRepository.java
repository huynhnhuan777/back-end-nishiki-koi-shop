package org.example.nishiki_koi_shop.repository;

import org.example.nishiki_koi_shop.model.entity.OrderFish;
import org.example.nishiki_koi_shop.model.entity.OrderTour;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface OrderFishRepository extends JpaRepository<OrderFish, Long> {
    List<OrderFish> findByUserId(long userId);
}
