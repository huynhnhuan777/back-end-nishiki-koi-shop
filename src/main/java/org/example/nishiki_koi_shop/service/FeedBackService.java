package org.example.nishiki_koi_shop.service;

import org.example.nishiki_koi_shop.model.dto.FeedbackDto;
import org.example.nishiki_koi_shop.model.payload.FeedbackForm;

import java.security.Principal;
import java.util.List;

public interface FeedBackService {
    //user
    FeedbackDto createFeedback(FeedbackForm feedbackForm, Principal principal);
    List<FeedbackDto> getFeedbacksByUserId(long id, Principal principal);
    FeedbackDto getFeedbackById(long id, Principal principal);

    // manager
    List<FeedbackDto> getAllFeedbacks();
    FeedbackDto getFeedbackById(long id);
    FeedbackDto updateFeedback(long id, FeedbackForm feedbackForm);
    void deleteFeedback(long id);

}
