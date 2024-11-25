package com.example.msadquisicione.dto;

import lombok.Data;

@Data
public class AdquisicionDto {
    private Integer proveedorId;
    private Integer productoId;
    private String nombreProducto;
    private String descripcionProducto;
    private String codigoProducto;
    private Integer cantidad;
    private Double precioCompra;
    private Double precioVenta;
    private Integer categoriaId;
}
