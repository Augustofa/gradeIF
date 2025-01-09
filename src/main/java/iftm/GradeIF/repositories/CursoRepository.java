package iftm.GradeIF.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import iftm.GradeIF.models.Curso;

@Repository
public interface CursoRepository extends CrudRepository<Curso, Integer>{}
