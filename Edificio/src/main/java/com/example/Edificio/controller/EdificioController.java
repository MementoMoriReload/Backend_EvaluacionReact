package com.example.Edificio.controller;

import com.example.Edificio.DTO.EdificioResponse;
import com.example.Edificio.DTO.MovimientoRequest;
import com.example.Edificio.DTO.MovimientoResponse;
import com.example.Edificio.DTO.PersonaResponse;
import com.example.Edificio.service.AccesoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Tag(name = "Edificio", description = "Control de entradas y salidas por edificio")
@RestController
@RequestMapping("/api/v1/Edificio")
public class EdificioController {
    private final AccesoService service;

    @Operation(summary = "Registrar entrada o salida de una persona")
    @PostMapping
    public MovimientoResponse registrar(@Valid @RequestBody MovimientoRequest m) {
        return service.registrar(m);
    }

    @Operation(summary = "Listar edificios")
    @GetMapping
    public List<EdificioResponse> edificios() {
        return service.listar();
    }

    @Operation(summary = "Obtener las personas de todos los edificios")
    @GetMapping("/personas")
    public Map<String, List<PersonaResponse>> datos() {
        return service.obtenerDatos();
    }

    @Operation(summary = "Obtener las personas de un edificio")
    @GetMapping("/personas/{id}")
    public List<PersonaResponse> personas(@PathVariable String id) {
        return service.obtenerPersonas(id);
    }

    @Operation(summary = "Eliminar una persona de un edificio por RUT")
    @DeleteMapping("/personas/{id}/{rut}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable String id, @PathVariable String rut) {
        service.eliminar(id, rut);
    }

}
