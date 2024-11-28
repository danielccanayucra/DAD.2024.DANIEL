package com.example.mspago.Service.Impl;

import com.example.mspago.dto.PagoRequestDto;
import com.example.mspago.dto.PagoResponseDto;
import com.example.mspago.dto.PedidoDto;
import com.example.mspago.Entity.Pago;
import com.example.mspago.feign.PedidoFeign;
import com.example.mspago.Repository.PagoRepository;
import com.example.mspago.Service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PagoServiceImpl implements PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private PedidoFeign pedidoFeign;

    @Override
    public PagoResponseDto procesarPago(PagoRequestDto pagoRequest) {
        // 1. Obtener los detalles del pedido desde el microservicio de pedidos
        PedidoDto pedido = pedidoFeign.getPedidoById(pagoRequest.getPedidoId());
        if (pedido == null || !"PENDIENTE".equalsIgnoreCase(pedido.getStatus())) {
            return new PagoResponseDto(null, "FALLIDO", "El pedido no está disponible para pago.", null, null);

        }

        // 2. Registrar el pago
        Pago pago = new Pago();
        pago.setPedidoId(pedido.getId());
        pago.setMonto(pedido.getTotal_price()); // Monto tomado directamente del pedido
        pago.setMetodoPago(pagoRequest.getMetodoPago());
        pago.setEstado("COMPLETADO");
        pagoRepository.save(pago);

        // 3. Actualizar el estado del pedido a "FINALIZADO"
        pedidoFeign.actualizarEstadoPedido(pedido.getId(), "FINALIZADO");

        return new PagoResponseDto(pago.getId(), "COMPLETADO", "Pago realizado con éxito.", pago.getMonto(), pedido.getId());

    }
}

