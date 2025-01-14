package iftm.GradeIF.unit;

import iftm.GradeIF.models.Disciplina;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DisciplinaTest {

    @Test
    void testSubtraiVaga() {
        Disciplina disciplina = new Disciplina();
        disciplina.setVagas(10);

        boolean result = disciplina.subtraiVaga();

        assertThat(result).isTrue();
        assertThat(disciplina.getVagas()).isEqualTo(9);
    }

    @Test
    void testSubtraiVagaQuandoNaoHouverVagas() {
        Disciplina disciplina = new Disciplina();
        disciplina.setVagas(0);

        boolean result = disciplina.subtraiVaga();

        assertThat(result).isFalse();
        assertThat(disciplina.getVagas()).isEqualTo(0);
    }

    @Test
    void testRevogaVaga() {
        Disciplina disciplina = new Disciplina();
        disciplina.setVagas(0);

        disciplina.revogaVaga();

        assertThat(disciplina.getVagas()).isEqualTo(1);
    }
}
