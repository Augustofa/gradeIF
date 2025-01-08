package iftm.GradeIF.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import iftm.GradeIF.models.Aluno;
import iftm.GradeIF.repositories.AlunoRepository;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoRepository repository;

    public AlunoController(AlunoRepository repository) {
        this.repository = repository;
    }
    @GetMapping("/criar")
    public String formularioCriarAluno(Aluno aluno) {
        return "alunos/add-aluno";
    }

    @PostMapping("/salvar")
    public String addAluno(@Valid Aluno aluno, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "alunos/add-aluno";
        }
        repository.save(aluno);
        return "redirect:/alunos";
    }

    @GetMapping
    public String listaAlunos(Model model) {
        model.addAttribute("alunos", repository.findAll());
        return "alunos/list-aluno";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditarAluno(@PathVariable("id") int id, Model model) {
        Aluno aluno = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        model.addAttribute("aluno", aluno);
        return "alunos/update-aluno";
    }

    @PostMapping("/atualizar/{id}")
    public String atualizarAluno(@PathVariable("id") int id, @Valid Aluno aluno, BindingResult result, Model model) {
        if (result.hasErrors()) {
            aluno.setId(id);
            return "alunos/update-aluno";
        }

        repository.save(aluno);
        return "redirect:/alunos";
    }

    @GetMapping("/deletar/{id}")
    public String deletarAluno(@PathVariable("id") int id, Model model) {
        Aluno aluno = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        repository.delete(aluno);
        return "redirect:/alunos";
    }
}
