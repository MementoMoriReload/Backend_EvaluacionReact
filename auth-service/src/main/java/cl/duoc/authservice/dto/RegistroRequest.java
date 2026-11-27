package cl.duoc.authservice.dto;

import cl.duoc.authservice.model.Rol;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroRequest{
        @NotBlank(message = "El RUT es obligatorio")
        String rut;

        @NotBlank(message = "El nombre es obligatorio")
        String nombre;

        @NotBlank(message = "El correo es obligatorio")
        String email;

        @NotBlank(message = "La clave es obligatoria")
        @Size(min = 4, max = 10, message = "La clave debe tener entre 4 y 10 caracteres")
        String pass ;

        private Rol rol;
}