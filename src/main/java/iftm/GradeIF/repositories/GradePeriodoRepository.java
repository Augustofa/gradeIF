package iftm.GradeIF.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import iftm.GradeIF.models.Curso;
import iftm.GradeIF.models.GradePeriodo;

@Repository
public interface GradePeriodoRepository extends JpaRepository<GradePeriodo, Integer>{
    List<GradePeriodo> findByCurso(Curso curso);
    List<GradePeriodo> findByPeriodo(int periodo);
}
