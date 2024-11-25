package com.example.msadquisicione.service;

import com.example.msadquisicione.dto.AdquisicionDto;;
import com.example.msadquisicione.entity.Adquisicion;

import java.util.List;

public interface AdquisicionService {
    Adquisicion registrarAdquisicion(AdquisicionDto adquisicionDto);
}
