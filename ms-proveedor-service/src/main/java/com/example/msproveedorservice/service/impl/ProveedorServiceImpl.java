package com.example.msproveedorservice.service.impl;

import com.example.msproveedorservice.entity.Proveedor;
import com.example.msproveedorservice.repository.ProveedorRepository;
import com.example.msproveedorservice.service.ProveedorService;
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
    public Proveedor save(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    @Override
    public Proveedor update(Integer id,Proveedor proveedor) {
        Optional<Proveedor> proveedorExistente = proveedorRepository.findById(id);

        if (proveedorExistente.isPresent()) {
            Proveedor existente = proveedorExistente.get();

            // Actualiza los campos del proveedor existente
            existente.setNombre(proveedor.getNombre());
            existente.setContacto(proveedor.getContacto());
            existente.setDireccion(proveedor.getDireccion());
            existente.setTelefono(proveedor.getTelefono());
            existente.setEmail(proveedor.getEmail());

            // Guarda y retorna el proveedor actualizado
            return proveedorRepository.save(existente);
        } else {
            // Si no se encuentra el proveedor, lanza una excepción
            throw new RuntimeException("Proveedor no encontrado con ID: " + id);
        }
    }

    @Override
    public void delete(Integer id) {
        proveedorRepository.deleteById(id);
    }
}