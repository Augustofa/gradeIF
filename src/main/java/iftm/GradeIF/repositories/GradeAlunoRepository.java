package iftm.GradeIF.repositories;

import iftm.GradeIF.models.Aluno;
import iftm.GradeIF.models.GradeAluno;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface GradeAlunoRepository extends JpaRepository<GradeAluno, Integer>{
    List<GradeAluno> findByAluno(Aluno aluno);
    List<GradeAluno> findByAluno(Aluno aluno, Sort sort);
}
