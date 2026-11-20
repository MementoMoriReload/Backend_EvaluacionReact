package com.example.Edificio.DTO;

public record MovimientoRequest (
        String nomPersona,
        String rut,
        String edificioId,
        String tipo
) {}
