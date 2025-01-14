package iftm.GradeIF.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import iftm.GradeIF.models.Disciplina;
import iftm.GradeIF.models.Professor;
import iftm.GradeIF.repositories.DisciplinaRepository;
import iftm.GradeIF.repositories.ProfessorRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DisciplinaControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Mock
    private ProfessorRepository professorRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    private Professor professorMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        professorMock = new Professor();
        professorMock.setId(1);
        professorMock.setNome("Professor Teste");
        professorMock.setCpf("12345678900");

        when(professorRepository.findByCpf("12345678900")).thenReturn(java.util.Optional.of(professorMock));
    }

    @SuppressWarnings("deprecation")
    @Test
    void testCreateDisciplina() {
        Disciplina disciplina = new Disciplina();
        disciplina.setCodigo("DIS002");
        disciplina.setNome("Disciplina Teste Criação");
        disciplina.setVagas(10);
        disciplina.setCreditos(4);
        disciplina.setProfessor(professorMock);

        disciplinaRepository.save(disciplina);

        String url = "http://localhost:" + port + "/disciplinas";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).contains("Disciplina Teste Criação");
    }

    @Test
    void testDeleteDisciplina() {
        Disciplina disciplina = new Disciplina();
        disciplina.setCodigo("DIS003");
        disciplina.setNome("Disciplina Teste Deleção");
        disciplina.setVagas(10);
        disciplina.setCreditos(4);
        disciplinaRepository.save(disciplina);

        String url = "http://localhost:" + port + "/disciplinas/deletar/" + disciplina.getId();
        restTemplate.delete(url);

        assertThat(disciplinaRepository.findById(disciplina.getId())).isEmpty();
    }
}
