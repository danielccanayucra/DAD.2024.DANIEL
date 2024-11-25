package com.example.msadquisicione.feign;

import com.example.msadquisicione.dto.ProveedorDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-proveedor-service", path = "/proveedor")
public interface ProveedorFeign {
    @GetMapping("/{id}")
    @CircuitBreaker(name = "proveedorListByIdCB", fallbackMethod = "proveedorListById")
    ProveedorDto obtenerProveedor(@PathVariable("id") Integer id);
}
