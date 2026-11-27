package cl.duoc.authservice.dto;

import cl.duoc.authservice.model.Rol;

public record UsuarioResponse(
        String rut,
        String nombre,
        String email,
        Rol rol) {}