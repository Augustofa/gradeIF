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
import iftm.GradeIF.models.Matricula;
import iftm.GradeIF.repositories.DisciplinaRepository;
import iftm.GradeIF.repositories.GradeAlunoRepository;
import iftm.GradeIF.repositories.GradePeriodoRepository;
import iftm.GradeIF.repositories.MatriculaRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/matriculas")
public class MatriculaController {
    private final MatriculaRepository matriculaRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final GradeAlunoRepository gradeAlunoRepository;
    private final GradePeriodoRepository gradePeriodoRepository;
    private final EntityManager entityManager;

    public MatriculaController(MatriculaRepository matriculaRepository, DisciplinaRepository disciplinaRepository, GradeAlunoRepository gradeAlunoRepository, GradePeriodoRepository gradePeriodoRepository, EntityManager entityManager) {
        this.matriculaRepository = matriculaRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.gradeAlunoRepository = gradeAlunoRepository;
        this.gradePeriodoRepository = gradePeriodoRepository;
        this.entityManager = entityManager;
    }

    @GetMapping
    public String listaMatriculas(Model model) {
        model.addAttribute("matriculas", matriculaRepository.findAll());
        return "matriculas/list-matriculas";
    }

    // @Transactional
    // public Iterable<Matricula> saveAllMatriculas(Iterable<Matricula> matriculas) {
    //     for (Matricula matricula : matriculas) {
    //         String nomeAluno = matricula.getNomeAluno();
    //         Aluno aluno = alunoRepository.findByNome(nomeAluno).getFirst();
    //         System.out.println("Encontrado: " + aluno.toString());
    //         matricula.setAluno(aluno);
    //         matriculaRepository.save(matricula);
    //     }
    //     return matriculas;
    // }

    @GetMapping("/deletar/{id}")
    public String deletarMatricula(@PathVariable("id") int id, Model model) {
        Matricula matricula = matriculaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        matriculaRepository.delete(matricula);
        return "redirect:/matriculas";
    }
}
