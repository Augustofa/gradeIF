package iftm.GradeIF.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import iftm.GradeIF.models.Aluno;
import java.util.List;


@Repository
public interface AlunoRepository extends CrudRepository<Aluno, Integer>{
    List<Aluno> findByNome(String nome);
    List<Aluno> findByCpf(String cpf);
}
