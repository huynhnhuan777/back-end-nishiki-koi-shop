package org.example.nishiki_koi_shop.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.nishiki_koi_shop.model.entity.Feedback;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedbackDto {
    private long feedbackId;
    private String comment;
    private long userId;
    private long tourId;
    private long fishId;
    private Feedback.Rating rating;
    private LocalDate createdDate;

    public static FeedbackDto from(Feedback feedback) {
        return FeedbackDto.builder()
                .feedbackId(feedback.getFeedBackId())
                .comment(feedback.getComment())
                .createdDate(feedback.getCreatedDate())
                .userId(feedback.getUser().getUserId())
                .tourId(feedback.getTour().getTourId())
                .fishId(feedback.getFish().getFishId())
                .rating(feedback.getRating())
                .build();
    }
}
