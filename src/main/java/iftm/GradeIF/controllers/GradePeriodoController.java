package iftm.GradeIF.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import iftm.GradeIF.models.Curso;
import iftm.GradeIF.models.GradePeriodo;
import iftm.GradeIF.repositories.CursoRepository;
import iftm.GradeIF.repositories.DisciplinaRepository;
import iftm.GradeIF.repositories.GradePeriodoRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/grades-periodos")
public class GradePeriodoController {

    private final GradePeriodoRepository gradePeriodoRepository;
    private final CursoRepository cursoRepository;
    public GradePeriodoController(GradePeriodoRepository gradePeriodoRepository, CursoRepository cursoRepository, DisciplinaRepository disciplinaRepository, EntityManager entityManager) {
        this.gradePeriodoRepository = gradePeriodoRepository;
        this.cursoRepository = cursoRepository;
    }

    @GetMapping
    public String listaGradePeriodos(Model model) {
        model.addAttribute("gradePeriodos", gradePeriodoRepository.findAll());
        return "grades-periodos/list-grades";
    }

    @Transactional
    public Iterable<GradePeriodo> saveAllGradePeriodos(Iterable<GradePeriodo> gradePeriodos) {
        for (GradePeriodo gradePeriodo : gradePeriodos) {
            String nomeCurso = gradePeriodo.getNomeCurso();
            Curso curso = cursoRepository.findByNome(nomeCurso).getFirst();
            gradePeriodo.setCurso(curso);
            gradePeriodoRepository.save(gradePeriodo);
        }
        return gradePeriodos;
    }

    @GetMapping("/deletar/{id}")
    public String deletarGradePeriodo(@PathVariable("id") int id, Model model) {
        GradePeriodo gradePeriodo = gradePeriodoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        gradePeriodoRepository.delete(gradePeriodo);
        return "redirect:/grades-periodos";
    }
}
