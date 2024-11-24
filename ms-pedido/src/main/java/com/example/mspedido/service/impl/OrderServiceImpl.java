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
        // Validar si el cliente existe antes de guardar el pedido
        ResponseEntity<ClientDto> response = clientFeign.getById(order.getClientId());
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("Cliente no encontrado con ID: " + order.getClientId());
        }

        // Reducir el stock de cada producto incluido en el pedido
        for (OrderDetail orderDetail : order.getOrderDetails()) {
            ResponseEntity<ProductDto> productResponse = productFeign.reducirStock(
                    orderDetail.getProductId(), orderDetail.getQuantity()
            );
            if (!productResponse.getStatusCode().is2xxSuccessful() || productResponse.getBody() == null) {
                throw new RuntimeException("Error al reducir el stock del producto con ID: " + orderDetail.getProductId());
            }
        }

        // Si el cliente existe, guardamos el pedido
        Order savedOrder = orderRepository.save(order);

        // Enriquecer el pedido con información del cliente (opcional)
        savedOrder.setClientDto(response.getBody());

        return savedOrder;
    }

    @Override
    public Optional<Order> findById(Integer id) {
        Optional<Order> order = orderRepository.findById(id);

        if (order.isPresent()) {
            // Llamar al servicio de clientes y setear el DTO del cliente
            ResponseEntity<ClientDto> clientResponse = clientFeign.getById(order.get().getClientId());
            if (clientResponse.getStatusCode().is2xxSuccessful() && clientResponse.getBody() != null) {
                order.get().setClientDto(clientResponse.getBody());
            }

            // Llamar al servicio de productos y setear los DTOs en cada detalle del pedido
            order.get().getOrderDetails().forEach(orderDetail -> {
                ResponseEntity<ProductDto> productResponse = productFeign.getById(orderDetail.getProductId());
                if (productResponse.getStatusCode().is2xxSuccessful() && productResponse.getBody() != null) {
                    orderDetail.setProductDto(productResponse.getBody());
                    orderDetail.calculateAmount();
                }
            });
        } else {
            throw new RuntimeException("Pedido no encontrado con ID: " + id);
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
