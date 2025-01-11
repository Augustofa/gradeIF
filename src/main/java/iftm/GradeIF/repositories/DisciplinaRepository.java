package iftm.GradeIF.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import iftm.GradeIF.models.Disciplina;
import java.util.List;


@Repository
public interface DisciplinaRepository extends CrudRepository<Disciplina, Integer>{
    List<Disciplina> findByCodigo(String codigo);
}
