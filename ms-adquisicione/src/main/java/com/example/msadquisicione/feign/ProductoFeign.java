package com.example.msadquisicione.feign;


import com.example.msadquisicione.dto.ProductoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


@FeignClient(name = "ms-catalogo-service", path = "/producto")
public interface ProductoFeign {
    @GetMapping("/productos/{id}/existe")
    Boolean verificarProducto(@PathVariable("id") Integer id);

    @PutMapping("/productos/{id}/actualizar-stock")
    void actualizarStock(@PathVariable("id") Integer id, @RequestParam("cantidad") Integer cantidad);
}
