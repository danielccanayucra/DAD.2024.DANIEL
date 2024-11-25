package com.example.msproveedorservice.service;

import com.example.msproveedorservice.entity.Proveedor;

import java.util.List;
import java.util.Optional;

public interface ProveedorService {

    public List<Proveedor> list();

    public Optional<Proveedor> findById(Integer id);
    Optional<Proveedor> listarPorId(Integer id);

    public Proveedor save(Proveedor proveedor);

    public Proveedor update(Proveedor proveedor);

    public void deleteById(Integer id);
}
