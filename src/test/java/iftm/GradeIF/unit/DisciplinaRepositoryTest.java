package iftm.GradeIF.unit;

import iftm.GradeIF.models.Disciplina;
import iftm.GradeIF.repositories.DisciplinaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DisciplinaRepositoryTest {

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Test
    void testFindByCodigo() {
        Disciplina disciplina = new Disciplina();
        disciplina.setCodigo("DIS001");
        disciplina.setNome("Disciplina Teste");
        disciplinaRepository.save(disciplina);

        Disciplina resultado = disciplinaRepository.findByCodigo("DIS001").get(0);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getCodigo()).isEqualTo("DIS001");
    }
}
