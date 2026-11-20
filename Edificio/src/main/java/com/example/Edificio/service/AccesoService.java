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
import com.example.Edificio.model.Estado;
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
        edificios.findAll(Sort.by("edificioId")).forEach(e ->
                datos.put(
                        e.getEdificioId(),
                        personas.findByEdificio_EdificioIdOrderByPersonaIdAsc(e.getEdificioId())
                                .stream()
                                .map(PersonaResponse::new)
                                .toList()
                )
        );
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
    public MovimientoResponse registrar(MovimientoRequest m) {
        Edificio e = edificios.findById(m.edificioId())
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "El edificio " + m.edificioId() + " no existe"
                ));

        String tipo = m.tipo() != null ? m.tipo().trim() : "";
        boolean esEntrada;
        if (tipo.startsWith("Entrada")) {
            esEntrada = true;
        } else if (tipo.startsWith("Salida")) {
            esEntrada = false;
        } else {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Tipo de movimiento inválido: " + m.tipo());
        }

        boolean visitante = tipo.endsWith("Visitante");
        String rut = m.rut() != null ? m.rut().trim().toUpperCase() : "";
        String nomPersona = m.nomPersona() != null ? m.nomPersona().trim() : "";
        String hora = LocalTime.now(CHILE).format(HH_MM);

        Persona p = personas.findByEdificio_EdificioIdAndRut(e.getEdificioId(), rut)
                .orElseGet(() -> Persona.builder()
                        .edificio(e)
                        .rut(rut)
                        .estado(Estado.Fuera)
                        .build());

        Estado estadoAnterior = p.getEstado();
        Estado nuevoEstado = esEntrada ? Estado.Dentro : Estado.Fuera;

        p.setNomPersona(nomPersona);
        p.setEstado(nuevoEstado);
        p.setHora(hora);
        p.setVisitante(visitante);
        personas.save(p);

        if (estadoAnterior != Estado.Dentro && nuevoEstado == Estado.Dentro) {
            e.setCantidad(e.getCantidad() + 1);
            edificios.save(e);
        } else if (estadoAnterior == Estado.Dentro && nuevoEstado != Estado.Dentro) {
            e.setCantidad(Math.max(0, e.getCantidad() - 1));
            edificios.save(e);
        }

        return new MovimientoResponse(
                p.getNomPersona() + ": " + (esEntrada ? "entrada" : "salida") + " registrada.",
                new PersonaResponse(p)
        );
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