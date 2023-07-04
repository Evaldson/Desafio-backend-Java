package com.challenge.backend.service;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.challenge.backend.model.output.ProductOutput;

import reactor.core.publisher.Mono;

@Component
public class ProductApiClient {
    private final WebClient webClient;

    public ProductApiClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://fakestoreapi.com/products").build();
    }

    public Mono<List<ProductOutput>> getAllProducts() {
        return webClient.get()
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ProductOutput>>() {})
                .onErrorResume(error -> {
                    return Mono.error(new Exception("Erro ao obter os produtos", error));
                });
    }

    public Mono<ProductOutput> getProductById(Long id) {
        return webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(ProductOutput.class)
                .onErrorResume(error -> {
                	 return Mono.error(new Exception("Erro ao obter o produto", error));
                });
    }
}
