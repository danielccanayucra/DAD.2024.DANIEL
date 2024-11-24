package com.example.mspedido.service.impl;

import com.example.mspedido.dto.ClientDto;
import com.example.mspedido.dto.ProductDto;
import com.example.mspedido.entity.Order;
import com.example.mspedido.entity.OrderDetail;
import com.example.mspedido.exception.ResourceNotFoundException;
import com.example.mspedido.feign.ClientFeign;
import com.example.mspedido.feign.ProductFeign;
import com.example.mspedido.repository.OrderRepository;
import com.example.mspedido.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);


    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductFeign productFeign;
    @Autowired
    private ClientFeign clientFeign;

    @Override
    public List<Order> list() {
        List<Order> orders = orderRepository.findAll();

        // Para cada pedido, obtener los detalles completos del cliente y los productos
        for (Order order : orders) {
            // Obtener los detalles del cliente
            ResponseEntity<ClientDto> clientResponse = clientFeign.getById(order.getClientId());
            if (clientResponse.getStatusCode().is2xxSuccessful() && clientResponse.getBody() != null) {
                order.setClientDto(clientResponse.getBody());
            }

            // Para cada detalle del pedido, obtener los detalles completos del producto
            for (OrderDetail orderDetail : order.getOrderDetails()) {
                Integer productId = orderDetail.getProductId();
                ResponseEntity<ProductDto> productResponse = productFeign.getById(productId);
                if (productResponse.getStatusCode().is2xxSuccessful() && productResponse.getBody() != null) {
                    ProductDto productDto = productResponse.getBody();
                    orderDetail.setProductDto(productDto); // Asignar el ProductDto completo al detalle
                }
            }
        }

        // Retornar la lista de pedidos con los detalles completos del cliente y los productos
        return orders;
    }

    @Override
    public Order save(Order order) {
        // Verificar si el cliente existe
        ResponseEntity<ClientDto> clientResponse = clientFeign.getById(order.getClientId());
        if (!clientResponse.getStatusCode().is2xxSuccessful() || clientResponse.getBody() == null) {
            throw new ResourceNotFoundException("Cliente no encontrado con ID: " + order.getClientId());
        }

        ClientDto clientDto = clientResponse.getBody();
        order.setClientDto(clientDto);  // Establecer los detalles del cliente en el pedido

        // Verificar los productos solo usando el productId
        Double totalPrice = 0.0;
        for (OrderDetail orderDetail : order.getOrderDetails()) {
            // Usar el productId para obtener los detalles completos del producto
            Integer productId = orderDetail.getProductId();
            ResponseEntity<ProductDto> productResponse = productFeign.getById(productId);
            if (!productResponse.getStatusCode().is2xxSuccessful() || productResponse.getBody() == null) {
                throw new RuntimeException("Producto no encontrado con ID: " + productId);
            }

            ProductDto productDto = productResponse.getBody();
            if (productDto.getStock() < orderDetail.getQuantity()) {
                throw new RuntimeException("Stock insuficiente para el producto con ID: " + productId);
            }

            // Establecer el precio en el detalle del pedido y calcular el monto
            orderDetail.setPrice(productDto.getPrecio());
            orderDetail.setProductDto(productDto); // Asignar el ProductDto completo al detalle
            orderDetail.calculateAmount(); // Calcular el amount basado en quantity y price
            totalPrice += orderDetail.getAmount();

            // Reducir el stock del producto
            productFeign.reducirStock(productId, orderDetail.getQuantity());
        }

        // Registrar el pedido
        order.setTotalPrice(totalPrice);  // Asignar el precio total del pedido
        order.setStatus("PENDING");  // El pedido inicia como pendiente hasta que se realice el pago

        // Guardar el pedido
        Order savedOrder = orderRepository.save(order);

        // Retornar el pedido guardado (con cliente y productos completos)
        return savedOrder;
    }




    @Override
    public Optional<Order> findById(Integer id) {
        // Obtener el pedido por ID
        Optional<Order> orderOptional = orderRepository.findById(id);

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();

            // Obtener el detalle del cliente
            ResponseEntity<ClientDto> clientResponse = clientFeign.getById(order.getClientId());
            if (clientResponse.getStatusCode().is2xxSuccessful() && clientResponse.getBody() != null) {
                order.setClientDto(clientResponse.getBody());
            }

            // Obtener los detalles del producto para cada OrderDetail
            for (OrderDetail orderDetail : order.getOrderDetails()) {
                Integer productId = orderDetail.getProductId();
                ResponseEntity<ProductDto> productResponse = productFeign.getById(productId);
                if (productResponse.getStatusCode().is2xxSuccessful() && productResponse.getBody() != null) {
                    ProductDto productDto = productResponse.getBody();
                    orderDetail.setProductDto(productDto); // Asignar el ProductDto completo al detalle
                }
            }

            return Optional.of(order);
        }

        return Optional.empty();
    }


    @Override
    public void delete(Integer id) {
        // Validar si el pedido existe antes de eliminar
        Order pedido = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + id));

        // Restaurar el stock de los productos incluidos en el pedido
        for (OrderDetail orderDetail : pedido.getOrderDetails()) {
            try {
                ResponseEntity<ProductDto> productResponse = productFeign.incrementarStock(
                        orderDetail.getProductId(), orderDetail.getQuantity()
                );
                if (!productResponse.getStatusCode().is2xxSuccessful() || productResponse.getBody() == null) {
                    throw new RuntimeException("Error al incrementar el stock del producto con ID: " + orderDetail.getProductId());
                }
            } catch (Exception e) {
                throw new RuntimeException("Error al llamar al servicio de productos: " + e.getMessage(), e);
            }
        }
        orderRepository.deleteById(id);
    }

    @Override
    public Order update(Order order) {
        // Validar si el cliente existe antes de actualizar el pedido
        ResponseEntity<ClientDto> response = clientFeign.getById(order.getClientId());
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ResourceNotFoundException("Cliente no encontrado con ID: " + order.getClientId());
        }

        // Validar que el pedido existe antes de actualizarlo
        Optional<Order> existingOrder = orderRepository.findById(order.getId());
        if (existingOrder.isEmpty()) {
            throw new ResourceNotFoundException("Pedido no encontrado con ID: " + order.getId());
        }

        // Actualizar el pedido
        Order updatedOrder = existingOrder.get();
        updatedOrder.setClientId(order.getClientId());
        updatedOrder.setNumber(order.getNumber());

        if (order.getStatus() != null) {
            updatedOrder.setStatus(order.getStatus());
        } else {
            updatedOrder.setStatus("PENDING");
        }

        updatedOrder.setTotalPrice(order.getTotalPrice());
        updatedOrder.setOrderDetails(order.getOrderDetails());

        // Enriquecer con información del cliente (opcional)
        updatedOrder.setClientDto(response.getBody());

        // Obtener los detalles del producto para cada OrderDetail
        Double totalPrice = 0.0; // Variable para acumular el total del pedido
        for (OrderDetail orderDetail : updatedOrder.getOrderDetails()) {
            Integer productId = orderDetail.getProductId();
            ResponseEntity<ProductDto> productResponse = productFeign.getById(productId);

            if (productResponse.getStatusCode().is2xxSuccessful() && productResponse.getBody() != null) {
                ProductDto productDto = productResponse.getBody();
                orderDetail.setProductDto(productDto);  // Asignar el ProductDto completo al detalle

                // Asignar el precio del producto al detalle del pedido
                orderDetail.setPrice(productDto.getPrecio());
                orderDetail.calculateAmount();  // Calcular el monto basado en el precio y cantidad
            } else {
                throw new RuntimeException("Producto no encontrado con ID: " + productId);
            }
        }

        updatedOrder.setTotalPrice(totalPrice); // Asignar el precio total del pedido

        // Guardar el pedido actualizado
        updatedOrder = orderRepository.save(updatedOrder);

        return updatedOrder;
    }

}
