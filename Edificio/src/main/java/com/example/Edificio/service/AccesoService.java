package com.example.Edificio.service;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Edificio.DTO.EdificioResponse;
import com.example.Edificio.DTO.MovimientoRequest;
import com.example.Edificio.DTO.MovimientoResponse;
import com.example.Edificio.DTO.PersonaResponse;
import com.example.Edificio.Exception.ApiException;
import com.example.Edificio.model.Edificio;
import com.example.Edificio.model.Persona;
import com.example.Edificio.repository.EdificioRepository;
import com.example.Edificio.repository.PersonaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccesoService {

    private static final ZoneId CHILE = ZoneId.of("America/Santiago");
    private static final DateTimeFormatter HH_MM = DateTimeFormatter.ofPattern("HH:mm");

    private final EdificioRepository edificios;
    private final PersonaRepository personas;

    public List<EdificioResponse> listar() {
        return edificios.findAll(Sort.by("edificioId")).stream()
                .map(e -> new EdificioResponse(e.getEdificioId(), e.getNomEdificio(), e.getCantidad()))
                .toList();
    }

    public Map<String, List<PersonaResponse>> obtenerDatos() {
        Map<String, List<PersonaResponse>> datos = new LinkedHashMap<>();
        edificios.findAll(Sort.by("edificioId")).forEach(e -> datos.put(
                e.getEdificioId(),
                personas.findByEdificio_EdificioIdOrderByPersonaIdAsc(e.getEdificioId())
                        .stream()
                        .map(PersonaResponse::new)
                        .toList()));
        return datos;
    }

    public List<PersonaResponse> obtenerPersonas(String edificioId) {
        asegurarEdificio(edificioId);
        return personas.findByEdificio_EdificioIdOrderByPersonaIdAsc(edificioId)
                .stream()
                .map(PersonaResponse::new)
                .toList();
    }

    @Transactional
    public MovimientoResponse registrar(MovimientoRequest req) {
        Edificio edificio = edificios.findById(req.edificioId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                        "El edificio " + req.edificioId() + " no existe"));

        String rut = req.rut().trim().toUpperCase();

        Persona p = personas.findByEdificio_EdificioIdAndRut(edificio.getEdificioId(), rut)
                .orElseGet(() -> Persona.builder().edificio(edificio).rut(rut).build());
        p.setNomPersona(req.nomPersona().trim());
        p.setEstado(req.tipo().getEstado());
        p.setHora(LocalTime.now(CHILE).format(HH_MM));
        p.setVisitante(req.visitante());
        personas.save(p);

        return new MovimientoResponse(
                p.getNomPersona() + ": " + req.tipo().getEtiqueta() + " registrada.",
                new PersonaResponse(p));
    }

    @Transactional
    public void eliminar(String id, String rut) {
        asegurarEdificio(id);
        if (personas.deleteByEdificio_EdificioIdAndRut(id, rut) == 0)
            throw new ApiException(HttpStatus.NOT_FOUND, "No existe la persona con RUT " + rut);
    }

    private void asegurarEdificio(String id) {
        if (!edificios.existsById(id)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "El edificio " + id + " no existe");
        }
    }
}