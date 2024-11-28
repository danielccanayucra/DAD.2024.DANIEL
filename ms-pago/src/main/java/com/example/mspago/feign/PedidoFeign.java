package com.example.mspago.feign;

import com.example.mspago.dto.PedidoDto; // Importa el DTO de Producto
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker; // Importa la anotación CircuitBreaker
import org.springframework.cloud.openfeign.FeignClient; // Importa la anotación FeignClient para crear clientes HTTP
import org.springframework.http.ResponseEntity; // Importa la clase ResponseEntity para manejar respuestas HTTP
import org.springframework.web.bind.annotation.GetMapping; // Importa la anotación para manejar solicitudes GET
import org.springframework.web.bind.annotation.PathVariable; // Importa la anotación para manejar variables de ruta
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ms-pedido-service", path = "/pedido")
public interface PedidoFeign {

    @CircuitBreaker(name = "pedidoListByIdCB", fallbackMethod = "pedidoListByIdCB")
    public ResponseEntity<PedidoDto> getById(@PathVariable Integer id);
    default ResponseEntity<PedidoDto> productListById(Integer id, Exception e) {
        return ResponseEntity.ok(new PedidoDto());
    }
    @GetMapping("/{id}")
    PedidoDto getPedidoById(@PathVariable("id") Integer id);

    @PutMapping("/{id}/status")
    void actualizarEstadoPedido(@PathVariable("id") Integer id, @RequestParam("status") String status);

}