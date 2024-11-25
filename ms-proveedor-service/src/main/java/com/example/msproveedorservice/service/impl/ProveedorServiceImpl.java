package com.example.msproveedorservice.service.impl;

import com.example.msproveedorservice.entity.Proveedor;
import com.example.msproveedorservice.repository.ProveedorRepository;
import com.example.msproveedorservice.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProveedorServiceImpl implements ProveedorService {
    @Autowired
    private ProveedorRepository proveedorRepository;

    @Override
    public List<Proveedor> list() {
        return proveedorRepository.findAll();
    }

    @Override
    public Optional<Proveedor> findById(Integer id) {
        return proveedorRepository.findById(id);
    }

    @Override
    public Optional<Proveedor> listarPorId(Integer id) {
        return proveedorRepository.findById(id);
    }

    @Override
    public Proveedor save(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    @Override
    public Proveedor update(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    @Override
    public void deleteById(Integer id) {
        proveedorRepository.deleteById(id);
    }
}