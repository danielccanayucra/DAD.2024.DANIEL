package com.example.msadquisicione.controller;


import com.example.msadquisicione.dto.AdquisicionRequestDTO;
import com.example.msadquisicione.dto.AdquisicionResponseDTO;
import com.example.msadquisicione.service.AdquisicionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/adquisiciones")
public class AdquisicionController {

    @Autowired
    private AdquisicionService adquisicionService;

    @PostMapping
    public ResponseEntity<AdquisicionResponseDTO> registrarAdquisicion(@RequestBody AdquisicionRequestDTO request) {
        return ResponseEntity.ok(adquisicionService.registrarAdquisicion(request));
    }

    @GetMapping
    public ResponseEntity<List<AdquisicionResponseDTO>> listarAdquisiciones() {
        return ResponseEntity.ok(adquisicionService.listarAdquisiciones());
    }

    @GetMapping("/proveedor/{proveedorId}")
    public ResponseEntity<List<AdquisicionResponseDTO>> listarAdquisicionesPorProveedor(@PathVariable Integer proveedorId) {
        return ResponseEntity.ok(adquisicionService.listarAdquisicionesPorProveedor(proveedorId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAdquisicion(@PathVariable Integer id) {
        adquisicionService.eliminarAdquisicion(id);
        return ResponseEntity.noContent().build();
    }
}