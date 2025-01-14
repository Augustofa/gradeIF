package iftm.GradeIF.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Disciplina {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    private String codigo;
    
    private String nome;
    
    private String sala;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id", referencedColumnName = "id")
    private Professor professor;
    private String nomeProfessor;
    
    private int vagas;

    private int creditos;
    
    @ManyToMany(fetch = FetchType.LAZY)
    // @Convert(converter = DisciplinaConverter.class)
    private List<Disciplina> preRequisitos;
    private List<String> preRequisitosNomes;
    
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<Horario> horarios;
    private List<String> horariosNomes;

    public Disciplina(){
        preRequisitos = new ArrayList<>();
        preRequisitosNomes = new ArrayList<>();
        horarios = new ArrayList<>();
        horariosNomes = new ArrayList<>();

    }

    public Boolean subtraiVaga(){
        if(this.vagas > 0){
            vagas--;
            return true;
        }
        return false;
    }

    public void revogaVaga(){
        this.vagas++;
    }
}
