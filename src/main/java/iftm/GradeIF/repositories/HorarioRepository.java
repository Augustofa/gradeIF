package iftm.GradeIF.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import iftm.GradeIF.models.Horario;
import java.util.List;


@Repository
public interface HorarioRepository extends CrudRepository<Horario, Integer>{
    List<Horario> findByCombinaDiaHora(String combinaDiaHora);
}
