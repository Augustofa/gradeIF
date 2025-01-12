package iftm.GradeIF.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import iftm.GradeIF.models.Horario;


@Repository
public interface HorarioRepository extends JpaRepository<Horario, Integer>{
    List<Horario> findByDia(String dia);

    List<Horario> findByCombinaDiaHora(String combinaDiaHora);

    List<Horario> findByCombinaDiaHoraContaining(@Param("combinaDiaHora") String combinaDiaHora);
}
