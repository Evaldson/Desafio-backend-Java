package com.challenge.backend.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.challenge.backend.enuns.Status;
import com.challenge.backend.model.ItemModel;
import com.challenge.backend.model.OrderModel;
import com.challenge.backend.model.input.OrderInput;
import com.challenge.backend.model.input.OrderUpdateInput;
import com.challenge.backend.model.input.ProductInput;
import com.challenge.backend.model.input.StatusInput;
import com.challenge.backend.model.output.ProductOutput;
import com.challenge.backend.model.output.UserOutput;
import com.challenge.backend.repository.OrderRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrderService {

	private ProductApiClient productApi;
	private UserApiClient userApi;
	private OrderRepository orderRepository;

	@Transactional
	public Optional<OrderModel>  addOrder(OrderInput orderInput) {
	
		UserOutput user = userApi.getUserById(orderInput.getUserId()).block();

		if (user != null && !orderInput.getProducts().isEmpty()) {
			OrderModel newOrder = new OrderModel();
			newOrder.setUserId(Long.valueOf(user.getId()));
			addOrUpdateItem(orderInput.getProducts(), newOrder);
			if (!newOrder.getItens().isEmpty()) {
				return Optional.of(orderRepository.save(newOrder));
			}
		}

		return Optional.empty();
	}

	@Transactional
	public Optional<OrderModel> addItemToOrder(OrderUpdateInput orderInput) {

		Optional<OrderModel> order = orderRepository.findById(orderInput.getId());

		if (order.isPresent()) {
			OrderModel updatedOrder = order.get();
			addOrUpdateItem(orderInput.getItems(), updatedOrder);
			orderRepository.save(order.get());
			return order;
		}

		return Optional.empty();
	}

	@Transactional
	public Optional<OrderModel> addStatusToOrder(StatusInput statusInput) {
		
		Optional<OrderModel> order = orderRepository.findById(statusInput.getId());

		if (order.isPresent()) {
			order.get().setStatus(Status.CONCLUDED);
			orderRepository.save(order.get());
			return order;
		}

		return Optional.empty();
	}

	private void addNewItem(OrderModel newOrder, ProductOutput productOutput) {
		ItemModel newItem = new ItemModel();
		newItem.setQuantidade(1L);
		newItem.setId(Long.valueOf(productOutput.getId()));
		newItem.setPrecoParcial(BigDecimal.valueOf(productOutput.getPrice()));
		newItem.setPreco(productOutput.getPrice());
		newOrder.getItens().add(newItem);
	}

	private void addOrUpdateItem(List<ProductInput> productsInput, OrderModel newOrder) {
		
		ProductOutput productOutput = null;
		List<ProductInput> products = productsInput;
		
			for (ProductInput productInput : products) {
				productOutput = productApi.getProductById(productInput.getId()).block();
				if (productOutput != null) {
					boolean itemExist = newOrder.getItens().stream()
														.anyMatch(item -> item.getId()
														.equals(Long.valueOf(productInput.getId())));

					if (itemExist) {
						newOrder.getItens().stream()
											.filter(item -> item.getId() == Long.valueOf(productInput.getId()))
											.findFirst()
											.ifPresent(ItemModel::incrementAmount);
					} else {
						addNewItem(newOrder, productOutput);
					}
				}
			}	

		BigDecimal totalPrice = newOrder.getItens().stream()
										.map(ItemModel::getPrecoParcial)
										.reduce(BigDecimal.ZERO, BigDecimal::add);

		newOrder.setPrecoTotal(totalPrice);
		newOrder.setStatus(Status.PENDING);
	}

}
