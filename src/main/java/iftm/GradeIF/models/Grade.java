package iftm.GradeIF.models;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public abstract class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int quantCreditos;

    private int tipo;

    public abstract int calcCreditos();
}
