package iftm.GradeIF.models;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
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

    private Integer idDiscSelecionada;
    private List<Integer> idDisciplinas;

    public GradeAluno() {
        this.setTipo(0);
    }

    public int calcCreditos(){
        return 0;
    }
}
