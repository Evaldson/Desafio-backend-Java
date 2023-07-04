package com.challenge.backend.model.input;

import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class OrderUpdateInput {
	private UUID id;
    private Long userId;
    private List<ProductInput> items;
}
