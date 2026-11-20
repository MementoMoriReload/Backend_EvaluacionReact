package com.example.Edificio.DTO;

import com.example.Edificio.model.Estado;
import com.example.Edificio.model.Persona;

public record PersonaResponse(
        String nomPersona,
        String rut,
        Estado estado,
        String hora,
        boolean visitante
) {
    public PersonaResponse(Persona p) {
        this(
                p.getNomPersona(),
                p.getRut(),
                p.getEstado(),
                p.getHora(),
                p.isVisitante()
        );
    }
}
