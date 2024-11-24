package com.example.msproveedorservice.controller;

import com.example.msproveedorservice.entity.Proveedor;
import com.example.msproveedorservice.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/proveedor")
public class ProveedorController {
    @Autowired
    private ProveedorService proveedorService;

    @GetMapping
    public ResponseEntity<List<Proveedor>> getAll() {
        return ResponseEntity.ok(proveedorService.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(proveedorService.findById(id).get());
    }

    @PostMapping
    public ResponseEntity<Proveedor> create(@RequestBody Proveedor Proveedor) {
        return ResponseEntity.ok(proveedorService.save(Proveedor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proveedor> update(@PathVariable Integer id,
                                          @RequestBody Proveedor Proveedor) {
        Proveedor.setId(id);
        return ResponseEntity.ok(proveedorService.save(Proveedor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<Proveedor>> delete(@PathVariable Integer id) {
        proveedorService.delete(id);
        return ResponseEntity.ok(proveedorService.list());
    }
}
