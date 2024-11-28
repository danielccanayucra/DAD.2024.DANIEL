package com.example.mspago.Controller;

import com.example.mspago.dto.PagoRequestDto;
import com.example.mspago.dto.PagoResponseDto;
import com.example.mspago.Service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @PostMapping
    public PagoResponseDto realizarPago(@RequestBody PagoRequestDto pagoRequest) {
        return pagoService.procesarPago(pagoRequest);
    }
}
