package cl.duoc.authservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegistroRequest(
        @NotBlank(message = "El RUT es obligatorio") String rut,

        @NotBlank(message = "El nombre es obligatorio") String nombre,

        @NotBlank(message = "El correo es obligatorio")
        @Pattern(regexp = "^[\\w.+-]+@(duoc\\.cl|profesor\\.duoc\\.cl|gmail\\.com)$",
                 message = "Correo inválido (use @duoc.cl, @profesor.duoc.cl o @gmail.com)")
        String email,

        @NotBlank(message = "La clave es obligatoria")
        @Size(min = 4, max = 10, message = "La clave debe tener entre 4 y 10 caracteres")
        String pass) {}