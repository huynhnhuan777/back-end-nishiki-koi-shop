package org.example.nishiki_koi_shop.service;

import org.example.nishiki_koi_shop.model.dto.TourDto;
import org.example.nishiki_koi_shop.model.payload.TourForm;

import java.util.List;

public interface TourService {
    List<TourDto> getAllTour();

    TourDto createTour(TourForm tourForm);
}