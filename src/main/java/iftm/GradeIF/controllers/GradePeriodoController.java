package iftm.GradeIF.controllers;

import iftm.GradeIF.models.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import iftm.GradeIF.repositories.CursoRepository;
import iftm.GradeIF.repositories.DisciplinaRepository;
import iftm.GradeIF.repositories.GradePeriodoRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.Comparator;
import java.util.List;

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
        List<GradePeriodo> grades = gradePeriodoRepository.findAll();
        grades.sort(Comparator.comparing(GradePeriodo::getNomeCurso));
        model.addAttribute("gradePeriodos", grades);
        
        return "grades-periodos/list-grades";
    }

    @GetMapping("/criar")
    public String formularioCriarGradePeriodo(GradePeriodo gradePeriodo, Model model) {
        model.addAttribute("cursos", cursoRepository.findAll());
        return "grades-periodos/add-grade";
    }

    @PostMapping("/salvar")
    public String addGradePeriodo(@Valid GradePeriodo gradePeriodo, BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "grades-periodos/add-grade";
        }
        Curso curso = cursoRepository.findByNome(gradePeriodo.getNomeCurso()).getFirst();
        gradePeriodo.setCurso(curso);

        gradePeriodoRepository.save(gradePeriodo);
        return "redirect:/grades-periodos";
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
