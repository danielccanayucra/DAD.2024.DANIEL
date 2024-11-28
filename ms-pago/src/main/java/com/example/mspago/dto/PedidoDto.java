package com.example.mspago.dto;

import lombok.Data;

@Data
public class PedidoDto {
    private Integer id;
    private Integer client_id;
    private String number;
    private String status;
    private Double  total_price;

}
