package iftm.GradeIF.models;

import java.util.List;

import lombok.Data;

@Data
public class DisciplinaHorario {
    private String codigo;

    private List<Horario> horarios;

    private Boolean preReqCumpridos;

    public DisciplinaHorario(){
        preReqCumpridos = true;
    }
}
