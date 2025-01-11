package iftm.GradeIF.utils;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import iftm.GradeIF.models.Disciplina;
import iftm.GradeIF.repositories.DisciplinaRepository;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class DisciplinaConverter implements AttributeConverter<List<Disciplina>, List<String>> {
    DisciplinaRepository disciplinaRepository;
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<String> convertToDatabaseColumn(List<Disciplina> disciplina) {
        try {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Disciplina> convertToEntityAttribute(List<String> disciplinas) {
        try {
            List<Disciplina> disciplinasObj = new ArrayList<>();
            for(String disciplina : disciplinas) {
                Disciplina tempDisciplina = disciplinaRepository.findByCodigo(disciplina).getFirst();
                disciplinasObj.add(tempDisciplina);
            }
            return disciplinasObj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
