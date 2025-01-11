package iftm.GradeIF.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import iftm.GradeIF.models.Horario;
import iftm.GradeIF.repositories.HorarioRepository;

@Controller
@RequestMapping("/horarios")
public class HorarioController {

    private final HorarioRepository repository;

    public HorarioController(HorarioRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public String listaHorarios(Model model) {
        model.addAttribute("horarios", repository.findAll());
        return "horarios/list-horario";
    }

    @GetMapping("/deletar/{id}")
    public String deletarHorario(@PathVariable("id") int id, Model model) {
        Horario horario = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        repository.delete(horario);
        return "redirect:/horarios";
    }
}