package com.example.Edificio.DTO;

import com.example.Edificio.model.TipoMovimiento;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MovimientoRequest(
                @NotBlank(message = "Tienes que agregar un nombre") String nomPersona,
                @NotBlank(message = "Tienes que agregar un RUT") @Size(min = 9, max = 9, message = "El RUT debe tener 9 caracteres") String rut,
                @NotBlank(message = "Tienes que escoger un edificio") String edificioId,
                @NotNull(message = "Tienes que indicar si es entrada o salida") TipoMovimiento tipo,
                boolean visitante) {
}
