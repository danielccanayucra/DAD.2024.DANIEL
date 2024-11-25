package com.example.msinventario.Entity;

import com.example.msinventario.dto.ProveedorDto; // Importa el DTO de Proveedor
import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // Para ignorar propiedades durante la serialización
import jakarta.persistence.*; // Importa anotaciones de JPA
import lombok.Data; // Importa la anotación de Lombok para generar automáticamente getters y setters

import java.util.List; // Importa la clase List para manejar listas

@Entity
@Data
public class Inventario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        private String nombre;

        private String descripcion;

        private Double stock;

        private String marca;

        private Integer codigo;

        private Integer proveedorId;


        public Inventario() {
            this.stock = 0.0;
        }

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "inventario_id")
    private List<InventarioDetalle> inventarioDetalle;

    @Transient
    private ProveedorDto proveedorDto;
}
