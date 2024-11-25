package com.example.mspedido.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderDetailRequestDTO {
    @NotNull
    private Integer productId;

    @NotNull
    private Integer quantity;
}

