package com.example.msadquisicione.feign;


import com.example.msadquisicione.dto.ProductoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "ms-catalogo-service", path = "/producto")
public interface ProductoFeign {
    @PostMapping("/api/productos/actualizarStock")
    void actualizarStock(@RequestBody ProductoDto productoDto);
}
