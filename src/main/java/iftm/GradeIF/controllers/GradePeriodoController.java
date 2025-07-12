package iftm.GradeIF.controllers;

import iftm.GradeIF.models.*;
import iftm.GradeIF.repositories.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import iftm.GradeIF.repositories.CursoRepository;
import iftm.GradeIF.repositories.DisciplinaRepository;
import iftm.GradeIF.repositories.GradePeriodoRepository;
import jakarta.transaction.Transactional;

import java.util.*;

@Controller
@RequestMapping("/grades-periodos")
public class GradePeriodoController {

    private final GradePeriodoRepository gradePeriodoRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final CursoRepository cursoRepository;
    private final UsuarioRepository usuarioRepository;
    public GradePeriodoController(GradePeriodoRepository gradePeriodoRepository, CursoRepository cursoRepository, DisciplinaRepository disciplinaRepository, UsuarioRepository usuarioRepository) {
        this.gradePeriodoRepository = gradePeriodoRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.cursoRepository = cursoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public String listaGradePeriodos(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<GradePeriodo> grades;
        if(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("Admin"))){
            grades = gradePeriodoRepository.findAll(Sort.by("curso", "periodo"));
        }else{

            grades = gradePeriodoRepository.findAllByConfirmada(true, Sort.by("curso", "periodo"));
            grades.sort(Comparator.comparing(GradePeriodo::getNomeCurso));
        }
        model.addAttribute("gradePeriodos", grades);
        
        return "grades-periodos/list-grades";
    }

    @GetMapping("/ppc")
    public String listaPpcCursoAluno(@AuthenticationPrincipal UserDetails userDetails){
        Usuario usuario = usuarioRepository.findByLogin(userDetails.getUsername()).get();

        return "redirect:/grades-periodos/curso/" + usuario.getAluno().getCurso().getId();
    }

    @GetMapping("/curso/{idCurso}")
    public String listaPpcCurso(Model model, @PathVariable int idCurso) {
        Curso curso = cursoRepository.findById(idCurso).get();

        List<GradePeriodo> grades = gradePeriodoRepository.findByCurso(curso, Sort.by("periodo"));
        List<String> nomesDisciplinas = new ArrayList<>();
        for(GradePeriodo grade : grades){
            for(Disciplina disciplina : grade.getDisciplinas()){
                nomesDisciplinas.add(disciplina.getNome());
            }
        }
        Map<String, String> coresDisciplinas = getDisciplinasCoresByCurso(curso);

        model.addAttribute("gradePeriodos", grades);
        model.addAttribute("disciplinas", nomesDisciplinas);
        model.addAttribute("coresDisciplinas", coresDisciplinas);
        return "grades-periodos/list-grades-curso";
    }

    @GetMapping("/criar")
    public String formularioCriarGradePeriodo(GradePeriodo gradePeriodo, Model model) {
        model.addAttribute("cursos", cursoRepository.findAll());
        return "grades-periodos/add-grade";
    }

    @PostMapping("/salvar")
    public String addGradePeriodo(@Valid GradePeriodo gradePeriodo, BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "grades-periodos/add-grade";
        }
        Curso curso = cursoRepository.findByNome(gradePeriodo.getNomeCurso()).getFirst();
        gradePeriodo.setCurso(curso);

