package com.example.mspedido.entity;

import com.example.mspedido.dto.ProductDto;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Double price;
    private Integer quantity;
    private Double amount;
    private Integer ProductId;
    @Transient
    private ProductDto productDto;
    public OrderDetail() {
        this.price = (double) 0;
        this.quantity = 0;
        this.amount = (double) 0;
    }
    // Método para calcular el amount basado en price y quantity
    public void calculateAmount() {
        this.amount = this.price * this.quantity;
    }
}

