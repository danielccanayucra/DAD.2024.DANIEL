package com.example.mscatalogo.controller;

import com.example.mscatalogo.entity.Category;
import com.example.mscatalogo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;


    @GetMapping()
    public ResponseEntity<List<Category>> list() {
        return ResponseEntity.ok().body(categoryService.listar());
    }
    @PostMapping()
    public ResponseEntity<Category> save(@RequestBody Category category){
        return ResponseEntity.ok(categoryService.guardar(category));
    }
    @PutMapping()
    public ResponseEntity<Category> update(@RequestBody Category category){
        return ResponseEntity.ok(categoryService.actualizar(category));
    }
    @GetMapping("/{id}")
    public ResponseEntity<Category> listById(@PathVariable(required = true) Integer id){
        return ResponseEntity.ok().body(categoryService.listarPorId(id).get());
    }
    @DeleteMapping("/{id}")
    public String deleteById(@PathVariable(required = true) Integer id){
        categoryService.eliminarPorId(id);
        return "Eliminacion Correcta";
    }

}
