package org.example.nishiki_koi_shop.repository;

import org.example.nishiki_koi_shop.model.dto.FeedbackDto;
import org.example.nishiki_koi_shop.model.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> findByUserId(long id);
}
