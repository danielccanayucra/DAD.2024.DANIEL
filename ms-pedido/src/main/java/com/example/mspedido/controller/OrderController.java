package com.example.mspedido.controller;

import com.example.mspedido.dto.OrderDetailResponseDTO;
import com.example.mspedido.dto.OrderRequestDTO;
import com.example.mspedido.dto.OrderResponseDTO;
import com.example.mspedido.dto.ProductDto;
import com.example.mspedido.entity.Order;
import com.example.mspedido.entity.OrderDetail;
import com.example.mspedido.exception.ResourceNotFoundException;
import com.example.mspedido.feign.ProductFeign;
import com.example.mspedido.service.OrderService;
import com.example.mspedido.service.impl.OrderServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
public class OrderController {
    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductFeign productFeign;

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
        log.info("Producto  para OrderDetail: {}", orderDetails);

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
    public ResponseEntity<OrderResponseDTO> update(@PathVariable Integer id, @RequestBody @Valid OrderRequestDTO orderRequestDTO) {
        // Recuperar el pedido existente desde la base de datos
        Optional<Order> optionalOrder = orderService.findById(id);
        if (optionalOrder.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // Pedido no encontrado
        }

        Order order = optionalOrder.get();  // Pedido existente
        order.setNumber(orderRequestDTO.getNumber());
        order.setClientId(orderRequestDTO.getClientId());

        // Actualizar los detalles del pedido
        List<OrderDetail> orderDetails = orderRequestDTO.getOrderDetails().stream().map(orderDetailRequest -> {
            // Aquí se hace la consulta al servicio para obtener el producto
            ResponseEntity<ProductDto> productResponse = productFeign.getById(orderDetailRequest.getProductId());
            if (!productResponse.getStatusCode().is2xxSuccessful() || productResponse.getBody() == null) {
                throw new RuntimeException("Producto no encontrado con ID: " + orderDetailRequest.getProductId());
            }
            ProductDto productDto = productResponse.getBody();


            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProductId(orderDetailRequest.getProductId());
            orderDetail.setQuantity(orderDetailRequest.getQuantity());
            orderDetail.setProductDto(productDto);


             // Asignar el productDto
            orderDetail.setPrice(productDto.getPrecio());  // Asignar el precio del producto
            log.info("Producto  para order: {}", orderDetail);

            return orderDetail;
        }).collect(Collectors.toList());
        log.info("Producto  para OrderDetails: {}", orderDetails);
        order.setOrderDetails(orderDetails);

        log.info("Producto obtenido para order: {}", order);
        // Actualizar el pedido en la base de datos
        Order updatedOrder = orderService.update(order);

        for (OrderDetail orderDetail : updatedOrder.getOrderDetails()) {
            ResponseEntity<ProductDto> productResponse = productFeign.getById(orderDetail.getProductId());
            if (productResponse.getStatusCode().is2xxSuccessful() && productResponse.getBody() != null) {
                orderDetail.setProductDto(productResponse.getBody());
            }
        }

        // Mapear la entidad guardada al DTO de respuesta
        OrderResponseDTO responseDTO = new OrderResponseDTO();
        responseDTO.setId(updatedOrder.getId());
        responseDTO.setNumber(updatedOrder.getNumber());
        responseDTO.setClientId(updatedOrder.getClientId());
        responseDTO.setClientDto(updatedOrder.getClientDto());

        responseDTO.setOrderDetails(updatedOrder.getOrderDetails().stream().map(detail -> {
            OrderDetailResponseDTO detailDTO = new OrderDetailResponseDTO();
            detailDTO.setId(detail.getId());
            detailDTO.setProductId(detail.getProductId());
            detailDTO.setQuantity(detail.getQuantity());
            detailDTO.setPrice(detail.getPrice());
            detailDTO.setAmount(detail.getAmount());
            detailDTO.setProductDto(detail.getProductDto());  // Asegurarse de que el productDto no sea null

            return detailDTO;

        }).collect(Collectors.toList()));

        responseDTO.setTotalPrice(updatedOrder.getTotalPrice());
        responseDTO.setStatus(updatedOrder.getStatus());
        return ResponseEntity.ok(responseDTO);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<List<Order>> delete(@PathVariable Integer id) {
        orderService.delete(id);
        return ResponseEntity.ok(orderService.list());
    }
}
