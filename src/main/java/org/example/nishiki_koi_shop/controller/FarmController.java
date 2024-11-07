package org.example.nishiki_koi_shop.controller;

import lombok.RequiredArgsConstructor;
import org.example.nishiki_koi_shop.model.dto.FarmDto;
import org.example.nishiki_koi_shop.service.FarmService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/farms")
@RequiredArgsConstructor
public class FarmController {
    private final FarmService farmService;
    @GetMapping("/get-all-farm")
    public ResponseEntity<List<FarmDto>> getAllFarms() {
        return new ResponseEntity<>(farmService.getAllFarm(), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<FarmDto> getFarmById(@PathVariable("id") long id) {
        return new ResponseEntity<>(farmService.getFarmById(id), HttpStatus.OK);
    }
}
