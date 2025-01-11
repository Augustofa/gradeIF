package iftm.GradeIF.models;

import java.beans.Transient;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

   
    public String getCombinaDiaHora() {
        this.combinaDiaHora = (this.dia + " - " + this.horaInicio.toString() + "-" + this.horaFim.toString());
        return combinaDiaHora;
    }

    @Override
    public int compareTo(Horario o) {
        return this.getCombinaDiaHora().compareTo(o.getCombinaDiaHora());
    }
}
