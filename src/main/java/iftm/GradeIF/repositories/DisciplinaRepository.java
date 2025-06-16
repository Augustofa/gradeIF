package iftm.GradeIF.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import iftm.GradeIF.models.Disciplina;
import java.util.List;


@Repository
public interface DisciplinaRepository extends JpaRepository<Disciplina, Integer>{
    List<Disciplina> findByCodigo(String codigo);
    List<Disciplina> findByNome(String nome);
}
