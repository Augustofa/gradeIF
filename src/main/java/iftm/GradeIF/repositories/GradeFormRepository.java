package iftm.GradeIF.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import iftm.GradeIF.models.GradeForm;

@Repository
public interface GradeFormRepository extends JpaRepository<GradeForm, Integer>{}
