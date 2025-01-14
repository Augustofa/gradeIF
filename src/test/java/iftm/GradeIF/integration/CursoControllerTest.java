package iftm.GradeIF.integration;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import iftm.GradeIF.controllers.CursoController;
import iftm.GradeIF.models.Curso;
import iftm.GradeIF.repositories.CursoRepository;

import java.util.Arrays;
import java.util.Optional;

class CursoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CursoRepository cursoRepository;

    @InjectMocks
    private CursoController cursoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cursoController).build();
    }

    @Test
    void testListaCursos() throws Exception {
        Curso curso1 = new Curso();
        curso1.setId(1);
        curso1.setNome("Curso A");

        Curso curso2 = new Curso();
        curso2.setId(2);
        curso2.setNome("Curso B");

        when(cursoRepository.findAll()).thenReturn(Arrays.asList(curso1, curso2));

        mockMvc.perform(get("/cursos"))
                .andExpect(status().isOk())
                .andExpect(view().name("cursos/list-curso"))
                .andExpect(model().attributeExists("cursos"));

        verify(cursoRepository, times(1)).findAll();
    }

    @Test
    void testDeletarCurso() throws Exception {
        Curso curso = new Curso();
        curso.setId(1);
        curso.setNome("Curso A");

        when(cursoRepository.findById(1)).thenReturn(Optional.of(curso));

        mockMvc.perform(get("/cursos/deletar/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cursos"));

        verify(cursoRepository, times(1)).delete(curso);
    }
}