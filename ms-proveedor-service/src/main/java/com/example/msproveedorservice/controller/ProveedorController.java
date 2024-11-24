package com.example.msproveedorservice.controller;

import com.example.msproveedorservice.entity.Proveedor;
import com.example.msproveedorservice.service.ProveedorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/proveedor")
public class ProveedorController {
    private final ProveedorService proveedorService;

    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @GetMapping
    public List<Proveedor> listarProveedores() {
        return proveedorService.list();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(proveedorService.findById(id).get());
    }

    @PostMapping
    public Proveedor create(@RequestBody Proveedor proveedor) {
        return proveedorService.save(proveedor);
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
