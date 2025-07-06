package iftm.GradeIF.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
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
    @Transient
    private String nomeProfessor;
    
    private int vagas;

    private int creditos;

    @Transient
    private int periodo = 99;
    
    @ManyToMany(fetch = FetchType.LAZY)
    // @Convert(converter = DisciplinaConverter.class)
    private List<Disciplina> preRequisitos;
    @Transient
    private List<String> preRequisitosNomes;
    
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<Horario> horarios;
    @Transient
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
