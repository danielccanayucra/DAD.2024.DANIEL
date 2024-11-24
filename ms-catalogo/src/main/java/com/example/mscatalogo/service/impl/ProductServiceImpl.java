package com.example.mscatalogo.service.impl;

import com.example.mscatalogo.entity.Product;
import com.example.mscatalogo.repository.ProductRepository;
import com.example.mscatalogo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<
            Product> list() {
        return productRepository.findAll();
    }

    @Override
    public Optional<
            Product> findById(Integer id) {
        return productRepository.findById(id);
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product update(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void delete(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        productRepository.delete(product);
    }
    @Override
    public Product reducirStock(Integer id, Integer stock) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (product.getStock() < stock) {
            throw new RuntimeException("Stock insuficiente para el producto: " + product.getName());
        }

        product.setStock(product.getStock() - stock);
        return productRepository.save(product);
    }

    @Override
    public Product incrementarStock(Integer id, Integer stock) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        product.setStock(product.getStock() + stock);
        return productRepository.save(product);
    }
}
