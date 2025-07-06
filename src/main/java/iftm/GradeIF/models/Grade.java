package iftm.GradeIF.models;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Data
public abstract class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int quantCreditos;

    private int tipo;

    @ManyToMany
    List<Disciplina> disciplinas;
    @Transient
    Integer idDiscSelecionada;

    @JdbcTypeCode(SqlTypes.JSON)
    protected Map<String, String> coresDisciplinas;
    @Transient
    private String corDisciplina;

    public Boolean confirmada = false;

    public Boolean checaDisciplina(int id){
        for(Disciplina disciplina : disciplinas){
            if(disciplina.getId() == id){
                return true;
            }
        }
        return false;
    }

    public void addCorDisciplina(String nomeDisciplina, String cor) {
        if(coresDisciplinas == null){
            coresDisciplinas = new HashMap<>();
        }
        coresDisciplinas.put(nomeDisciplina, cor);
        this.setCoresDisciplinas(coresDisciplinas);
    }

    public int calcCreditos(){
        int somaCreditos = 0;
        for(Disciplina disciplina : disciplinas){
            somaCreditos += disciplina.getCreditos();
        }
        return somaCreditos;
    }
}
