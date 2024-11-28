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

    /**
     * Recupera un pedido por ID con Circuit Breaker.
     */
    @CircuitBreaker(name = "pedidoListByIdCB", fallbackMethod = "pedidoListByIdFallback")
    @GetMapping("/{id}")
    ResponseEntity<PedidoDto> getPedidoById(@PathVariable("id") Integer id);

    /**
     * Actualiza el estado de un pedido.
     */
    @PutMapping("/{id}/status")
    void actualizarEstadoPedido(@PathVariable("id") Integer id, @RequestParam("status") String status);

    /**
     * Método de respaldo (fallback) para `getPedidoById`.
     */
    default ResponseEntity<PedidoDto> pedidoListByIdFallback(Integer id, Throwable e) {
        PedidoDto fallbackPedido = new PedidoDto();
        fallbackPedido.setId(id);
        fallbackPedido.setStatus("Información no disponible - Fallback");
        return ResponseEntity.ok(fallbackPedido);
    }
}