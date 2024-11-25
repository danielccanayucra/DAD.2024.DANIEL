package com.example.msadquisicione.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class Adquisicion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer proveedorId;
    private Integer productoId;
    private Integer cantidad;
    private Double precioCompra;
    private Double precioVenta;

    @CreationTimestamp
    private LocalDateTime fechaAdquisicion;
}
