package com.example.msadquisicione.repository;

import com.example.msadquisicione.entity.Adquisicion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdquisicionRepository extends JpaRepository<Adquisicion, Integer> {
    List<Adquisicion> findByProveedorId(Integer proveedorId);
}
