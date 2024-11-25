package com.example.msinventario.dto;

import lombok.Data; // Importa la anotación de Lombok para generar automáticamente getters y setters

@Data // Genera automáticamente los métodos getters, setters, toString, equals y hashCode
public class ProveedorDto {
    private Integer id;

    private String nombre;

    private String ruc;

    private String direccion;

    private String telefono;
}
