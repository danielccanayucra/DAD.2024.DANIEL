package com.example.msadquisicione.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoDto {
    private Integer id;
    private String name;
    private String description;
    private String code;
    private Double  precio;
    private Integer stock;
    private CategoryDto category;
}
