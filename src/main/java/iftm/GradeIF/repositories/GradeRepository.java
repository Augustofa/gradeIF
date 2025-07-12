package iftm.GradeIF.repositories;

import iftm.GradeIF.models.Aluno;
import iftm.GradeIF.models.Disciplina;
import iftm.GradeIF.models.Grade;
import iftm.GradeIF.models.GradeAluno;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface GradeRepository extends JpaRepository<Grade, Integer>{
    List<Grade> findAllByDisciplinasContaining(Disciplina disciplina);
}
