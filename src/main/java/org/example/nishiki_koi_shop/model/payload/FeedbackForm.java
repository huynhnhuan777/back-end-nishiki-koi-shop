package org.example.nishiki_koi_shop.model.payload;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.nishiki_koi_shop.model.entity.Feedback;
import org.example.nishiki_koi_shop.model.entity.Fish;
import org.example.nishiki_koi_shop.model.entity.Tour;
import org.example.nishiki_koi_shop.model.entity.User;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackForm {

    private String comment;
    private long userId;
    private long tourId;
    private long fishId;
    private Feedback.Rating rating;
    private LocalDate createdDate;
}
