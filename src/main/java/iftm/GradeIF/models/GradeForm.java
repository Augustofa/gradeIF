package iftm.GradeIF.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;

@Entity
@Data
public class GradeForm {
    @Id
    private int idGrade;

    private String nomeAluno;

    private String idDisciplina;

    @ManyToMany
    List<Disciplina> disciplinas;

    public GradeForm(){
        disciplinas = new ArrayList<>();
    }
}
