package com.challenge.backend.service;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.challenge.backend.model.output.UserOutput;

import reactor.core.publisher.Mono;

@Component
public class UserApiClient {
    private final WebClient webClient;

    public UserApiClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://fakestoreapi.com/users").build();
    }

    public Mono<List<UserOutput>> getAllUsers() {
        return webClient.get()
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<UserOutput>>() {})
                .onErrorResume(error -> {
                	 return Mono.error(new Exception("Erro ao obter os usuários", error));
                });
    }

    public Mono<UserOutput> getUserById(Long id) {
        return webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(UserOutput.class)
                .onErrorResume(error -> {
                	 return Mono.error(new Exception("Erro ao obter o usuário", error));
                });
    }
}

