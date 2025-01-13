package iftm.GradeIF.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import iftm.GradeIF.models.Aluno;
import iftm.GradeIF.models.Disciplina;
import iftm.GradeIF.models.GradeAluno;
import iftm.GradeIF.models.GradeForm;
import iftm.GradeIF.repositories.AlunoRepository;
import iftm.GradeIF.repositories.DisciplinaRepository;
import iftm.GradeIF.repositories.GradeAlunoRepository;
import iftm.GradeIF.repositories.GradeFormRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/grades-alunos")
public class GradeAlunoController {
    private final GradeAlunoRepository gradeAlunoRepository;
    private final AlunoRepository alunoRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final EntityManager entityManager;
    private final GradeFormRepository gradeFormRepository;

    public GradeAlunoController(GradeAlunoRepository gradeAlunoRepository, AlunoRepository alunoRepository, DisciplinaRepository disciplinaRepository, GradeFormRepository gradeFormRepository, EntityManager entityManager) {
        this.gradeAlunoRepository = gradeAlunoRepository;
        this.alunoRepository = alunoRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.gradeFormRepository = gradeFormRepository;
        this.entityManager = entityManager;
    }

    @GetMapping
    public String listaGradeAlunos(Model model) {
        model.addAttribute("gradesAlunos", gradeAlunoRepository.findAll());
        return "grades-alunos/list-grades";
        }

    @GetMapping("/criar")
    public String formularioCriarGradeAluno(GradeAluno gradeAluno, Model model) {
        model.addAttribute("alunos", alunoRepository.findAll());
        return "grades-alunos/add-grade";
    }
        
    @PostMapping("/salvar")
    public String addGradeAluno(@Valid GradeAluno gradeAluno, BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "grades-alunos/add-grade-aluno";
        }
        Aluno aluno = alunoRepository.findByNome(gradeAluno.getNomeAluno()).getFirst();
        gradeAluno.setAluno(aluno);
        gradeAluno.setPeriodo("2025.1");

        gradeAlunoRepository.save(gradeAluno);
        return "redirect:/grades-alunos";
    }

    @Transactional
    public Iterable<GradeAluno> saveAllGradeAlunos(Iterable<GradeAluno> gradeAlunos) {
        for (GradeAluno gradeAluno : gradeAlunos) {
            String nomeAluno = gradeAluno.getNomeAluno();
            Aluno aluno = alunoRepository.findByNome(nomeAluno).getFirst();
            System.out.println("Encontrado: " + aluno.toString());
            gradeAluno.setAluno(aluno);
            gradeAlunoRepository.save(gradeAluno);
        }
        return gradeAlunos;
        }

    @GetMapping("/editar/{id}")
        public String formularioEditarGradeAluno(@PathVariable("id") int id, Model model) {
        GradeAluno gradeAluno = gradeAlunoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));

        gradeFormRepository.findById(gradeAluno.getId());
        GradeForm gradeForm = new GradeForm();
        gradeForm.setNomeAluno(gradeAluno.getAluno().getNome());

        model.addAttribute("gradeForm", gradeForm);
        model.addAttribute("gradeAluno", gradeAluno);
        model.addAttribute("aluno", gradeAluno.getAluno());
        model.addAttribute("disciplinas", disciplinaRepository.findAll());
        return "grades-alunos/edit-grade";
        }

    @PostMapping("/editar/{id}/add")
    public String formularioAdicionarDisciplinaGrade(@PathVariable("id") int id, @ModelAttribute GradeForm gradeForm, BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "grades-alunos/edit-grade";
        }

        GradeAluno gradeAluno = gradeAlunoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        Disciplina disciplina = disciplinaRepository.findById(Integer.parseInt(gradeForm.getIdDisciplina())).orElseThrow(() -> new IllegalArgumentException("ID de disciplina inválido: " + gradeForm.getIdDisciplina()));
        
        gradeForm.getDisciplinas().add(disciplina);
        System.out.println("Form: " + gradeForm.getDisciplinas());
        gradeAlunoRepository.save(gradeAluno);


        model.addAttribute("gradeForm", gradeForm);
        model.addAttribute("gradeAluno", gradeAluno);
        model.addAttribute("aluno", gradeAluno.getAluno());
        model.addAttribute("disciplinas", disciplinaRepository.findAll());

        return "grades-alunos/edit-grade";
    }

        @GetMapping("/deletar/{id}")
        public String deletarGradeAluno(@PathVariable("id") int id, Model model) {
        GradeAluno gradeAluno = gradeAlunoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        gradeAlunoRepository.delete(gradeAluno);
        return "redirect:/grades-alunos";
    }
}
