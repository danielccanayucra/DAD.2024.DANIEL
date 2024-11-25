package com.example.msinventario.Service.Impl;

import com.example.msinventario.dto.ProductoDto;
import com.example.msinventario.dto.ProveedorDto;
import com.example.msinventario.Entity.Inventario;
import com.example.msinventario.Entity.InventarioDetalle;
import com.example.msinventario.feign.ProductoFeign;
import com.example.msinventario.feign.ProveedorFeign;
import com.example.msinventario.Repository.InventarioRepository;
import com.example.msinventario.Service.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventarioServiceImpl implements InventarioService {

    @Autowired
    private InventarioRepository inventarioRepository;

    @Autowired
    private ProductoFeign productoFeign;

    @Autowired
    private ProveedorFeign proveedorFeign;

    @Override
    public List<Inventario> list() {
        return inventarioRepository.findAll();
    }

    @Override
    public Inventario save(Inventario inventario) {
        return inventarioRepository.save(inventario);
    }

    @Override
    public Optional<Inventario> findById(Integer id) {
        Optional<Inventario> inventarioOptional = inventarioRepository.findById(id);


        if (inventarioOptional.isPresent()) {
            Inventario inventario = inventarioOptional.get();


            ProveedorDto proveedorDto = proveedorFeign.getById(inventario.getProveedorId()).getBody();
            inventario.setProveedorDto(proveedorDto);


            for (InventarioDetalle detalleInventario : inventario.getInventarioDetalle()) {

                ProductoDto productoDto = productoFeign.getById(detalleInventario.getProductoId()).getBody();
                detalleInventario.setProductoDto(productoDto);
            }


            return Optional.of(inventario);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void delete(Integer id) {
        inventarioRepository.deleteById(id);
    }

    @Override
    public Inventario update(Inventario inventario) {
        return inventarioRepository.save(inventario);
    }
}
