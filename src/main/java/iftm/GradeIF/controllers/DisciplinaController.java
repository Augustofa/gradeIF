package iftm.GradeIF.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import iftm.GradeIF.models.Disciplina;
import iftm.GradeIF.models.Horario;
import iftm.GradeIF.models.Professor;
import iftm.GradeIF.repositories.DisciplinaRepository;
import iftm.GradeIF.repositories.HorarioRepository;
import iftm.GradeIF.repositories.ProfessorRepository;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/disciplinas")
public class DisciplinaController {

    private final DisciplinaRepository disciplinaRepository;
    private final ProfessorRepository professorRepository;
    private final HorarioRepository horarioRepository;

    public DisciplinaController(DisciplinaRepository disciplinaRepository, ProfessorRepository professorRepository, HorarioRepository horarioRepository) {
        this.disciplinaRepository = disciplinaRepository;
        this.professorRepository = professorRepository;
        this.horarioRepository = horarioRepository;
    }

    // @GetMapping("/criar")
    // public String formularioCriarDisciplina(Disciplina disciplina, Model model) {
    //     model.addAttribute("professores", professorRepository.findAll());
    //     return "disciplinas/add-disciplina";
    // }
    
    // @PostMapping("/salvar")
    // public String addDisciplina(@Valid Disciplina disciplina, BindingResult result, Model model) {
    //     if (result.hasErrors()) {
    //         System.out.println(result.getAllErrors());
    //         return "disciplinas/add-disciplina";
    //     }
    //     Professor professor = professorRepository.findByCpf(disciplina.getProfessor().getCpf()).getFirst();
    //     disciplina.setProfessor(professor);

    //     disciplinaRepository.save(disciplina);
    //     return "redirect:/disciplinas";
    // }

    
    public Iterable<Disciplina> saveAllDisciplinas(Iterable<Disciplina> disciplinas) {
        for (Disciplina disciplina : disciplinas) {
            System.out.println("----------Antes: " + disciplina.toString());

            // String cpfProfessor = disciplina.getProfessor().getCpf();
            // Professor professor = professorRepository.findByCpf(cpfProfessor).get();
            // disciplina.setProfessor(professor);
            // System.out.println("-----------------Professor: " + disciplina.getProfessor().toString());
            
            // List<Disciplina> preRequisitos = new ArrayList<>();
            // if(!disciplina.getStrPreRequisitos().isEmpty()) {
            //     System.out.println("+++++++PreRequisitos: " + disciplina.getStrPreRequisitos());
            //     for (String codigo : disciplina.getStrPreRequisitos()) {
            //         System.out.println("===========Procurando disciplina: " + codigo);
            //         List<Disciplina> res = disciplinaRepository.findByCodigo(codigo);
            //         System.out.println("===========Resultado: " + res.toString());
                    // Disciplina preRequisito = disciplinaRepository.findByCodigo(codigo).getFirst();
                    // System.out.println("Disciplina encontrada: " + preRequisito.toString());
                    // preRequisitos.add(preRequisito);
            //     }
            // }
            // disciplina.setPreRequisitos(preRequisitos);
            
            // List<Disciplina> posRequisitos = new ArrayList<>();
            // if(!disciplina.getStrPosRequisitos().isEmpty()) {
            //     for (String codigo : disciplina.getStrPosRequisitos()) {
            //         Disciplina posRequisito = disciplinaRepository.findByCodigo(codigo).getFirst();
            //         posRequisitos.add(posRequisito);
            //     }
            // }
            // disciplina.setPosRequisitos(posRequisitos);

            List<Horario> horarios = new ArrayList<>();
            for (Horario horario : disciplina.getHorarios()) {
                List<Horario> tempHorarios = horarioRepository.findByCombinaDiaHora(horario.getCombinaDiaHora());
                if(tempHorarios.isEmpty()){
                    horarios.add(horario);
                } else {
                    horarios.add(tempHorarios.getFirst());
                }
            }
            disciplina.setHorarios(horarios);
            
            System.out.println("--------Depois: " + disciplina.toString());
            disciplinaRepository.save(disciplina);
        }
        return disciplinas;
    }

    @GetMapping
    public String listaDisciplinas(Model model) {
        model.addAttribute("disciplinas", disciplinaRepository.findAll());
        return "disciplinas/list-disciplina";
    }

    // @GetMapping("/editar/{id}")
    // public String formularioEditarDisciplina(@PathVariable("id") int id, Model model) {
    //     Disciplina disciplina = disciplinaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
    //     model.addAttribute("disciplina", disciplina);
    //     model.addAttribute("professores", professorRepository.findAll());
    //     return "disciplinas/update-disciplina";
    // }

    // @PostMapping("/atualizar/{id}")
    // public String atualizarDisciplina(@PathVariable("id") int id, @Valid Disciplina disciplina, BindingResult result, Model model) {
    //     if (result.hasErrors()) {
    //         disciplina.setId(id);
    //         return "disciplinas/update-disciplina";
    //     }

    //     disciplinaRepository.save(disciplina);
    //     return "redirect:/disciplinas";
    // }

    @GetMapping("/deletar/{id}")
    public String deletarDisciplina(@PathVariable("id") int id, Model model) {
        Disciplina disciplina = disciplinaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        disciplinaRepository.delete(disciplina);
        return "redirect:/disciplinas";
    }
}