        gradePeriodoRepository.save(gradePeriodo);
        return "redirect:/grades-periodos";
    }

    @GetMapping("/visualizar/{id}")
    public String formularioVisualizarGradePeriodo(@PathVariable("id") int id, Model model) {
        GradePeriodo gradePeriodo = gradePeriodoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        List<DisciplinaHorario> discHorarios = new ArrayList<>();
        List<Disciplina> disciplinas = disciplinaRepository.findAll();
        DisciplinaController.getHorariosDisciplinas(discHorarios, disciplinas, gradePeriodo.getDisciplinas());

        model.addAttribute("coresDisciplinas", gradePeriodo.getCoresDisciplinas());
        model.addAttribute("discHorarios", discHorarios);
        model.addAttribute("gradePeriodo", gradePeriodo);
        model.addAttribute("curso", gradePeriodo.getCurso());
        return "grades-periodos/view-grade";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditarGradePeriodo(@PathVariable("id") int id, Model model) {
        GradePeriodo gradePeriodo = gradePeriodoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        List<DisciplinaHorario> discHorarios = new ArrayList<>();
        List<Disciplina> disciplinas = disciplinaRepository.findAll(Sort.by("nome"));

        List<GradePeriodo> gradesAnterioresCurso = gradePeriodoRepository.findByCurso(gradePeriodo.getCurso());
        for(GradePeriodo gradeAnterior : gradesAnterioresCurso){
            List<Disciplina> disciplinasAnteriores = gradeAnterior.getDisciplinas();
            for(Disciplina disciplinaAnterior : disciplinasAnteriores){
                disciplinas.remove(disciplinaAnterior);
            }
        }

        DisciplinaController.getHorariosDisciplinas(discHorarios, disciplinas, gradePeriodo.getDisciplinas());

        model.addAttribute("coresDisciplinas", gradePeriodo.getCoresDisciplinas());
        model.addAttribute("discHorarios", discHorarios);
        model.addAttribute("gradePeriodo", gradePeriodo);
        model.addAttribute("curso", gradePeriodo.getCurso());
        model.addAttribute("disciplinas", disciplinas);
        return "grades-periodos/edit-grade";
    }

    @PostMapping("/editar/{id}/add")
    public String formularioAdicionarDisciplinaGrade(@PathVariable("id") int id, @ModelAttribute GradePeriodo gradePeriodo, BindingResult result, Model model) {
        if (result.hasErrors() || gradePeriodo.getIdDiscSelecionada() == null) {
            System.out.println(result.getAllErrors());
            return "grades-periodos/edit-grade";
        }

        GradePeriodo gradeExistente = gradePeriodoRepository.findById(id).get();
        List<Disciplina> discRestantes = disciplinaRepository.findAll();
        Disciplina disciplina = disciplinaRepository.findById(gradePeriodo.getIdDiscSelecionada()).get();
        List<Disciplina> preRequisitos = disciplina.getPreRequisitos();

        Curso curso = cursoRepository.findById(gradeExistente.getCurso().getId()).get();

        List<Disciplina> disciplinasPassadas = getDisciplinasCurso(curso);
        discRestantes.removeAll(disciplinasPassadas);

        String preReqFaltando = "";
        for(Disciplina preRequisito : preRequisitos){
            Boolean encontrado = false;
            if(disciplinasPassadas.contains(preRequisito)){
                encontrado = true;
            }
            if(!encontrado){
                if(!preReqFaltando.isEmpty()){
                    preReqFaltando += ", ";
                }
                preReqFaltando += preRequisito.getCodigo();
            }
        };

        List<Disciplina> listDisciplinas = gradeExistente.getDisciplinas();
        Boolean repetida = false;
        for (Disciplina tempDisc : listDisciplinas) {
            if(tempDisc.getId() == disciplina.getId()){
                repetida = true;
            }
        }
        if(!repetida && preReqFaltando.isEmpty()){
            disciplina.subtraiVaga();
            listDisciplinas.add(disciplina);
        }
        gradeExistente.setDisciplinas(listDisciplinas);
        gradeExistente.setConfirmada(false);

        String corDisciplina = gradePeriodo.getCorDisciplina();
        gradeExistente.addCorDisciplina(disciplina.getNome(), corDisciplina);

        gradePeriodoRepository.saveAndFlush(gradeExistente);

        List<DisciplinaHorario> discHorarios = new ArrayList<>();
        for (Disciplina tempDisc : gradeExistente.getDisciplinas()) {
            DisciplinaHorario discHorario = new DisciplinaHorario();
            List<Horario> horarios = new ArrayList<>();
            for(Horario tempHorario : tempDisc.getHorarios()){
                horarios.add(tempHorario);
            }
            discHorario.setCodigo(tempDisc.getCodigo());
            discHorario.setNome(tempDisc.getNome());
            discHorario.setHorarios(horarios);
            discHorarios.add(discHorario);
            if(!preReqFaltando.isEmpty()){
                discHorario.setPreReqCumpridos(false);
            }

            discRestantes.remove(tempDisc);
        }

        model.addAttribute("coresDisciplinas", gradeExistente.getCoresDisciplinas());
        model.addAttribute("preRequisitos", preReqFaltando);
        model.addAttribute("discHorarios", discHorarios);
        model.addAttribute("gradePeriodo", gradeExistente);
        model.addAttribute("curso", gradeExistente.getCurso());
        model.addAttribute("disciplinas", discRestantes);

        return "grades-periodos/edit-grade";
    }

    @PostMapping("/editar/{idGrade}/remove/{idDisciplina}")
    public String formularioRemoverDisciplinaGrade(@PathVariable("idGrade") int idGrade, @PathVariable("idDisciplina") int idDisciplina, @ModelAttribute GradePeriodo gradePeriodo, BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "grades-periodos/edit-grade";
        }

        GradePeriodo gradeExistente = gradePeriodoRepository.findById(idGrade).get();

        Disciplina discRemovida = disciplinaRepository.findById(idDisciplina).get();

        List<Disciplina> discRestantes = disciplinaRepository.findAll();

        List<Disciplina> listDisciplinas = gradeExistente.getDisciplinas();

        listDisciplinas.remove(discRemovida);
        discRemovida.revogaVaga();

        gradeExistente.setDisciplinas(listDisciplinas);

        gradePeriodoRepository.saveAndFlush(gradeExistente);

        List<DisciplinaHorario> discHorarios = new ArrayList<>();
        for (Disciplina tempDisc : gradeExistente.getDisciplinas()) {
            DisciplinaHorario discHorario = new DisciplinaHorario();
            List<Horario> horarios = new ArrayList<>();
            for(Horario tempHorario : tempDisc.getHorarios()){
                horarios.add(tempHorario);
            }
            discHorario.setCodigo(tempDisc.getCodigo());
            discHorario.setNome(tempDisc.getNome());
            discHorario.setHorarios(horarios);
            discHorarios.add(discHorario);

            discRestantes.remove(tempDisc);
        }

        model.addAttribute("discHorarios", discHorarios);
        model.addAttribute("gradePeriodo", gradeExistente);
        model.addAttribute("curso", gradeExistente.getCurso());
        model.addAttribute("disciplinas", discRestantes);

        return "redirect:/grades-periodos/editar/" + idGrade;
    }

    @PostMapping("/editar/{idGrade}/confirmar")
    public String confirmaGrade(@PathVariable("idGrade") int idGrade, @ModelAttribute GradePeriodo gradePeriodo, BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "grades-periodos/edit-grade";
        }

        GradePeriodo gradeExistente = gradePeriodoRepository.findById(idGrade).get();
        gradeExistente.setConfirmada(true);

        gradePeriodoRepository.saveAndFlush(gradeExistente);

        return "redirect:/grades-periodos";
    }

    @Transactional
    public Iterable<GradePeriodo> saveAllGradePeriodos(Iterable<GradePeriodo> gradePeriodos) {
        for (GradePeriodo gradePeriodo : gradePeriodos) {
            String nomeCurso = gradePeriodo.getNomeCurso();
            Curso curso = cursoRepository.findByNome(nomeCurso).getFirst();
            gradePeriodo.setCurso(curso);
            gradePeriodoRepository.save(gradePeriodo);
        }
        return gradePeriodos;
    }

    @GetMapping("/deletar/{id}")
    public String deletarGradePeriodo(@PathVariable("id") int id, Model model) {
        GradePeriodo gradePeriodo = gradePeriodoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        gradePeriodoRepository.delete(gradePeriodo);
        return "redirect:/grades-periodos";
    }

    private List<Disciplina> getDisciplinasCurso(Curso curso){
        List<GradePeriodo> gradesAnterioresCurso = gradePeriodoRepository.findByCurso(curso);
        List<Disciplina> disciplinasPresentes = new ArrayList<>();
        for(GradePeriodo gradeAnterior : gradesAnterioresCurso){
            disciplinasPresentes.addAll(gradeAnterior.getDisciplinas());
        }
        return disciplinasPresentes;
    }

    public void getPeriodosDisciplinas(List<Disciplina> disciplinas, Curso curso){
        List<GradePeriodo> gradesCurso = gradePeriodoRepository.findByCurso(curso);
        for(GradePeriodo grade : gradesCurso){
            for(Disciplina disciplina : grade.getDisciplinas()){
                disciplina.setPeriodo(grade.getPeriodo());
            }
        }
        disciplinas.sort(Comparator.comparing(Disciplina::getPeriodo));
    }

    public Map<String, String> getDisciplinasCoresByCurso(Curso curso){
        List<GradePeriodo> gradesCurso = gradePeriodoRepository.findByCurso(curso);
        Map<String, String> disciplinasCores = new HashMap<>();
        for(GradePeriodo grade : gradesCurso){
            disciplinasCores.putAll(grade.getCoresDisciplinas());
        }
        return disciplinasCores;
    }
}
