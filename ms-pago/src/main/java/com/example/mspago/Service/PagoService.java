package com.example.mspago.Service;

import com.example.mspago.Entity.Pago; // Importa la entidad Pago

import java.util.List;
import java.util.Optional;

public interface PagoService {
    public List<Pago> list();

    public Pago save(Pago pago);

    public Pago update(Pago pago);

    Optional<Pago> findById(Integer id);
    public void delete(Integer id);
}
