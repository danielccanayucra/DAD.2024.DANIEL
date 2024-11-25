package com.example.msadquisicione.service.impl;

import com.example.msadquisicione.dto.*;
import com.example.msadquisicione.entity.Adquisicion;
import com.example.msadquisicione.repository.AdquisicionRepository;
import com.example.msadquisicione.feign.ProveedorFeign;
import com.example.msadquisicione.feign.ProductoFeign;
import com.example.msadquisicione.service.AdquisicionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdquisicionServiceImpl implements AdquisicionService {

    @Autowired
    private AdquisicionRepository adquisicionRepository;

    @Autowired
    private ProveedorFeign proveedorFeign;

    @Autowired
    private ProductoFeign productoFeign;

    @Override
    public AdquisicionResponseDTO registrarAdquisicion(AdquisicionRequestDTO request) {
        // 1. Validar proveedor
        ProveedorDto proveedor = proveedorFeign.obtenerProveedor(request.getProveedorId());
        if (proveedor == null) {
            throw new RuntimeException("El proveedor no existe.");
        }

        // 2. Validar producto existente
        Boolean productoExiste = productoFeign.verificarProducto(request.getProductoId());
        if (!productoExiste) {
            throw new RuntimeException("El producto no existe.");
        }

        // 3. Registrar adquisición
        Adquisicion adquisicion = new Adquisicion();
        adquisicion.setProductoId(request.getProductoId());
        adquisicion.setProveedorId(request.getProveedorId());
        adquisicion.setCantidad(request.getCantidad());
        adquisicion.setPrecioCompra(request.getPrecioCompra());
        adquisicion.setPrecioVenta(request.getPrecioVenta());
        Adquisicion adquisicionGuardada = adquisicionRepository.save(adquisicion);

        // 4. Actualizar stock
        productoFeign.actualizarStock(request.getProductoId(), request.getCantidad());

        // 5. Devolver respuesta
        return mapToResponseDTO(adquisicionGuardada);
    }

    @Override
    public List<AdquisicionResponseDTO> listarAdquisiciones() {
        return adquisicionRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AdquisicionResponseDTO> listarAdquisicionesPorProveedor(Integer proveedorId) {
        return adquisicionRepository.findByProveedorId(proveedorId)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void eliminarAdquisicion(Integer id) {
        if (!adquisicionRepository.existsById(id)) {
            throw new RuntimeException("La adquisición no existe.");
        }
        adquisicionRepository.deleteById(id);
    }

    private AdquisicionResponseDTO mapToResponseDTO(Adquisicion adquisicion) {
        AdquisicionResponseDTO response = new AdquisicionResponseDTO();
        response.setId(adquisicion.getId());
        response.setProductoId(adquisicion.getProductoId());
        response.setProveedorId(adquisicion.getProveedorId());
        response.setCantidad(adquisicion.getCantidad());
        response.setPrecioCompra(adquisicion.getPrecioCompra());
        response.setPrecioVenta(adquisicion.getPrecioVenta());
        response.setFechaAdquisicion(adquisicion.getFechaAdquisicion().toString());
        return response;
    }
}