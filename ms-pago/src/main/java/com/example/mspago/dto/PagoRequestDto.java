package com.example.mspago.dto;

import lombok.Data;

@Data
public class PagoRequestDto {
    private Integer pedidoId; // ID del pedido que se desea pagar
    private String metodoPago;
}
