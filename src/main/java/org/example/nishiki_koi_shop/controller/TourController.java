package org.example.nishiki_koi_shop.controller;

import lombok.RequiredArgsConstructor;
import org.example.nishiki_koi_shop.model.dto.TourDto;
import org.example.nishiki_koi_shop.service.TourService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tours")
@RequiredArgsConstructor
public class TourController {
    private final TourService tourService;
    @GetMapping("/get-all-tours")
    public ResponseEntity<List<TourDto>> getAllTours() {
        return new ResponseEntity<>(tourService.getAllTour(), HttpStatus.OK);
    }
    @GetMapping("{id}")
    public ResponseEntity<TourDto> getTourById(@PathVariable("id") long id) {
        return new ResponseEntity<>(tourService.getTourById(id), HttpStatus.OK);
    }
}
