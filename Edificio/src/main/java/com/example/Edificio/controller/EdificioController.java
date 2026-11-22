package com.example.Edificio.controller;

import com.example.Edificio.DTO.EdificioResponse;
import com.example.Edificio.DTO.MovimientoRequest;
import com.example.Edificio.DTO.MovimientoResponse;
import com.example.Edificio.DTO.PersonaResponse;
import com.example.Edificio.service.AccesoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/Edificio")
@CrossOrigin(origins = "http://localhost:5173")
public class EdificioController {
    private final AccesoService service;

    @PostMapping
    public MovimientoResponse registrar(@Valid @RequestBody MovimientoRequest m) {
        return service.registrar(m);
    }

    @GetMapping
    private List<EdificioResponse> edificios() {
        return service.listar();
    }

    @GetMapping("/personas")
    public Map<String, List<PersonaResponse>> datos() {
        return service.obtenerDatos();
    }

    @GetMapping("/personas/{id}")
    public List<PersonaResponse> personas(@PathVariable String id) {
        return service.obtenerPersonas(id);
    }

    @DeleteMapping("/personas/{rut}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable String id, @PathVariable String rut) {
        service.eliminar(id, rut);
    }

}
