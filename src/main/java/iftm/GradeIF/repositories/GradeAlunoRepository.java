package iftm.GradeIF.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import iftm.GradeIF.models.GradeAluno;

@Repository
public interface GradeAlunoRepository extends JpaRepository<GradeAluno, Integer>{}
