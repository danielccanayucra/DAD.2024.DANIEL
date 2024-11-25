package com.example.msadquisicione.service;

import com.example.msadquisicione.dto.AdquisicionDto;;
import com.example.msadquisicione.dto.AdquisicionRequestDTO;
import com.example.msadquisicione.dto.AdquisicionResponseDTO;
import com.example.msadquisicione.entity.Adquisicion;

import java.util.List;

public interface AdquisicionService {

    AdquisicionResponseDTO registrarAdquisicion(AdquisicionRequestDTO request);

    List<AdquisicionResponseDTO> listarAdquisiciones();

    List<AdquisicionResponseDTO> listarAdquisicionesPorProveedor(Integer proveedorId);

    void eliminarAdquisicion(Integer id);
}
