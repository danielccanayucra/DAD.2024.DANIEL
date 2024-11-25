package com.example.msadquisicione.controller;

import com.example.msadquisicione.dto.AdquisicionDto;
import com.example.msadquisicione.service.AdquisicionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/adquisiciones")
public class AdquisicionController {

    @Autowired
    private AdquisicionService adquisicionService;

    @PostMapping
    public ResponseEntity<?> registrarAdquisicion(@RequestBody AdquisicionDto adquisicionDto) {
        return ResponseEntity.ok(adquisicionService.registrarAdquisicion(adquisicionDto));
    }
}