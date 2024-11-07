package org.example.nishiki_koi_shop.service;

import org.example.nishiki_koi_shop.model.dto.FarmDto;
import org.example.nishiki_koi_shop.model.dto.FishDto;
import org.example.nishiki_koi_shop.model.payload.FarmForm;
import org.example.nishiki_koi_shop.model.payload.FishForm;

import java.util.List;

public interface FarmService {
    List<FarmDto> getAllFarm();
    FarmDto createFarm(FarmForm farmForm);
    FarmDto getFarmById(long id);
    FarmDto updateFarm(Long id, FarmForm farmForm);
    void deleteFarm(Long id);
}
