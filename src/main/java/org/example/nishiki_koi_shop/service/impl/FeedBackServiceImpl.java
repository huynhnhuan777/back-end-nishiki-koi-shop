package org.example.nishiki_koi_shop.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.nishiki_koi_shop.model.dto.FeedbackDto;
import org.example.nishiki_koi_shop.model.entity.*;
import org.example.nishiki_koi_shop.model.payload.FeedbackForm;
import org.example.nishiki_koi_shop.repository.FeedbackRepository;
import org.example.nishiki_koi_shop.repository.FishRepository;
import org.example.nishiki_koi_shop.repository.TourRepository;
import org.example.nishiki_koi_shop.repository.UserRepository;
import org.example.nishiki_koi_shop.service.FeedBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FeedBackServiceImpl implements FeedBackService {

    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TourRepository tourRepository;
    @Autowired
    private FishRepository fishRepository;

    @Override
    public FeedbackDto createFeedback(FeedbackForm feedbackForm, Principal principal) {
        User user = userRepository.findById(feedbackForm.getUserId()).orElseThrow(() -> new IllegalArgumentException("User co ID nay khong ton tai"));

        if(!user.getEmail().equals(principal.getName())){
            log.warn("Unauthorized access attempt for user ID: {}", feedbackForm.getUserId());
            throw new SecurityException("Unauthorized access");
        }
        Tour tour = tourRepository.findById(feedbackForm.getTourId()).orElseThrow(()->new IllegalArgumentException("Tour not found "));

        Fish fish = fishRepository.findById(feedbackForm.getFishId()).orElseThrow(() -> new IllegalArgumentException("fish not found"));

        Feedback feedback = Feedback.builder()
                .user(user)
                .tour(tour)
                .fish(fish)
                .createdDate(LocalDate.now())
                .comment(feedbackForm.getComment())
                .rating(feedbackForm.getRating())
                .build();

        Feedback savedFeedback = feedbackRepository.save(feedback);
        return FeedbackDto.from(savedFeedback);
    }

    @Override
    public FeedbackDto getFeedbackById(long id) {
        Feedback feedback = feedbackRepository.findById(id).orElseThrow(() -> new RuntimeException("Feedback not found"));
        return FeedbackDto.from(feedback);
    }

    @Override
    public List<FeedbackDto> getFeedbacksByUserId(long id, Principal principal) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!user.getEmail().equals(principal.getName())) {
            log.warn("Unauthorized access attempt for user ID: {}", id);
            throw new SecurityException("Unauthorized access");
        }

        List<Feedback> feedbacks = feedbackRepository.findByUserId(id);

        log.info("Fetching feedbacks for user: {}", user.getUsername());

        return feedbacks.stream().map(FeedbackDto::from).collect(Collectors.toList());
    }


    @Override
    public FeedbackDto getFeedbackById(long id, Principal principal) {

        Feedback feedback = feedbackRepository.findById(id).orElseThrow(() -> new RuntimeException("Feedback not found"));

        if (!feedback.getUser().getEmail().equals(principal.getName())) {
            log.warn("Unauthorized access attempt for order ID: {}", id);
            throw new SecurityException("Unauthorized access");
        }

        log.info("Order retrieved with ID: {}", id);
        return FeedbackDto.from(feedback);
    }

    @Override
    public List<FeedbackDto> getAllFeedbacks() {
        return feedbackRepository.findAll().stream()
                .map(FeedbackDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public FeedbackDto updateFeedback(long id, FeedbackForm feedbackForm) {
        User user = userRepository.findById(feedbackForm.getUserId()).orElseThrow(() -> new IllegalArgumentException("User co ID nay khong ton tai"));

        Tour tour = tourRepository.findById(feedbackForm.getTourId()).orElseThrow(()->new IllegalArgumentException("Tour not found "));

        Fish fish = fishRepository.findById(feedbackForm.getFishId()).orElseThrow(() -> new IllegalArgumentException("fish not found"));

        Feedback feedback = feedbackRepository.findById(id).orElseThrow(() -> new RuntimeException("Feedback not found"));

        feedback.setUser(user);
        feedback.setTour(tour);
        feedback.setFish(fish);
        feedback.setRating(feedbackForm.getRating());
        feedback.setComment(feedbackForm.getComment());
        feedback.setCreatedDate(LocalDate.now());

        return FeedbackDto.from(feedbackRepository.save(feedback));
    }

    @Override
    public void deleteFeedback(long id) {
        Feedback feedback = feedbackRepository.findById(id).orElseThrow(() -> new RuntimeException("Feedback not found"));
        feedbackRepository.delete(feedback);
    }

}
