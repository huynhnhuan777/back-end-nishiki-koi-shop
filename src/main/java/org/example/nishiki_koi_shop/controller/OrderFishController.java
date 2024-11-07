package org.example.nishiki_koi_shop.controller;

import lombok.RequiredArgsConstructor;
import org.example.nishiki_koi_shop.model.dto.OrderFishDto;
import org.example.nishiki_koi_shop.model.dto.OrderTourDto;
import org.example.nishiki_koi_shop.model.entity.OrderFish;
import org.example.nishiki_koi_shop.model.payload.OrderFishForm;
import org.example.nishiki_koi_shop.model.payload.OrderTourForm;
import org.example.nishiki_koi_shop.service.OrderFishService;
import org.example.nishiki_koi_shop.service.OrderTourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order-fishes")
public class OrderFishController{
    @Autowired
    private OrderFishService orderFishService;

    @PostMapping("/create")
    public ResponseEntity<OrderFishDto> createOrderFish(@RequestBody OrderFishForm orderFishForm, Principal principal) {
        return new ResponseEntity<>(orderFishService.createOrderFish(orderFishForm, principal), HttpStatus.CREATED);
    }
    @GetMapping("/history/user/{id}")
    public ResponseEntity<List<OrderFishDto>> getOrderToursByUserId(@PathVariable long id, Principal principal) {
        return new ResponseEntity<>(orderFishService.getOrderFishByUserId(id, principal), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<OrderFishDto> getOrderTourById(@PathVariable long id, Principal principal) {
        return new ResponseEntity<>(orderFishService.getOrderFishById(id, principal), HttpStatus.OK);
    }
}
