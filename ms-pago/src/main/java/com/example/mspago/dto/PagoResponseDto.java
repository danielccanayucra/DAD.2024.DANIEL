package com.example.mspago.dto;

import lombok.Data;

@Data
public class PagoResponseDto {
    private Integer pagoId; // ID del pago registrado
    private String status; // Estado del pago (ejemplo: "COMPLETADO", "FALLIDO")
    private String mensaje; // Mensaje adicional para el cliente
    private Double monto; // Monto del pago realizado
    private Integer pedidoId; // ID del pedido relacionado con el pago

    // Constructores
    public PagoResponseDto() {
    }

    public PagoResponseDto(Integer pagoId, String status, String mensaje, Double monto, Integer pedidoId) {
        this.pagoId = pagoId;
        this.status = status;
        this.mensaje = mensaje;
        this.monto = monto;
        this.pedidoId = pedidoId;
    }
}
