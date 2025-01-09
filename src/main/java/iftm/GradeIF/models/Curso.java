package iftm.GradeIF.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotNull(message = "Quantidade de semestres é obrigatório")
    private int quantSemestres;

    private String descricao;
}
