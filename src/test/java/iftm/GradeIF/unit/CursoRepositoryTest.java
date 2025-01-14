package iftm.GradeIF.unit;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;

import iftm.GradeIF.models.Curso;
import iftm.GradeIF.repositories.CursoRepository;

@SpringBootTest
@AutoConfigureDataJpa
class CursoRepositoryTest {

    @Autowired
    private CursoRepository cursoRepository;

    @Test
    void testFindByNome() {
        Curso curso = new Curso();
        curso.setNome("TestCurso");
        cursoRepository.save(curso);

        List<Curso> encontrado = cursoRepository.findByNome("TestCurso");

        assertThat(encontrado).isNotEmpty();
        assertThat(encontrado.get(0).getNome()).isEqualTo("TestCurso");
    }

    @Test
    void testFindAll() {
        Curso curso1 = new Curso();
        curso1.setNome("Curso 1");
        
        Curso curso2 = new Curso();
        curso2.setNome("Curso 2");

        cursoRepository.save(curso1);
        cursoRepository.save(curso2);

        Iterable<Curso> cursos = cursoRepository.findAll();

        assertThat(cursos).isNotEmpty();

        assertThat(cursos).extracting(Curso::getNome).contains("Curso 1", "Curso 2");
    }
}
