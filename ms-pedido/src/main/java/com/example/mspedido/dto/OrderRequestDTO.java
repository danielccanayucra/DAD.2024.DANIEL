package com.example.mspedido.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDTO {
    @NotNull
    private String number;

    @NotNull
    private Integer clientId;

    @NotNull
    @Valid
    private List<OrderDetailRequestDTO> orderDetails;
}

