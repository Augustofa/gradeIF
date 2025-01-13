package iftm.GradeIF.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(unique = true)
    private String nome;

    private int quantSemestres;

    private String descricao;

    // @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH, orphanRemoval = true, mappedBy = "curso")
    // private List<GradePeriodo> gradesPeriodos;
}
