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
    private String edificioId;
    private String nomEdificio;
    private int cantidad;
}
