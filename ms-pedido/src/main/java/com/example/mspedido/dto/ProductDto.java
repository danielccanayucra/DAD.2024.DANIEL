package com.example.mspedido.dto;

import lombok.Data;

@Data
public class ProductDto {
    private Integer id;
    private String name;
    private String description;
    private String code;
    private Double  precio;
    private Integer stock;
    private CategoryDto category;
}
