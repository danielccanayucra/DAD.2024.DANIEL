package com.example.mspedido.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderResponseDTO {
    private Integer id;
    private String number;
    private Integer clientId;
    private ClientDto clientDto;
    private List<OrderDetailResponseDTO> orderDetails;
    private Double totalPrice;
    private String status;
}
