package iftm.GradeIF.controllers;

import java.awt.*;
import java.util.*;
import java.util.List;

import iftm.GradeIF.models.*;
import iftm.GradeIF.repositories.*;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
@DependsOn("gradePeriodoController")
@RequestMapping("/grades-alunos")
public class GradeAlunoController {
    private final GradeAlunoRepository gradeAlunoRepository;
    private final AlunoRepository alunoRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final UsuarioRepository usuarioRepository;
    private final GradePeriodoController gradePeriodoController;

    public GradeAlunoController(GradeAlunoRepository gradeAlunoRepository, AlunoRepository alunoRepository, DisciplinaRepository disciplinaRepository, GradeFormRepository gradeFormRepository, EntityManager entityManager, UsuarioRepository usuarioRepository, GradePeriodoController gradePeriodoController) {
        this.gradeAlunoRepository = gradeAlunoRepository;
        this.alunoRepository = alunoRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.usuarioRepository = usuarioRepository;
        this.gradePeriodoController = gradePeriodoController;
    }

    @GetMapping
    public String listaGradeAlunos(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = usuarioRepository.findByLogin(userDetails.getUsername()).get();
        if(usuario.getAluno() == null){
            model.addAttribute("gradesAlunos", gradeAlunoRepository.findAll(Sort.by("periodo").descending()));
        }else{
            List<GradeAluno> gradesAluno = gradeAlunoRepository.findByAluno(usuario.getAluno(), Sort.by("periodo").descending());
            model.addAttribute("gradesAlunos", gradesAluno);
        }

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
            return "grades-alunos/add-grade";
        }

        Aluno aluno = alunoRepository.findByNome(gradeAluno.getNomeAluno()).getFirst();
        gradeAluno.setAluno(aluno);

