package com.example.msadquisicione.service.impl;

import com.example.msadquisicione.dto.AdquisicionDto;
import com.example.msadquisicione.dto.ProductoDto;
import com.example.msadquisicione.entity.Adquisicion;
import com.example.msadquisicione.repository.AdquisicionRepository;
import com.example.msadquisicione.feign.ProveedorFeign;
import com.example.msadquisicione.feign.ProductoFeign;
import com.example.msadquisicione.service.AdquisicionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdquisicionServiceImpl implements AdquisicionService {

    @Autowired
    private AdquisicionRepository adquisicionRepository;

    @Autowired
    private ProveedorFeign proveedorFeign;

    @Autowired
    private ProductoFeign productoFeign;

    @Override
    public Adquisicion registrarAdquisicion(AdquisicionDto adquisicionDto) {
        // 1. Validar proveedor
        Boolean proveedorValido = proveedorFeign.verificarProveedor(adquisicionDto.getProveedorId());
        if (!proveedorValido) {
            throw new RuntimeException("El proveedor no existe.");
        }

        // 2. Guardar adquisición
        Adquisicion adquisicion = new Adquisicion();
        adquisicion.setProveedorId(adquisicionDto.getProveedorId());
        adquisicion.setProductoId(adquisicionDto.getProductoId());
        adquisicion.setCantidad(adquisicionDto.getCantidad());
        adquisicion.setPrecioCompra(adquisicionDto.getPrecioCompra());
        adquisicion.setPrecioVenta(adquisicionDto.getPrecioVenta());

        Adquisicion nuevaAdquisicion = adquisicionRepository.save(adquisicion);

        // 3. Actualizar stock
        ProductoDto productoDto = new ProductoDto();
        productoDto.setId(adquisicionDto.getProductoId());
        productoDto.setStock(adquisicionDto.getCantidad());
        productoFeign.actualizarStock(productoDto);

        return nuevaAdquisicion;
    }
}