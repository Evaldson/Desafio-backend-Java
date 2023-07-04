package com.challenge.backend.model.input;

import java.util.UUID;

import com.challenge.backend.enuns.Status;

import lombok.Data;

@Data
public class StatusInput {

    private UUID id;
    private Long userId;
    private Status status;
}
