package iftm.GradeIF.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import iftm.GradeIF.models.Matricula;
import iftm.GradeIF.repositories.DisciplinaRepository;
import iftm.GradeIF.repositories.GradeAlunoRepository;
import iftm.GradeIF.repositories.GradePeriodoRepository;
import iftm.GradeIF.repositories.MatriculaRepository;
import jakarta.persistence.EntityManager;

@Controller
@RequestMapping("/matriculas")
public class MatriculaController {
    private final MatriculaRepository matriculaRepository;
    public MatriculaController(MatriculaRepository matriculaRepository, DisciplinaRepository disciplinaRepository, GradeAlunoRepository gradeAlunoRepository, GradePeriodoRepository gradePeriodoRepository, EntityManager entityManager) {
        this.matriculaRepository = matriculaRepository;
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
