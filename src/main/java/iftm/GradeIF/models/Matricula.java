package iftm.GradeIF.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Matricula {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_id", referencedColumnName = "id")
    private Grade grade;
    @Transient
    private int idGrade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disciplina_id", referencedColumnName = "id")
    private Disciplina disciplina;
}
