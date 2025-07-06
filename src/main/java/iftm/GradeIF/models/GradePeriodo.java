package iftm.GradeIF.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class GradePeriodo extends Grade{
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", referencedColumnName = "id")
    private Curso curso;
    private String nomeCurso;

    private int periodo;
    @Transient
    private String cursoPeriodo;

    public GradePeriodo() {
        this.setTipo(1);
    }

    @PostLoad
    private void onLoad() {
        this.cursoPeriodo = curso.getNome() + " - " + periodo;
    }

    public int calcCreditos(){
        return 0;
    }
}
