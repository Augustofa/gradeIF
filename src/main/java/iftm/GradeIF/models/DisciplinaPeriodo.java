package iftm.GradeIF.models;

import lombok.Data;

import java.util.List;

@Data
public class DisciplinaPeriodo {
    private String codigo;

    private String nome;

    private int periodo;

    private boolean extracurricular;
}
