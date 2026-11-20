package com.example.Edificio.repository;

import com.example.Edificio.model.Persona;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository 
public interface PersonaRepository extends JpaRepository<Persona, Long>{
    List<Persona> findByEdificio_EdificioIdOrderByPersonaIdAsc(String edificioId);
    Optional<Persona> findByEdificio_EdificioIdAndRut(String edificioId, String rut);
    long deleteByEdificio_EdificioIdAndRut(String edificioId, String rut);
}
