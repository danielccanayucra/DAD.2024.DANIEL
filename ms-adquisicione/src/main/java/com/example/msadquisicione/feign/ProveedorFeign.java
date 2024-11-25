package com.example.msadquisicione.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-proveedor-service", path = "/proveedor")
public interface ProveedorFeign {
    @GetMapping("/api/proveedores/{id}")
    Boolean verificarProveedor(@PathVariable("id") Integer proveedorId);
}
