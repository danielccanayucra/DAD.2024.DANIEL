package com.example.ms_inventario.controller;

import com.example.ms_inventario.entity.Inventory;
import com.example.ms_inventario.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<List<Inventory>> getAll() {
        return ResponseEntity.ok(inventoryService.list());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Inventory>> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(inventoryService.findById(id));
    }
    @PostMapping
    public ResponseEntity<Inventory> create(@RequestBody Inventory inventory) {
        return ResponseEntity.ok(inventoryService.save(inventory));
    }
    @PutMapping("/{id}")
    public ResponseEntity<Inventory> update(@PathVariable Integer id,
                                           @RequestBody Inventory inventory) {
        inventory.setId(id);
        return ResponseEntity.ok(inventoryService.save(inventory));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<List<Inventory>> delete(@PathVariable Integer id) {
        inventoryService.delete(id);
        return ResponseEntity.ok(inventoryService.list());
    }
}
