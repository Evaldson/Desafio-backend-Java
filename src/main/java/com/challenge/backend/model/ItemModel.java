package com.challenge.backend.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class ItemModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long idItem;
	private Long id;
	private Double preco;
	private Long quantidade;
	private BigDecimal precoParcial;

	private void updatePartialAmount() {
		if (preco != null && quantidade != null) {
			precoParcial = BigDecimal.valueOf(preco).multiply(BigDecimal.valueOf(quantidade));
		}
	}

	public void incrementAmount() {
		if (quantidade != null) {
			quantidade++;
			updatePartialAmount();
		}
	}

}
