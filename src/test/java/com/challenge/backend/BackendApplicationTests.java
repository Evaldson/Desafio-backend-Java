package com.challenge.backend;

import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

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
import com.challenge.backend.service.OrderService;
import com.challenge.backend.service.ProductApiClient;
import com.challenge.backend.service.UserApiClient;

import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BackendApplicationTests {

    @Mock
    private ProductApiClient productApi;

    @Mock
    private UserApiClient userApi;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddOrder() {
        // Mock input data
        int userId = 1;
        OrderInput orderInput = new OrderInput();
        orderInput.setUserId(Long.valueOf(userId));
        List<ProductInput> products = new ArrayList<>();
        ProductInput productInput1 = new ProductInput();
        productInput1.setId(1L);
        products.add(productInput1);
        orderInput.setProducts(products);
        orderInput.setUserId(1L);;

        // Mock API
        UserOutput userOutput = new UserOutput();
        userOutput.setId(userId);
        when(userApi.getUserById(anyLong())).thenReturn(Mono.just(userOutput));

        ProductOutput productOutput = new ProductOutput();
        productOutput.setId(userId);
        productOutput.setPrice(109.95);
        when(productApi.getProductById(anyLong())).thenReturn(Mono.just(productOutput));

        // Mock repository
        OrderModel savedOrder = new OrderModel();
        savedOrder.setUserId(1L);
        savedOrder.setPrecoTotal(BigDecimal.valueOf(109.95));
        savedOrder.setStatus(Status.PENDING);
        
        ItemModel savedItem = new ItemModel();
        
        savedItem.setId(1L);
        savedItem.setPrecoParcial(BigDecimal.valueOf(109.95));
        savedItem.setQuantidade(1L);
        
        savedOrder.getItens().add(savedItem);
        
        when(orderRepository.save(any(OrderModel.class))).thenReturn(savedOrder);

        // Service
        Optional<OrderModel> result = orderService.addOrder(orderInput);

        // Verify result
        assertTrue(result.isPresent());
        OrderModel orderModel = result.get();
        assertEquals(1L, orderModel.getUserId());
        assertEquals(Status.PENDING, orderModel.getStatus());
        assertEquals(BigDecimal.valueOf(109.95), orderModel.getPrecoTotal());
        assertEquals(1, orderModel.getItens().size());
        ItemModel item = orderModel.getItens().get(0);
        assertEquals(productInput1.getId(), item.getId());
        assertEquals(1L, item.getQuantidade());
        assertEquals(BigDecimal.valueOf(109.95), item.getPrecoParcial());

        // Verify API
        verify(userApi, times(1)).getUserById(anyLong());
        verify(productApi, times(1)).getProductById(anyLong());

        // Verify Repository
        verify(orderRepository, times(1)).save(any(OrderModel.class));
    }
    
    @Test
    public void testAddItemToOrder() {
    	// Mock data
    	int userId = 1;
    	UUID orderId = UUID.fromString("89147a55-f8e5-4cc5-91f0-08b1457e6a77");
        OrderUpdateInput orderInput = new OrderUpdateInput();
        orderInput.setId(orderId);
        List<ProductInput> items = new ArrayList<>();
        
        ProductInput productInput1 = new ProductInput();
        productInput1.setId(1L);
        
        items.add(productInput1);

        orderInput.setItems(items);
        
        // Mock API
        UserOutput userOutput = new UserOutput();
        userOutput.setId(userId);
        when(userApi.getUserById(anyLong())).thenReturn(Mono.just(userOutput));

        ProductOutput productOutput = new ProductOutput();
        productOutput.setId(userId);
        productOutput.setPrice(109.95);
        when(productApi.getProductById(anyLong())).thenReturn(Mono.just(productOutput));

        OrderModel orderModel = new OrderModel();
        orderModel.setId(orderId);
        Optional<OrderModel> order = Optional.of(orderModel);
        when(orderRepository.findById(orderId)).thenReturn(order);

        // Service
        Optional<OrderModel> result = orderService.addItemToOrder(orderInput);

        // Verify Results
        assertTrue(result.isPresent());
        OrderModel orderResult = result.get();
        assertEquals(UUID.fromString("89147a55-f8e5-4cc5-91f0-08b1457e6a77"), orderResult.getId());
        assertEquals(productInput1.getId(), orderResult.getItens().get(0).getId());

        // Verify Repository
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).save(orderModel);
    }


    @Test
    public void testAddStatusToOrder() {
        // Mock input data
        UUID orderId = UUID.fromString("89147a55-f8e5-4cc5-91f0-08b1457e6a77");
        StatusInput statusInput = new StatusInput();
        statusInput.setId(orderId);

        OrderModel orderModel = new OrderModel();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(orderModel));

        // Perform the service method
        Optional<OrderModel> result = orderService.addStatusToOrder(statusInput);

        // Verify the result
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(Status.CONCLUDED, result.get().getStatus());

        // Verify that repository save was called
        verify(orderRepository, times(1)).save(orderModel);
    }
    
}
