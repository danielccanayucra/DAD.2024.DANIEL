package com.example.msclienteservice.repository;

import com.example.msclienteservice.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {
}
