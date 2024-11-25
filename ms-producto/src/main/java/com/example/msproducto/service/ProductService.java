package com.example.msproducto.service;

import com.example.msproducto.entity.Producto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ProductService {


    List<Producto> list();


    Producto save(Producto product);


    Optional<Producto> findById(Integer id);


    Producto update(Producto product);


    void deleteById(Integer id);
}
