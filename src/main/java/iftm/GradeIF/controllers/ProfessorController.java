package iftm.GradeIF.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import iftm.GradeIF.models.Professor;
import iftm.GradeIF.repositories.ProfessorRepository;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/professores")
public class ProfessorController {

    private final ProfessorRepository professorRepository;

    public ProfessorController(ProfessorRepository repository) {
        this.professorRepository = repository;
    }

    @GetMapping
    public String listaProfessores(Model model) {
        model.addAttribute("professores", professorRepository.findAll());
        return "professores/list-professor";
    }

    @GetMapping("/criar")
    public String formularioCriarAluno(Professor professor, Model model) {
        return "professores/add-professor";
    }
    
    @PostMapping("/salvar")
    public String addAluno(@Valid Professor professor, BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "professores/add-aluno";
        }

        professorRepository.save(professor);
        return "redirect:/professores";
    }

    @GetMapping("/deletar/{id}")
    public String deletarProfessor(@PathVariable("id") int id, Model model) {
        Professor professor = professorRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        professorRepository.delete(professor);
        return "redirect:/professores";
    }
}
