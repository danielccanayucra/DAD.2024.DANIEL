package com.example.msinventario.Service;

import com.example.msinventario.Entity.Inventario;

import java.util.List;
import java.util.Optional;

public interface InventarioService {
    public List<Inventario> list();

    public Inventario save(Inventario inventario);

    public Inventario update(Inventario inventario);

    Optional<Inventario> findById(Integer id);

    public void delete(Integer id);
}
