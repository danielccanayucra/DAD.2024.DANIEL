package com.example.msinventario.Entity;

import com.example.msinventario.dto.ProductoDto; // Importa el DTO de Producto
import jakarta.persistence.*; // Importa anotaciones de JPA
import lombok.Data; // Importa la anotación de Lombok para generar automáticamente getters y setters

import java.util.Date; // Importa la clase Date para manejar fechas

@Entity
@Data
public class InventarioDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String categoria;

    private Double precio;

    private String estadoProducto;

    private String marca;

    private Integer codigo;

    private Date fechaRecibido;

    private Integer productoId;

    @Transient
    private ProductoDto productoDto;


    public InventarioDetalle() {
        this.precio = 0.0;
    }
}
