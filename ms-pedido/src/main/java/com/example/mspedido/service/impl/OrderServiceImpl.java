package com.example.mspedido.service.impl;

import com.example.mspedido.dto.ClientDto;
import com.example.mspedido.dto.ProductDto;
import com.example.mspedido.entity.Order;
import com.example.mspedido.entity.OrderDetail;
import com.example.mspedido.feign.ClientFeign;
import com.example.mspedido.feign.ProductFeign;
import com.example.mspedido.repository.OrderRepository;
import com.example.mspedido.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductFeign productFeign;
    @Autowired
    private ClientFeign clientFeign;

    @Override
    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Override
    public Order save(Order order) {
        // 1. Verificar que el cliente existe
        ResponseEntity<ClientDto> clientResponse = clientFeign.getById(order.getClientId());
        if (!clientResponse.getStatusCode().is2xxSuccessful() || clientResponse.getBody() == null) {
            throw new RuntimeException("Cliente no encontrado con ID: " + order.getClientId());
        }
        order.setClientDto(clientResponse.getBody());

        // 2. Verificar los productos y actualizar el inventario
        Double totalPrice = 0.0;
        for (OrderDetail orderDetail : order.getOrderDetails()) {
            // Obtener el producto usando el productId
            ResponseEntity<ProductDto> productResponse = productFeign.getById(orderDetail.getProductId());
            if (!productResponse.getStatusCode().is2xxSuccessful() || productResponse.getBody() == null) {
                throw new RuntimeException("Producto no encontrado con ID: " + orderDetail.getProductId());
            }

            // Verificar si hay suficiente stock
            ProductDto productDto = productResponse.getBody();
            if (productDto.getStock() < orderDetail.getQuantity()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + productDto.getName());
            }

            // Actualizar el precio en el detalle del pedido
            orderDetail.setPrice(productDto.getPrecio());
            totalPrice += productDto.getPrecio() * orderDetail.getQuantity();

            // Reducir el stock del producto
            productFeign.reducirStock(orderDetail.getProductId(), orderDetail.getQuantity());
        }

        // Registrar el pedido
        order.setTotalPrice(totalPrice);  // Asignar el precio total del pedido
        order.setStatus("PENDING");  // El pedido inicia como pendiente hasta que se realice el pago
        Order savedOrder = orderRepository.save(order);

        return savedOrder;
    }

    @Override
    public Optional<Order> findById(Integer id) {
        Optional<Order> order = orderRepository.findById(id);

        if (order.isPresent()) {
            // Enriquecer con el cliente
            ResponseEntity<ClientDto> clientResponse = clientFeign.getById(order.get().getClientId());
            if (clientResponse.getStatusCode().is2xxSuccessful() && clientResponse.getBody() != null) {
                order.get().setClientDto(clientResponse.getBody());
            }

            // Enriquecer los detalles con los productos
            order.get().getOrderDetails().forEach(orderDetail -> {
                ResponseEntity<ProductDto> productResponse = productFeign.getById(orderDetail.getProductId());
                if (productResponse.getStatusCode().is2xxSuccessful() && productResponse.getBody() != null) {
                    orderDetail.setProductDto(productResponse.getBody());
                }
            });
        }

        return order;
    }

    @Override
    public void delete(Integer id) {
        // Validar si el pedido existe antes de eliminar
        Optional<Order> pedidoOpt = orderRepository.findById(id);
        if (!pedidoOpt.isPresent()) {
            throw new RuntimeException("Pedido no encontrado con ID: " + id);
        }

        Order pedido = pedidoOpt.get();

        // Restaurar el stock de los productos incluidos en el pedido
        for (OrderDetail orderDetail : pedido.getOrderDetails()) {
            ResponseEntity<ProductDto> productResponse = productFeign.incrementarStock(
                    orderDetail.getProductId(), orderDetail.getQuantity()
            );
            if (!productResponse.getStatusCode().is2xxSuccessful() || productResponse.getBody() == null) {
                throw new RuntimeException("Error al incrementar el stock del producto con ID: " + orderDetail.getProductId());
            }
        }
        orderRepository.deleteById(id);
    }

    @Override
    public Order update(Order order) {
        // Validar si el cliente existe antes de actualizar el pedido
        ResponseEntity<ClientDto> response = clientFeign.getById(order.getClientId());
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("Cliente no encontrado con ID: " + order.getClientId());
        }

        // Actualizar el pedido
        Order updatedOrder = orderRepository.save(order);

        // Enriquecer con información del cliente (opcional)
        updatedOrder.setClientDto(response.getBody());

        return updatedOrder;
    }
}
