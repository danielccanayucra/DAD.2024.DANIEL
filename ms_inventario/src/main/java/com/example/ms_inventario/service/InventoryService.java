package com.example.ms_inventario.service;

import com.example.ms_inventario.entity.Inventory;

import java.util.List;
import java.util.Optional;

public interface InventoryService {
    List<Inventory> list();
    Optional<Inventory> findById(Integer id);
    Inventory save(Inventory inventory);
    Inventory update(Inventory inventory);
    void delete(Integer id);
}