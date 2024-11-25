package com.example.msadquisicione.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class AdquisicionDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Adquisicion adquisicion;

    private String descripcionProducto;
    private String codigoProducto;
}
