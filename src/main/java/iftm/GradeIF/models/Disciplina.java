package iftm.GradeIF.models;

import java.util.List;

import jakarta.persistence.ElementCollection;
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
    
    // @ManyToOne
    // @JoinColumn(name = "professor_id", nullable = false)
    // private Professor professor;
    
    private int vagas;

    private int creditos;
    
    @ElementCollection
    @ManyToMany(fetch = FetchType.LAZY)
    // @Convert(converter = DisciplinaConverter.class)
    private List<Disciplina> preRequisitos;
    private List<String> strPreRequisitos;
    
    @ManyToMany(fetch = FetchType.LAZY)
    // @Convert(converter = DisciplinaConverter.class)
    private List<Disciplina> posRequisitos;
    private List<String> strPosRequisitos;
    
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Horario> horarios;
}
