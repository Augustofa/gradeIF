package iftm.GradeIF.integration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import iftm.GradeIF.models.Curso;
import iftm.GradeIF.repositories.CursoRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CursoIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private CursoRepository cursoRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    @SuppressWarnings("deprecation")
    @Test
    void testFullIntegration() {
        Curso curso = new Curso();
        curso.setNome("TesteIntegração");
        curso.setDescricao("Descrição do Teste");
        curso.setQuantSemestres(6);

        cursoRepository.save(curso);

        String url = "http://localhost:" + port + "/cursos";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).contains("TesteIntegração");
    }
}
