package com.example.msproveedorservice.repository;

import com.example.msproveedorservice.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {
}
