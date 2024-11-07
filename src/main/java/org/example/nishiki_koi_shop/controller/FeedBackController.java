package org.example.nishiki_koi_shop.controller;


import lombok.RequiredArgsConstructor;
import org.example.nishiki_koi_shop.model.dto.FeedbackDto;
import org.example.nishiki_koi_shop.model.payload.FeedbackForm;
import org.example.nishiki_koi_shop.service.FeedBackService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/feedbacks")
@RequiredArgsConstructor
public class FeedBackController {

    private final FeedBackService feedbackService;

    @PostMapping("/create")
    public ResponseEntity<FeedbackDto> createFeedback(@RequestBody FeedbackForm feedbackForm, Principal principal) {
        return new ResponseEntity<>(feedbackService.createFeedback(feedbackForm, principal), HttpStatus.CREATED);
    };
    @GetMapping("/history/{id}")
    public ResponseEntity<List<FeedbackDto>> getFeedbacksByUserId(@PathVariable("id") long id, Principal principal){
        return new ResponseEntity<>(feedbackService.getFeedbacksByUserId(id, principal), HttpStatus.OK);
    };
    @GetMapping("/{id}")
    public ResponseEntity<FeedbackDto> getFeedbackById(@PathVariable("id") long id, Principal principal){
        return new ResponseEntity<>(feedbackService.getFeedbackById(id, principal), HttpStatus.OK);
    };
}

