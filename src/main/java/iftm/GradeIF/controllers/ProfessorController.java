package iftm.GradeIF.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import iftm.GradeIF.models.Professor;
import iftm.GradeIF.repositories.ProfessorRepository;

@Controller
@RequestMapping("/professores")
public class ProfessorController {

    private final ProfessorRepository repository;

    public ProfessorController(ProfessorRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public String listaProfessores(Model model) {
        model.addAttribute("professores", repository.findAll());
        return "professores/list-professor";
    }

    @GetMapping("/deletar/{id}")
    public String deletarProfessor(@PathVariable("id") int id, Model model) {
        Professor professor = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        repository.delete(professor);
        return "redirect:/professores";
    }
}
