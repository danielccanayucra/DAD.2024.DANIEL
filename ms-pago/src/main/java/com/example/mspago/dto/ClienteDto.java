package com.example.mspago.dto;

import lombok.Data;

@Data
public class ClienteDto {
    private Integer id;
    private String name;
    private String apellido;
    private String telefono;
    private String document;
}
