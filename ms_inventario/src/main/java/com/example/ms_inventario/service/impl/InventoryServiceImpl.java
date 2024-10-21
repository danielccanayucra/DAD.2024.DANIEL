package com.example.ms_inventario.service.impl;

import com.example.ms_inventario.entity.Inventory;
import com.example.ms_inventario.repository.InventoryRepository;
import com.example.ms_inventario.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;

    @Override
    public List<Inventory> list() {
        return inventoryRepository.findAll();
    }

    @Override
    public Optional<Inventory> findById(Integer id) {
        return inventoryRepository.findById(id);
    }

    @Override
    public Inventory save(Inventory category) {
        return inventoryRepository.save(category);
    }

    @Override
    public Inventory update(Inventory category) {
        return inventoryRepository.save(category);
    }

    @Override
    public void delete(Integer id) {
        inventoryRepository.deleteById(id);
    }
}
