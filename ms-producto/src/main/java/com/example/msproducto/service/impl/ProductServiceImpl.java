package com.example.msproducto.service.impl;

import com.example.msproducto.entity.Producto;
import com.example.msproducto.repository.ProductoRepository;
import com.example.msproducto.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final String[] ALLOWED_CONTENT_TYPES = {
            "image/jpeg", "image/png", "image/gif", "image/jpg"
    };

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public List<Producto> list() {
        return productoRepository.findAll();
    }

    @Override
    public Producto save(Producto product, MultipartFile imagen) throws IOException {
        validateProduct(product);
        if (imagen != null && !imagen.isEmpty()) {
            validateImage(imagen);
            product.setImagen(imagen.getBytes());
        }
        return productoRepository.save(product);
    }

    @Override
    public Optional<Producto> findById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID del producto no puede ser nulo");
        }
        return productoRepository.findById(id);
    }

    @Override
    public Producto update(Producto product, MultipartFile imagen) throws IOException {
        if (product.getId() == null) {
            throw new IllegalArgumentException("El ID del producto no puede ser nulo para actualizar");
        }

        Optional<Producto> existingProduct = productoRepository.findById(product.getId());
        if (!existingProduct.isPresent()) {
            throw new RuntimeException("Producto no encontrado con ID: " + product.getId());
        }

        validateProduct(product);

        if (imagen != null && !imagen.isEmpty()) {
            validateImage(imagen);
            product.setImagen(imagen.getBytes());
        } else {
            // Mantener la imagen existente si no se proporciona una nueva
            product.setImagen(existingProduct.get().getImagen());
        }

        return productoRepository.save(product);
    }

    @Override
    public void deleteById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID del producto no puede ser nulo");
        }
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado con ID: " + id);
        }
        productoRepository.deleteById(id);
    }

    private void validateProduct(Producto product) {
        if (product == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }
        if (product.getNombre() == null || product.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es requerido");
        }
        if (product.getPrecio() == null || product.getPrecio() < 0) {
            throw new IllegalArgumentException("El precio del producto debe ser válido");
        }
        if (product.getCodigo() == null || product.getCodigo() < 0) {
            throw new IllegalArgumentException("El código del producto debe ser válido");
        }
    }

    private void validateImage(MultipartFile imagen) {
        // Validar tamaño de la imagen
        if (imagen.getSize() > MAX_IMAGE_SIZE) {
            throw new IllegalArgumentException("El tamaño de la imagen no debe exceder 5MB");
        }

        // Validar tipo de contenido
        String contentType = imagen.getContentType();
        boolean isValidContentType = false;
        for (String allowedType : ALLOWED_CONTENT_TYPES) {
            if (allowedType.equals(contentType)) {
                isValidContentType = true;
                break;
            }
        }
        if (!isValidContentType) {
            throw new IllegalArgumentException("Tipo de imagen no válido. Solo se permiten: JPEG, PNG, GIF");
        }
    }
}