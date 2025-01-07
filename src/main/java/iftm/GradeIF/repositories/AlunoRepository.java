package iftm.GradeIF.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import iftm.GradeIF.models.Aluno;

@Repository
public interface AlunoRepository extends CrudRepository<Aluno, Integer>{}
