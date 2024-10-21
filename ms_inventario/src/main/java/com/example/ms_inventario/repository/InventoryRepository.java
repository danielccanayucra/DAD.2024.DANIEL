package com.example.ms_inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findByCode(String code);
}
