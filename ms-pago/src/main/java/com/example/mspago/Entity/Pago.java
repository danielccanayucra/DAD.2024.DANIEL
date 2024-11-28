package com.example.mspago.Entity;

import com.example.mspago.dto.ClienteDto; // Importa el DTO de Cliente
import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // Para ignorar propiedades durante la serialización
import jakarta.persistence.*; // Importa anotaciones de JPA
import lombok.Data; // Importa la anotación de Lombok para generar automáticamente getters y setters

import java.time.LocalDateTime;
import java.util.List; // Importa la clase List para manejar listas

@Entity // Indica que esta clase es una entidad JPA
@Data // Genera automáticamente los métodos getters, setters, toString, equals y hashCode
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    private Integer pedidoId;

    private Double monto;

    private String estado; // COMPLETADO, FALLIDO, etc.

    private String metodoPago; // Ejemplo: TARJETA, PAYPAL, EFECTIVO

    @Transient // Indica que este campo no se persiste en la base de datos
    private ClienteDto clienteDto; // Objeto Cliente, no persistido en la base de datos
}
