package cl.duoc.authservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest{
    @NotBlank(message = "El usuario es obligatorio")
    String username;

    @NotBlank(message = "La contraseña es obligatoria")
    String password;
}
