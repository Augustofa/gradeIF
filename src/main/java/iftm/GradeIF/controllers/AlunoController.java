package iftm.GradeIF.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import iftm.GradeIF.models.Aluno;
import iftm.GradeIF.models.Curso;
import iftm.GradeIF.repositories.AlunoRepository;
import iftm.GradeIF.repositories.CursoRepository;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoRepository alunoRepository;
    private final CursoRepository cursoRepository;

    public AlunoController(AlunoRepository alunoRepository, CursoRepository cursoRepository) {
        this.alunoRepository = alunoRepository;
        this.cursoRepository = cursoRepository;
    }

    @GetMapping("/criar")
    public String formularioCriarAluno(Aluno aluno, Model model) {
        model.addAttribute("cursos", cursoRepository.findAll());
        return "alunos/add-aluno";
    }
    
    @PostMapping("/salvar")
    public String addAluno(@Valid Aluno aluno, BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "alunos/add-aluno";
        }
        Curso curso = cursoRepository.findByNome(aluno.getNomeCurso()).getFirst();
        aluno.setCurso(curso);

        alunoRepository.save(aluno);
        return "redirect:/alunos";
    }

    
    public Iterable<Aluno> saveAllAlunos(Iterable<Aluno> alunos) {
        for (Aluno aluno : alunos) {
            String nomeCurso = aluno.getCurso().getNome();
            Curso curso = cursoRepository.findByNome(nomeCurso).getFirst();
            aluno.setCurso(curso);
            alunoRepository.save(aluno);
        }
        return alunos;
    }

    @GetMapping
    public String listaAlunos(Model model) {
        model.addAttribute("alunos", alunoRepository.findAll());
        return "alunos/list-aluno";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditarAluno(@PathVariable("id") int id, Model model) {
        Aluno aluno = alunoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        model.addAttribute("aluno", aluno);
        model.addAttribute("cursos", cursoRepository.findAll());
        return "alunos/update-aluno";
    }

    @PostMapping("/atualizar/{id}")
    public String atualizarAluno(@PathVariable("id") int id, @Valid Aluno aluno, BindingResult result, Model model) {
        if (result.hasErrors()) {
            aluno.setId(id);
            return "alunos/update-aluno";
        }

        alunoRepository.save(aluno);
        return "redirect:/alunos";
    }

    @GetMapping("/deletar/{id}")
    public String deletarAluno(@PathVariable("id") int id, Model model) {
        Aluno aluno = alunoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        alunoRepository.delete(aluno);
        return "redirect:/alunos";
    }
}
