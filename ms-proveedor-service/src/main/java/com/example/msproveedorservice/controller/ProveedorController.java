package com.example.msproveedorservice.controller;

import com.example.msproveedorservice.entity.Proveedor;
import com.example.msproveedorservice.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/proveedor")
public class ProveedorController {
    @Autowired
    private ProveedorService proveedorService; // Inyección del servicio de proveedores

    @GetMapping // Maneja las solicitudes GET a "/proveedor"
    public ResponseEntity<List<Proveedor>> list() {
        List<Proveedor> proveedores = proveedorService.list(); // Obtiene la lista de proveedores
        return ResponseEntity.ok(proveedores); // Retorna la lista en una respuesta 200 OK
    }

    @GetMapping("/{id}") // Maneja las solicitudes GET a "/proveedor/{id}"
    public ResponseEntity<Proveedor> listById(@PathVariable Integer id) {
        Optional<Proveedor> proveedor = proveedorService.findById(id); // Busca el proveedor por ID
        return proveedor.map(ResponseEntity::ok) // Retorna 200 OK si se encuentra
                .orElseGet(() -> ResponseEntity.notFound().build()); // Retorna 404 Not Found si no se encuentra
    }

    @PostMapping // Maneja las solicitudes POST a "/proveedor"
    public ResponseEntity<Proveedor> save(@RequestBody Proveedor proveedor) {
        Proveedor savedProveedor = proveedorService.save(proveedor); // Guarda el proveedor
        return ResponseEntity.ok(savedProveedor); // Retorna el proveedor guardado
    }

    @PutMapping // Maneja las solicitudes PUT a "/proveedor"
    public ResponseEntity<Proveedor> update(@RequestBody Proveedor proveedor) {
        Proveedor updatedProveedor = proveedorService.update(proveedor); // Actualiza el proveedor
        return ResponseEntity.ok(updatedProveedor); // Retorna el proveedor actualizado
    }


    @DeleteMapping("/{id}") // Maneja las solicitudes DELETE a "/proveedor/{id}"
    public ResponseEntity<String> deleteById(@PathVariable Integer id) {
        proveedorService.deleteById(id); // Elimina el proveedor por ID
        return ResponseEntity.ok("Eliminación Correcta"); // Retorna un mensaje de éxito
    }
}
