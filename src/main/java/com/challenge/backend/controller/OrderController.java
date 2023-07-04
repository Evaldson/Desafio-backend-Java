package com.challenge.backend.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.challenge.backend.model.OrderModel;
import com.challenge.backend.model.input.OrderInput;
import com.challenge.backend.model.input.OrderUpdateInput;
import com.challenge.backend.model.input.StatusInput;
import com.challenge.backend.service.OrderService;


import lombok.AllArgsConstructor;


@AllArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
	
	private OrderService orderService;

	@PostMapping()
	public ResponseEntity<OrderModel>  addOrder(@RequestBody OrderInput orderInput) {
		
		Optional<OrderModel> newOrder = orderService.addOrder(orderInput);
		
		if(newOrder.isPresent()) {
			return ResponseEntity.ok(newOrder.get());
		}
		
		return ResponseEntity.badRequest().build();
	}
	
	@PatchMapping()
	public ResponseEntity<HttpStatus> status(@RequestBody StatusInput statusInput){
		
		Optional<OrderModel> order = orderService.addStatusToOrder(statusInput);
		
		if(order.isPresent()) {
			return ResponseEntity.ok().build();
		}
	
		return ResponseEntity.notFound().build();
	}
	
	@PutMapping()
	public ResponseEntity<OrderModel> addItemToOrder(@RequestBody OrderUpdateInput orderUpdateInput){
		
		Optional<OrderModel> order = orderService.addItemToOrder(orderUpdateInput);
		
		if(order.isPresent()) {
			return ResponseEntity.ok(order.get());
		}
	
		return ResponseEntity.notFound().build();
	}
	
}
