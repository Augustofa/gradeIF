package iftm.GradeIF.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public abstract class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int quantCreditos;

    public int calcCreditos() {
        return quantCreditos;
    }
}
