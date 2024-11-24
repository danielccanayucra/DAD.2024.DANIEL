package com.example.mspedido.feign;

import com.example.mspedido.dto.ProductDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@FeignClient(name = "ms-catalogo-service", path = "/product")
public interface ProductFeign {
    @GetMapping("/{id}")
    @CircuitBreaker(name = "productListByIdCB", fallbackMethod = "productListById")
    public ResponseEntity<ProductDto> getById(@PathVariable Integer id);
    default ResponseEntity<ProductDto> productListById(Integer id, Exception e) {
        return ResponseEntity.ok(new ProductDto());
    }
    @PostMapping("/{id}/reduce-stock")
    ResponseEntity<ProductDto> reducirStock(@PathVariable Integer id, @RequestParam Integer stock);

    @PostMapping("/{id}/increase-stock")
    ResponseEntity<ProductDto> incrementarStock(@PathVariable Integer id, @RequestParam Integer stock);



}