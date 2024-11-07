package org.example.nishiki_koi_shop.service;

import org.example.nishiki_koi_shop.model.dto.FishTypeDto;
import org.example.nishiki_koi_shop.model.payload.FishTypeForm;

import java.util.List;

public interface FishTypeService {
    List<FishTypeDto> getAllFishTypes();
    FishTypeDto getFishTypeById(long id);
    FishTypeDto createFishType(FishTypeForm fishTypeForm);
    FishTypeDto updateFishType(long id, FishTypeForm fishTypeForm);
    String deleteFishTypeById(long id);
}
