package com.example.msadquisicione.dto;

import lombok.Data;

@Data
public class AdquisicionResponseDTO {
    private Integer id;
    private Integer productoId;
    private String nombreProducto;
    private Integer proveedorId;
    private Integer cantidad;
    private Double precioCompra;
    private Double precioVenta;
    private String fechaAdquisicion;
}
