package iftm.GradeIF.controllers;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import iftm.GradeIF.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import iftm.GradeIF.repositories.DisciplinaRepository;
import iftm.GradeIF.repositories.HorarioRepository;
import iftm.GradeIF.repositories.ProfessorRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/disciplinas")
public class DisciplinaController {

    private final DisciplinaRepository disciplinaRepository;

    private final ProfessorRepository professorRepository;

    private final HorarioRepository horarioRepository;

    @PersistenceContext
    private final EntityManager entityManager;

    public DisciplinaController(DisciplinaRepository disciplinaRepository, ProfessorRepository professorRepository, HorarioRepository horarioRepository, EntityManager entityManager) {
        this.disciplinaRepository = disciplinaRepository;
        this.professorRepository = professorRepository;
        this.horarioRepository = horarioRepository;
        this.entityManager = entityManager;
    }

    @GetMapping("/criar")
    public String formularioCriarDisciplina(Disciplina disciplina, Model model) {
        model.addAttribute("disciplinas", disciplinaRepository.findAll());
        model.addAttribute("professores", professorRepository.findAll());
        model.addAttribute("horarios", horarioRepository.findAll());
        return "disciplinas/add-disciplina";
    }
    
    @PostMapping("/salvar")
    public String addDisciplina(@Valid Disciplina disciplina, BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "disciplinas/add-disciplina";
        }

        Professor professor = professorRepository.findByNome(disciplina.getNomeProfessor()).orElse(null);
        disciplina.setProfessor(professor);

        List<Disciplina> preRequisitos = new ArrayList<>();
        if(!disciplina.getPreRequisitosNomes().isEmpty()) {
            for (String codigo : disciplina.getPreRequisitosNomes()) {
                Disciplina preRequisito = disciplinaRepository.findByNome(codigo).getFirst();
                preRequisitos.add(preRequisito);
            }
        }
        disciplina.setPreRequisitos(preRequisitos);
        
        List<Horario> horarios = new ArrayList<>();
        if(!disciplina.getHorariosNomes().isEmpty()) {
            for (String diaHora : disciplina.getHorariosNomes()) {
                List<Horario> horariosDia = horarioRepository.findByDia(diaHora.split(" ")[0]);
                for (Horario horario : horariosDia) {
                    if(horario.getHoraInicio().equals(LocalTime.parse(diaHora.split(" ")[2]))) {
                        horarios.add(horario);
                        break;
                    }
                }
            }
        }
        disciplina.setHorarios(horarios);

        disciplinaRepository.save(disciplina);
        return "redirect:/disciplinas";
    }

    @Transactional
    public Iterable<Disciplina> saveAllDisciplinas(Iterable<Disciplina> disciplinas) {
        for (Disciplina disciplina : disciplinas) {

            Professor professor = professorRepository.findByCpf(disciplina.getProfessor().getCpf()).orElse(null);
            disciplina.setProfessor(professor);
            
            List<Disciplina> preRequisitos = new ArrayList<>();
            if(!disciplina.getPreRequisitosNomes().isEmpty()) {
                for (String codigo : disciplina.getPreRequisitosNomes()) {
                    Disciplina preRequisito = disciplinaRepository.findByCodigo(codigo).getFirst();
                    preRequisitos.add(preRequisito);
                }
            }
            disciplina.setPreRequisitos(preRequisitos);
            
            List<Horario> horarios = new ArrayList<>();
            for (Horario horario : disciplina.getHorarios()) {
                List<Horario> tempDia = horarioRepository.findByDia(horario.getDia());
                for (Horario tempHorario : tempDia) {
                    if(tempHorario.getHoraInicio().equals(horario.getHoraInicio())) {
                        horarios.add(tempHorario);
                        break;
                    }
                }
            }
            disciplina.setHorarios(horarios);
            
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

    public static void getHorariosDisciplinas(List<DisciplinaHorario> discHorarios, List<Disciplina> discRestantes, List<Disciplina> disciplinasGrade) {
        for (Disciplina tempDisc : disciplinasGrade) {
            DisciplinaHorario discHorario = new DisciplinaHorario();
            List<Horario> horarios = new ArrayList<>(tempDisc.getHorarios());
            discHorario.setHorarios(horarios);
            discHorario.setCodigo(tempDisc.getCodigo());
            discHorario.setNome(tempDisc.getNome());
            discHorarios.add(discHorario);

            discRestantes.remove(tempDisc);
        }
    }
}
