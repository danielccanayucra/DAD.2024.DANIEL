package com.example.mspago.Service;

import com.example.mspago.Entity.Pago; // Importa la entidad Pago
import com.example.mspago.dto.PagoRequestDto;
import com.example.mspago.dto.PagoResponseDto;

import java.util.List;
import java.util.Optional;

public interface PagoService {


    PagoResponseDto procesarPago(PagoRequestDto pagoRequest);
}
