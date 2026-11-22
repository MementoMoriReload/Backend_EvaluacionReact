package com.example.Edificio.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TipoMovimiento {
    ADENTRO(Estado.DENTRO, "entrada"),
    AFUERA(Estado.FUERA, "salida");

    private final Estado estado;
    private final String etiqueta;

}
