package iftm.GradeIF.models;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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

    private Boolean confirmada;

    public GradeAluno() {
        this.setTipo(0);
        disciplinas = new ArrayList<>();
        confirmada = false;
    }

    public Boolean checaDisciplina(int id){
        for(Disciplina disciplina : disciplinas){
            if(disciplina.getId() == id){
                return true;
            }
        }
        return false;
    }

    @Override
    public void addCorDisciplina(String nomeDisciplina, String cor) {
        if(coresDisciplinas == null){
            coresDisciplinas = new HashMap<>();
        }
        System.out.println(coresDisciplinas);
        coresDisciplinas.put(nomeDisciplina, cor);
        this.setCoresDisciplinas(coresDisciplinas);
    }

    @Override
    public int calcCreditos(){
        int somaCreditos = 0;
        for(Disciplina disciplina : disciplinas){
            somaCreditos += disciplina.getCreditos();
        }
        return somaCreditos;
    }
}
