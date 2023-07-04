package com.challenge.backend.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.challenge.backend.enuns.Status;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class OrderModel {


    @Id
    @GeneratedValue(generator = "uuid2")
    private UUID id;
    
    private Long userId;
    
    private Status status;
    
    private BigDecimal precoTotal;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private List<ItemModel> itens = new ArrayList();
}
