package com.example.mspedido.entity;

import com.example.mspedido.dto.ClientDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String number;

    @NotNull
    private Integer clientId;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    @Valid
    private List<OrderDetail> orderDetails;

    @Transient
    private ClientDto clientDto; // No lo envías en la solicitud

    private Double totalPrice; // Calculado automáticamente
    private String status; // Asignado automáticamente
}

