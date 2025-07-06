package iftm.GradeIF.models;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.*;
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
    @Transient
    private String nomeAluno;

    private String periodo;

    public GradeAluno() {
        this.setTipo(0);
        disciplinas = new ArrayList<>();
        confirmada = false;
    }
}
