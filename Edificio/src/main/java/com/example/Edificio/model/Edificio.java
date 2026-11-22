package com.example.Edificio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Edificio {
    @Id
    @Column(length = 10)
    private String edificioId;
    @Column(nullable = false)
    private String nomEdificio;
    @Column(nullable = false)

    private int cantidad;
}
