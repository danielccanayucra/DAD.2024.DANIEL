package com.example.mspedido.entity;

import com.example.mspedido.dto.ClientDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String number;         // Número del pedido
    private Integer clientId;      // ID del cliente

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private List<OrderDetail> orderDetails; // Detalles del pedido (productos)

    @Transient
    private ClientDto clientDto; // Cliente asociado (DTO para cliente)

    private Double totalPrice;  // Precio total del pedido
    private String status;      // Estado del pedido (PENDING, PAID, etc.)

    // Constructor por defecto
    public Order() {
        this.status = "PENDING";  // Por defecto, el estado es "PENDING"
        this.totalPrice = 0.0;    // Precio total por defecto es 0
    }

    // Método para calcular el precio total
    public void calculateTotalPrice() {
        double total = 0.0;
        for (OrderDetail orderDetail : orderDetails) {
            total += orderDetail.getPrice() * orderDetail.getAmount();
        }
        this.totalPrice = total;
    }
}
