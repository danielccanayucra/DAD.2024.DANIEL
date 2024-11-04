package com.example.msclienteservice.service;

import com.example.msclienteservice.entity.Cliente;
import com.example.msclienteservice.entity.Proveedor;

import java.util.List;
import java.util.Optional;


public interface ProveedorService {
    List<Proveedor> list();

    Optional<Proveedor> findById(Integer id);

    Proveedor save(Proveedor proveedor);

    Proveedor update(Proveedor proveedor);

    void delete(Integer id);
}

