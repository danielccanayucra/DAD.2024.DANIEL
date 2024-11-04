package com.example.msclienteservice.service.impl;

import com.example.msclienteservice.entity.Proveedor;
import com.example.msclienteservice.repository.ProveedorRepository;
import com.example.msclienteservice.service.ProveedorService;
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
    public Proveedor save(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    @Override
    public Proveedor update(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    @Override
    public void delete(Integer id) {
        proveedorRepository.deleteById(id);
    }
}
