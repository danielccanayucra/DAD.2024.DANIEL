package com.example.mspedido.controller;

import com.example.mspedido.dto.OrderDetailResponseDTO;
import com.example.mspedido.dto.OrderRequestDTO;
import com.example.mspedido.dto.OrderResponseDTO;
import com.example.mspedido.entity.Order;
import com.example.mspedido.entity.OrderDetail;
import com.example.mspedido.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<List<Order>> listOrders() {
        List<Order> orders = orderService.list();
        return ResponseEntity.ok(orders);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Integer id) {
        Optional<Order> order = orderService.findById(id);
        if (order.isPresent()) {
            return ResponseEntity.ok(order.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> create(@RequestBody @Valid OrderRequestDTO orderRequestDTO) {
        // Mapear el DTO de entrada a la entidad Order
        Order order = new Order();
        order.setNumber(orderRequestDTO.getNumber());
        order.setClientId(orderRequestDTO.getClientId());

        List<OrderDetail> orderDetails = orderRequestDTO.getOrderDetails().stream().map(orderDetailRequest -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProductId(orderDetailRequest.getProductId());
            orderDetail.setQuantity(orderDetailRequest.getQuantity());
            return orderDetail;
        }).collect(Collectors.toList());

        order.setOrderDetails(orderDetails);

        // Guardar el pedido
        Order savedOrder = orderService.save(order);

        // Mapear la entidad guardada al DTO de respuesta
        OrderResponseDTO responseDTO = new OrderResponseDTO();
        responseDTO.setId(savedOrder.getId());
        responseDTO.setNumber(savedOrder.getNumber());
        responseDTO.setClientId(savedOrder.getClientId());
        responseDTO.setClientDto(savedOrder.getClientDto());
        responseDTO.setOrderDetails(savedOrder.getOrderDetails().stream().map(detail -> {
            OrderDetailResponseDTO detailDTO = new OrderDetailResponseDTO();
            detailDTO.setId(detail.getId());
            detailDTO.setProductId(detail.getProductId());
            detailDTO.setQuantity(detail.getQuantity());
            detailDTO.setPrice(detail.getPrice());
            detailDTO.setAmount(detail.getAmount());
            detailDTO.setProductDto(detail.getProductDto());
            return detailDTO;
        }).collect(Collectors.toList()));
        responseDTO.setTotalPrice(savedOrder.getTotalPrice());
        responseDTO.setStatus(savedOrder.getStatus());

        return ResponseEntity.ok(responseDTO);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Order> update(@PathVariable Integer id, @RequestBody Order order) {
        order.setId(id);  // Establecer el ID del pedido recibido en la URL
        return ResponseEntity.ok(orderService.update(order));  // Llamar al método update() en lugar de save()
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<List<Order>> delete(@PathVariable Integer id) {
        orderService.delete(id);
        return ResponseEntity.ok(orderService.list());
    }
}
