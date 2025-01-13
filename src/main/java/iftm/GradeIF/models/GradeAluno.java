package iftm.GradeIF.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class GradeAluno extends Grade {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", referencedColumnName = "id")
    private Aluno aluno;
    private String nomeAluno;

    private String periodo;

    @ManyToMany
    private List<Disciplina> disciplinas;
    private Integer idDiscSelecionada;

    public GradeAluno() {
        this.setTipo(0);
        disciplinas = new ArrayList<>();
    }

    public int calcCreditos(){
        return 0;
    }
}