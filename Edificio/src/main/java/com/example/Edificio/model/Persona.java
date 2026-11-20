package com.example.Edificio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data; 

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data 
@Entity
public class Persona {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long personaId;
    private String nomPersona;
    private String rut;
    private Estado estado;
    private String hora;
    private boolean visitante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "edificio_id", nullable = false)
    private Edificio edificio;
}
