package iftm.GradeIF.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import iftm.GradeIF.models.Aluno;
import iftm.GradeIF.models.Curso;


@Repository
public interface AlunoRepository extends CrudRepository<Aluno, Integer>{
    List<Aluno> findByNome(String nome);
    Aluno findByCpf(String cpf);
    List<Aluno> findByCurso(Curso curso);
}
