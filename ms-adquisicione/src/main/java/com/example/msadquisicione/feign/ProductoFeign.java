package com.example.msadquisicione.feign;


import com.example.msadquisicione.dto.ProductoDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


@FeignClient(name = "ms-catalogo-service", path = "/producto")
public interface ProductoFeign {
    @GetMapping("/{id}/existe")
    @CircuitBreaker(name = "productListByIdCB", fallbackMethod = "productListById")
    Boolean verificarProducto(@PathVariable("id") Integer id);

    @PutMapping("/{id}/actualizar-stock")
    void actualizarStock(@PathVariable("id") Integer id, @RequestParam("cantidad") Integer cantidad);
}
