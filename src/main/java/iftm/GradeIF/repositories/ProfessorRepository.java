package iftm.GradeIF.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import iftm.GradeIF.models.Professor;

@Repository
public interface ProfessorRepository extends CrudRepository<Professor, Integer> {
    Optional<Professor> findByNome(String nome);
    // Optional<Professor> findByCpf(String cpf);
}
