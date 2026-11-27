package cl.duoc.authservice.dto;
import cl.duoc.authservice.dto.UsuarioResponse;

public record AuthResponse(String token,
    String tipo,
    long expiresIn,
    UsuarioResponse usuario
) {}
