package com.example.mspedido.dto;

import lombok.Data;

@Data
public class OrderDetailResponseDTO {
    private Integer id;
    private Integer productId;
    private Integer quantity;
    private Double price;
    private Double amount;
    private ProductDto productDto;
}
