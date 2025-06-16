package iftm.GradeIF.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @Column(name = "user_login")
    private String login;

    @Column(name = "user_passwd")
    private String senha;

    @OneToOne
    @JoinColumn(name = "id_aluno")
    private Aluno aluno;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="roles", joinColumns = @JoinColumn(name = "user_id"))
    private List<String> permissoes;
}
