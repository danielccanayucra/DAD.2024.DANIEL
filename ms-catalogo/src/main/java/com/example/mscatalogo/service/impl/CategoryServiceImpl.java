package com.example.mscatalogo.service.impl;

import com.example.mscatalogo.entity.Category;
import com.example.mscatalogo.repository.CategoryRepository;
import com.example.mscatalogo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    public List<Category> listar() {
        return categoryRepository.findAll();
    }


    @Override
    public Category guardar(Category categoria) {
        return categoryRepository.save(categoria);
    }


    @Override
    public Category actualizar(Category categoria) {
        return categoryRepository.save(categoria);
    }


    @Override
    public Optional<Category> listarPorId(Integer id) {
        return categoryRepository.findById(id);
    }


    @Override
    public void eliminarPorId(Integer id) {
        categoryRepository.deleteById(id);
    }

}
