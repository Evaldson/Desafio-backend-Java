package com.challenge.backend.model.input;

import java.util.List;

import lombok.Data;

@Data
public class OrderInput {

    private Long userId;
    private List<ProductInput> products;

}
