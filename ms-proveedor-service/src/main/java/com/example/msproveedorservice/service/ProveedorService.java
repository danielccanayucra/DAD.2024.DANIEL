package com.example.msproveedorservice.service;

import com.example.msproveedorservice.entity.Proveedor;

import java.util.List;
import java.util.Optional;

public interface ProveedorService {

    List<Proveedor> list();

    Optional<Proveedor> findById(Integer id);

    Proveedor save(Proveedor proveedor);

    Proveedor update(Integer id, Proveedor proveedor);

    void delete(Integer id);
}
