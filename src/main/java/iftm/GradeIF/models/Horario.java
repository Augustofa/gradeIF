package iftm.GradeIF.models;

import java.time.LocalTime;
import java.util.Map;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Horario implements Comparable<Horario> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String dia;

    private LocalTime horaInicio;
    
    private LocalTime horaFim;

    private String combinaDiaHora;

    
    @PostConstruct
    public String getCombinaDiaHora() {
        this.combinaDiaHora = (this.dia + " - " + this.horaInicio.toString() + "-" + this.horaFim.toString());
        return combinaDiaHora;
    }
    
    private static final Map<String, Integer> DIAS_DA_SEMANA = Map.of(
        "Segunda", 1,
        "Terça", 2,
        "Quarta", 3,
        "Quinta", 4,
        "Sexta", 5,
        "Sábado", 6,
        "Domingo", 7
    );

    @Override
    public int compareTo(Horario o) {
        int diaComparison = Integer.compare(DIAS_DA_SEMANA.get(this.dia), DIAS_DA_SEMANA.get(o.dia));
        if (diaComparison != 0) {
            return diaComparison;
        }
        return this.horaInicio.compareTo(o.horaInicio);
    }
}
