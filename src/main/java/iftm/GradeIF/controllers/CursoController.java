package iftm.GradeIF.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import iftm.GradeIF.models.Curso;
import iftm.GradeIF.repositories.CursoRepository;

@Controller
@RequestMapping("/cursos")
public class CursoController {

    private final CursoRepository repository;

    public CursoController(CursoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public String listaCursos(Model model) {
        model.addAttribute("cursos", repository.findAll());
        return "cursos/list-curso";
    }

    @GetMapping("/deletar/{id}")
    public String deletarCurso(@PathVariable("id") int id, Model model) {
        Curso curso = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        repository.delete(curso);
        return "redirect:/cursos";
    }
}