        List<GradeAluno> gradesExistentes = gradeAlunoRepository.findByAluno(aluno);
        boolean jaExiste = false;
        for(GradeAluno tempGrade : gradesExistentes){
            if(tempGrade.getPeriodo().equals(gradeAluno.getPeriodo())){
                jaExiste = true;
            }
        }
        if(!jaExiste){
            gradeAlunoRepository.save(gradeAluno);
        }
        return "redirect:/grades-alunos";
    }

    @Transactional
    public Iterable<GradeAluno> saveAllGradeAlunos(Iterable<GradeAluno> gradeAlunos) {
        for (GradeAluno gradeAluno : gradeAlunos) {
            String nomeAluno = gradeAluno.getNomeAluno();
            Aluno aluno = alunoRepository.findByNome(nomeAluno).getFirst();
            gradeAluno.setAluno(aluno);
            gradeAlunoRepository.save(gradeAluno);
        }
        return gradeAlunos;
    }

    @GetMapping("/visualizar/{id}")
    public String formularioVisualizarGradeAluno(@PathVariable("id") int id, Model model) {
        GradeAluno gradeAluno = gradeAlunoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        List<DisciplinaHorario> discHorarios = new ArrayList<>();
        List<Disciplina> discRestantes = disciplinaRepository.findAll();

        DisciplinaController.getHorariosDisciplinas(discHorarios, discRestantes, gradeAluno.getDisciplinas());

        model.addAttribute("coresDisciplinas", gradeAluno.getCoresDisciplinas());
        model.addAttribute("discHorarios", discHorarios);
        model.addAttribute("gradeAluno", gradeAluno);
        model.addAttribute("aluno", gradeAluno.getAluno());
        return "grades-alunos/view-grade";
    }

    @GetMapping("/editar/{id}")
        public String formularioEditarGradeAluno(@PathVariable("id") int id, Model model) {
        GradeAluno gradeAluno = gradeAlunoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        List<DisciplinaHorario> discHorarios = new ArrayList<>();

        List<Disciplina> disciplinasDisponiveis = disciplinaRepository.findAll();
        List<Disciplina> disciplinasCursadas = getDisciplinasPassadas(gradeAluno.getAluno());
        disciplinasDisponiveis.removeAll(disciplinasCursadas);

        DisciplinaController.getHorariosDisciplinas(discHorarios, disciplinasDisponiveis, gradeAluno.getDisciplinas());
        gradePeriodoController.getPeriodosDisciplinas(disciplinasDisponiveis, gradeAluno.getAluno().getCurso());

        model.addAttribute("coresDisciplinas", gradeAluno.getCoresDisciplinas());
        model.addAttribute("discHorarios", discHorarios);
        model.addAttribute("gradeAluno", gradeAluno);
        model.addAttribute("aluno", gradeAluno.getAluno());
        model.addAttribute("disciplinas", disciplinasDisponiveis);
        return "grades-alunos/edit-grade";
    }

    @PostMapping("/editar/{id}/add")
    public String formularioAdicionarDisciplinaGrade(@PathVariable("id") int id, @ModelAttribute GradeAluno gradeAluno, BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "grades-alunos/edit-grade";
        }

        GradeAluno gradeExistente = gradeAlunoRepository.findById(id).get();
        Aluno aluno = alunoRepository.findById(gradeExistente.getAluno().getId()).get();

        List<Disciplina> disciplinasDisponiveis = disciplinaRepository.findAll();
        List<Disciplina> disciplinasCursadas = getDisciplinasPassadas(aluno);
        disciplinasDisponiveis.removeAll(disciplinasCursadas);

        Disciplina disciplinaAdicionada = disciplinaRepository.findById(gradeAluno.getIdDiscSelecionada()).get();
        List<Disciplina> preRequisitos = disciplinaAdicionada.getPreRequisitos();

        String preReqFaltando = "";
        for(Disciplina preRequisito : preRequisitos) {
            if(!disciplinasCursadas.contains(preRequisito)) {
                preReqFaltando += preRequisito.getNome();
            }
        }

        Boolean repetida = gradeExistente.getDisciplinas().contains(disciplinaAdicionada);
        if(!repetida && preReqFaltando.isEmpty()){
            gradeExistente.getDisciplinas().add(disciplinaAdicionada);
        }

        gradeExistente.setConfirmada(false);

        String corDisciplina = gradeAluno.getCorDisciplina();
        gradeExistente.addCorDisciplina(disciplinaAdicionada.getNome(), corDisciplina);

        gradeAlunoRepository.saveAndFlush(gradeExistente);

        List<DisciplinaHorario> discHorarios = new ArrayList<>();
        for (Disciplina tempDisc : gradeExistente.getDisciplinas()) {
            DisciplinaHorario discHorario = new DisciplinaHorario();
            List<Horario> horarios = new ArrayList<>(tempDisc.getHorarios());
            discHorario.setCodigo(tempDisc.getCodigo());
            discHorario.setNome(tempDisc.getNome());
            discHorario.setHorarios(horarios);
            discHorarios.add(discHorario);
            if(!preReqFaltando.isEmpty()){
                discHorario.setPreReqCumpridos(false);
            }

            disciplinasDisponiveis.remove(tempDisc);
        }

        gradePeriodoController.getPeriodosDisciplinas(disciplinasDisponiveis, aluno.getCurso());

        model.addAttribute("coresDisciplinas", gradeExistente.getCoresDisciplinas());
        model.addAttribute("preRequisitos", preReqFaltando);
        model.addAttribute("discHorarios", discHorarios);
        model.addAttribute("gradeAluno", gradeExistente);
        model.addAttribute("aluno", gradeExistente.getAluno());
        model.addAttribute("disciplinas", disciplinasDisponiveis);

        return "grades-alunos/edit-grade";
    }

    @PostMapping("/editar/{id}/auto")
    public String montarGradeAutomatica(@PathVariable("id") int id, @ModelAttribute GradeAluno gradeAluno, BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "grades-alunos/edit-grade";
        }

        GradeAluno gradeExistente = gradeAlunoRepository.findById(id).get();
        Aluno aluno = alunoRepository.findById(gradeExistente.getAluno().getId()).get();

        List<Disciplina> disciplinasDisponiveis = disciplinaRepository.findAll();
        List<Disciplina> disciplinasCursadas = getDisciplinasPassadas(aluno);
        disciplinasDisponiveis.removeAll(disciplinasCursadas);

        gradePeriodoController.getPeriodosDisciplinas(disciplinasDisponiveis, aluno.getCurso());
        Set<Horario> horariosPreenchidos = new HashSet<>();
        List<Disciplina> disciplinasAdicionadas = new ArrayList<>();

        // Tenta adicionar disciplinas em ordem de periodo
        for(Iterator<Disciplina> it = disciplinasDisponiveis.iterator(); it.hasNext(); ){
            Disciplina disciplina = it.next();
            List<Disciplina> preRequisitos = disciplina.getPreRequisitos();
            if(checaPreRequisitosCumpridos(aluno, disciplina, disciplinasCursadas)
                    && checaHorariosLivres(disciplina, horariosPreenchidos)){
                disciplina.subtraiVaga();
                disciplinasAdicionadas.add(disciplina);
                horariosPreenchidos.addAll(disciplina.getHorarios());
                it.remove();
            }
        }

        gradeExistente.setDisciplinas(disciplinasAdicionadas);
        gradeExistente.setConfirmada(false);

        //TODO: Adicionar cores contidas na GradePeriodo
        gradeExistente.setCoresDisciplinas(gradePeriodoController.getDisciplinasCoresByCurso(aluno.getCurso()));
        var teste = gradeExistente.getCoresDisciplinas();

        gradeAlunoRepository.saveAndFlush(gradeExistente);

        List<DisciplinaHorario> horariosDisciplinas = new ArrayList<>();
        for (Disciplina disciplinaAdicionada : disciplinasAdicionadas) {
            DisciplinaHorario discHorario = new DisciplinaHorario();
            discHorario.setCodigo(disciplinaAdicionada.getCodigo());
            discHorario.setNome(disciplinaAdicionada.getNome());
            discHorario.setHorarios(disciplinaAdicionada.getHorarios());
            discHorario.setPreReqCumpridos(true);

            horariosDisciplinas.add(discHorario);
        }

        model.addAttribute("coresDisciplinas", gradeExistente.getCoresDisciplinas());
        model.addAttribute("preRequisitos", "");
        model.addAttribute("discHorarios", horariosDisciplinas);
        model.addAttribute("gradeAluno", gradeExistente);
        model.addAttribute("aluno", gradeExistente.getAluno());
        model.addAttribute("disciplinas", disciplinasDisponiveis);

        return "grades-alunos/edit-grade";
    }

    @PostMapping("/editar/{idGrade}/remove/{idDisciplina}")
    public String formularioRemoverDisciplinaGrade(@PathVariable("idGrade") int idGrade, @PathVariable("idDisciplina") int idDisciplina, @ModelAttribute GradeAluno gradeAluno, BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "grades-alunos/edit-grade";
        }
        
        GradeAluno gradeExistente = gradeAlunoRepository.findById(idGrade).get();

        Disciplina discRemovida = disciplinaRepository.findById(idDisciplina).get();

        List<Disciplina> discRestantes = disciplinaRepository.findAll();

        List<Disciplina> listDisciplinas = gradeExistente.getDisciplinas();

        listDisciplinas.remove(discRemovida);
        discRemovida.revogaVaga();
        
        gradeExistente.setDisciplinas(listDisciplinas);
        gradeExistente.setConfirmada(false);

        gradeAlunoRepository.saveAndFlush(gradeExistente);

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
        gradePeriodoController.getPeriodosDisciplinas(discRestantes, gradeExistente.getAluno().getCurso());

        model.addAttribute("discHorarios", discHorarios);
        model.addAttribute("gradeAluno", gradeExistente);
        model.addAttribute("aluno", gradeExistente.getAluno());
        model.addAttribute("disciplinas", discRestantes);

        return "redirect:/grades-alunos/editar/" + idGrade;
    }

    @PostMapping("/editar/{idGrade}/confirmar")
    public String confirmaGrade(@PathVariable("idGrade") int idGrade, @ModelAttribute GradeAluno gradeAluno, BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "grades-alunos/edit-grade";
        }
        
        GradeAluno gradeExistente = gradeAlunoRepository.findById(idGrade).get();
        gradeExistente.setConfirmada(true);
        
        gradeAlunoRepository.saveAndFlush(gradeExistente);

        return "redirect:/grades-alunos";
    }

    @GetMapping("/deletar/{id}")
    public String deletarGradeAluno(@PathVariable("id") int id, Model model) {
        GradeAluno gradeAluno = gradeAlunoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        gradeAlunoRepository.delete(gradeAluno);
        return "redirect:/grades-alunos";
    }

    private List<Disciplina> getDisciplinasPassadas(Aluno aluno){
        List<GradeAluno> gradesAnteriores = gradeAlunoRepository.findByAluno(aluno);
        List<Disciplina> disciplinasCursadas = new ArrayList<>();
        for(GradeAluno gradeAnterior : gradesAnteriores){
            disciplinasCursadas.addAll(gradeAnterior.getDisciplinas());
        }
        return disciplinasCursadas;
    }

    private boolean checaPreRequisitosCumpridos(Aluno aluno, Disciplina disciplina, List<Disciplina> disciplinasCursadas){
        List<Disciplina> preRequisitos = disciplina.getPreRequisitos();
        if(preRequisitos.isEmpty()){
            return true;
        }
        return disciplinasCursadas.containsAll(preRequisitos);
    }

    private boolean checaHorariosLivres(Disciplina disciplina, Set<Horario> horariosPreenchidos){
        for(Horario horario : disciplina.getHorarios()){
            if(horariosPreenchidos.contains(horario)){
                return false;
            }
        }
        return true;
    }
}
