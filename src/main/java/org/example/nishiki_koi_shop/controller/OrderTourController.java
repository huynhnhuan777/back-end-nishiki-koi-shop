package org.example.nishiki_koi_shop.controller;

import lombok.RequiredArgsConstructor;
import org.example.nishiki_koi_shop.model.dto.OrderTourDto;
import org.example.nishiki_koi_shop.model.payload.OrderTourForm;
import org.example.nishiki_koi_shop.service.OrderTourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order-tours")
public class OrderTourController {
    @Autowired
    private OrderTourService orderTourService;

    @PostMapping("/create")
    public ResponseEntity<OrderTourDto> createOrderTour(@RequestBody OrderTourForm orderTourForm, Principal principal) {
        OrderTourDto createdOrderTour = orderTourService.createOrderTour(orderTourForm, principal);
        return new ResponseEntity<>(createdOrderTour, HttpStatus.CREATED);
    }
    @GetMapping("/history/user/{id}")
    public ResponseEntity<List<OrderTourDto>> getOrderToursByUserId(@PathVariable long id, Principal principal) {
        List<OrderTourDto> orderTours = orderTourService.getOrderToursByUserId(id, principal);
        return new ResponseEntity<>(orderTours, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<OrderTourDto> getOrderTourById(@PathVariable long id, Principal principal) {
        OrderTourDto orderTourDto = orderTourService.getOrderTourById(id, principal);
        return new ResponseEntity<>(orderTourDto, HttpStatus.OK);
    }
}
