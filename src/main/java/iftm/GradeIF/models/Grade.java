package iftm.GradeIF.models;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Data
public abstract class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int quantCreditos;

    private int tipo;

    @JdbcTypeCode(SqlTypes.JSON)
    protected Map<String, String> coresDisciplinas;
    private String corDisciplina;

    public abstract void addCorDisciplina(String nomeDisciplina, String cor);

    public abstract int calcCreditos();
}
