package org.example.nishiki_koi_shop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.nishiki_koi_shop.model.dto.OrderFishDetailDto;
import org.example.nishiki_koi_shop.model.dto.OrderFishDto;
import org.example.nishiki_koi_shop.model.dto.OrderTourDetailDto;
import org.example.nishiki_koi_shop.model.entity.OrderFishDetail;
import org.example.nishiki_koi_shop.model.payload.OrderFishDetailForm;
import org.example.nishiki_koi_shop.model.payload.OrderTourDetailForm;
import org.example.nishiki_koi_shop.service.OrderFishDetailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/order-fish-details")
@RequiredArgsConstructor
public class OrderFishDetailController {

    private final OrderFishDetailService orderFishDetailService;

    @PostMapping("/create")
    public ResponseEntity<OrderFishDetailDto> createOrderFishDetail(@RequestBody OrderFishDetailForm orderFishDetailForm) {
        return new ResponseEntity<>(orderFishDetailService.createOrderFishDetail(orderFishDetailForm), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderFishDetailDto> getOrderFishDetailById(@PathVariable long id, Principal principal) {
        return new ResponseEntity<>(orderFishDetailService.getOrderFishDetailById(id, principal), HttpStatus.OK);
    }

}
