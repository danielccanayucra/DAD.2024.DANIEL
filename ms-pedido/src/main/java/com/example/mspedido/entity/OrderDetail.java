package com.example.mspedido.entity;

import com.example.mspedido.dto.ProductDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private Integer productId;

    @NotNull
    private Integer quantity;

    private Double price; // Calculado automáticamente
    private Double amount; // Calculado automáticamente

    @Transient
    private ProductDto productDto; // No lo envías en la solicitud

    public void calculateAmount() {
        this.amount = this.price * this.quantity;
    }
}


