package iftm.GradeIF.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import iftm.GradeIF.models.Aluno;
import iftm.GradeIF.models.GradeAluno;


@Repository
public interface GradeAlunoRepository extends JpaRepository<GradeAluno, Integer>{
    List<GradeAluno> findByAluno(Aluno aluno);
}
