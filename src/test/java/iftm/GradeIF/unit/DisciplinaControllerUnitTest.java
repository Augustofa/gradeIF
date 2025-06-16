package iftm.GradeIF.unit;

import iftm.GradeIF.controllers.DisciplinaController;
import iftm.GradeIF.models.Disciplina;
import iftm.GradeIF.models.Professor;
import iftm.GradeIF.repositories.DisciplinaRepository;
import iftm.GradeIF.repositories.ProfessorRepository;
import iftm.GradeIF.repositories.HorarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

class DisciplinaControllerUnitTest {

    @InjectMocks
    private DisciplinaController disciplinaController;

    @Mock
    private DisciplinaRepository disciplinaRepository;

    @Mock
    private ProfessorRepository professorRepository;

    @Mock
    private HorarioRepository horarioRepository;

    @Mock
    private Model model;

    @Mock
    private BindingResult result;  

    private Professor professorMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        professorMock = new Professor();
        professorMock.setId(1);
        professorMock.setNome("Professor Teste");
        professorMock.setCpf("12345678900");

        when(professorRepository.findByCpf("12345678900")).thenReturn(java.util.Optional.of(professorMock));
    }

    @Test
    void testAddDisciplina() {
        Disciplina disciplina = new Disciplina();
        disciplina.setCodigo("DIS004");
        disciplina.setNome("Disciplina Teste Adicionar");
        disciplina.setProfessor(professorMock);

        when(result.hasErrors()).thenReturn(false);

        when(disciplinaRepository.save(any(Disciplina.class))).thenReturn(disciplina);

        String resultPage = disciplinaController.addDisciplina(disciplina, result, model);

        assertThat(resultPage).isEqualTo("redirect:/disciplinas");
        verify(disciplinaRepository, times(1)).save(disciplina);
        verify(professorRepository, times(1)).findByCpf("12345678900");  
    }

    @Test
    void testAddDisciplinaComErro() {
        Disciplina disciplina = new Disciplina();
        disciplina.setCodigo("DIS004");
        disciplina.setNome("Disciplina Teste Adicionar");
        disciplina.setNomeProfessor("Professor Teste");

        when(result.hasErrors()).thenReturn(true);

        String resultPage = disciplinaController.addDisciplina(disciplina, result, model);

        assertThat(resultPage).isEqualTo("disciplinas/add-disciplina");
        verify(disciplinaRepository, never()).save(disciplina); 
    }
}
