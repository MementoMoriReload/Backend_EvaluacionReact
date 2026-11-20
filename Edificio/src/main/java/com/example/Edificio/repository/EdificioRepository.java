package com.example.Edificio.repository;

import com.example.Edificio.model.Edificio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository  
public interface EdificioRepository extends JpaRepository<Edificio, String> {

}
