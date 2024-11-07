package org.example.nishiki_koi_shop.service.impl;

import org.example.nishiki_koi_shop.model.dto.FishDto;
import org.example.nishiki_koi_shop.model.dto.FishTypeDto;
import org.example.nishiki_koi_shop.model.entity.FishType;
import org.example.nishiki_koi_shop.model.payload.FishTypeForm;
import org.example.nishiki_koi_shop.repository.FishTypeRepository;
import org.example.nishiki_koi_shop.service.FishTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FishTypeServiceImpl implements FishTypeService {
    @Autowired
    private FishTypeRepository fishTypeRepository;

    @Override
    public List<FishTypeDto> getAllFishTypes() {
        List<FishType> fishTypes = fishTypeRepository.findAll();
        return fishTypes.stream()
                .map(FishTypeDto::fromFishType)
                .collect(Collectors.toList());
    }

    @Override
    public FishTypeDto getFishTypeById(long id) {
        return FishTypeDto.fromFishType(fishTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fish type not found with id " + id)));
    }

    @Override
    public FishTypeDto createFishType(FishTypeForm fishTypeForm) {

        if (fishTypeRepository.existsByName(fishTypeForm.getName())) {
            throw new IllegalArgumentException("Tên loại cá đã tồn tại. Vui lòng chọn tên khác.");
        }

        FishType fishType = new FishType();

        fishType.setName(fishTypeForm.getName());
        fishType.setDescription(fishTypeForm.getDescription());
        fishType.setCreatedDate(LocalDate.now());
        return FishTypeDto.fromFishType(fishTypeRepository.save(fishType));
    }

    @Override
    public FishTypeDto updateFishType(long id, FishTypeForm fishTypeForm) {
        Optional<FishType> fishType = fishTypeRepository.findById(id);
        if (fishType.isPresent()) {
            FishType oldFishType = fishType.get();
            oldFishType.setName(fishTypeForm.getName());
            oldFishType.setDescription(fishTypeForm.getDescription());
            return FishTypeDto.fromFishType(fishTypeRepository.save(oldFishType));
        } else {
            throw new RuntimeException("Fish type not found with id {} " + id );
        }
    }

    @Override
    public String deleteFishTypeById(long id) {
        FishType fishType = fishTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fish type not found with id " + id));

        fishTypeRepository.delete(fishType);
        return "Xóa Fish Type " + fishType.getName() + " thành công";
    }

}
