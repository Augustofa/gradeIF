package iftm.GradeIF.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import iftm.GradeIF.models.Curso;
import java.util.List;


@Repository
public interface CursoRepository extends CrudRepository<Curso, Integer>{
    List<Curso> findByNome(String nome);
}
