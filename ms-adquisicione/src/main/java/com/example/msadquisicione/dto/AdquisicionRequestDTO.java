package com.example.msadquisicione.dto;

import lombok.Data;

@Data
public class AdquisicionRequestDTO {
    private Integer productoId;
    private Integer proveedorId;
    private Integer cantidad;
    private Double precioCompra;
    private Double precioVenta;
}
