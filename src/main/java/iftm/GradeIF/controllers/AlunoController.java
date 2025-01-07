package iftm.GradeIF.controllers;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import iftm.GradeIF.models.Aluno;
import iftm.GradeIF.repositories.AlunoRepository;
import jakarta.validation.Valid;

public class AlunoController {

    AlunoRepository alunosRepository;
    
    @GetMapping("/alunos/criar")
    public String formularioCriarAluno(Aluno aluno) {
        return "add-aluno";
    }

    @PostMapping("/alunos/salvar")
    public String addAluno(@Valid Aluno aluno, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return "add-aluno";
        }
        alunosRepository.save(aluno);
        attributes.addFlashAttribute("mensagem", "Aluno salvo com sucesso!");
        return "redirect:/alunos";
    }

    @GetMapping("/alunos")
    public String listaAlunos(Model model) {
        model.addAttribute("alunos", alunosRepository.findAll());
        return "alunos";
    }

    @GetMapping("/alunos/editar/{id}")
    public String formularioEditarAluno(@PathVariable("id") int id, Model model) {
        Aluno aluno = alunosRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        model.addAttribute("aluno", aluno);
        return "update-aluno";
    }

    @PostMapping("/alunos/atualizar/{id}")
    public String atualizarAluno(@PathVariable("id") int id, @Valid Aluno aluno, BindingResult result, Model model) {
        if (result.hasErrors()) {
            aluno.setId(id);
            return "update-aluno";
        }

        alunosRepository.save(aluno);
        return "redirect:/alunos";
    }

    @GetMapping("/alunos/deletar/{id}")
    public String deletarAluno(@PathVariable("id") int id, Model model) {
        Aluno aluno = alunosRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        alunosRepository.delete(aluno);
        return "redirect:/alunos";
    }
}
