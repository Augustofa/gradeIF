package iftm.GradeIF.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import iftm.GradeIF.models.Curso;
import iftm.GradeIF.repositories.CursoRepository;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/cursos")
public class CursoController {

    private final CursoRepository repository;

    public CursoController(CursoRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/criar")
    public String formularioCriarCurso(Curso curso) {
        return "cursos/add-curso";
    }

    @PostMapping("/salvar")
    public String addCurso(@Valid Curso curso, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "cursos/add-curso";
        }
        repository.save(curso);
        return "redirect:/cursos";
    }

    @GetMapping
    public String listaCursos(Model model) {
        model.addAttribute("cursos", repository.findAll());
        return "cursos/list-curso";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditarCurso(@PathVariable("id") int id, Model model) {
        Curso curso = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        model.addAttribute("curso", curso);
        return "cursos/update-curso";
    }

    @PostMapping("/atualizar/{id}")
    public String atualizarCurso(@PathVariable("id") int id, @Valid Curso curso, BindingResult result, Model model) {
        if (result.hasErrors()) {
            curso.setId(id);
            return "cursos/update-curso";
        }

        repository.save(curso);
        return "redirect:/cursos";
    }

    @GetMapping("/deletar/{id}")
    public String deletarCurso(@PathVariable("id") int id, Model model) {
        Curso curso = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        repository.delete(curso);
        return "redirect:/cursos";
    }
}
