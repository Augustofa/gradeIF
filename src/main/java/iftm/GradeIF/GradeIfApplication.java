package iftm.GradeIF;

import java.io.InputStream;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import iftm.GradeIF.models.Aluno;
import iftm.GradeIF.models.Curso;
import iftm.GradeIF.repositories.AlunoRepository;
import iftm.GradeIF.repositories.CursoRepository;

@EnableJpaRepositories
@ComponentScan("iftm.GradeIF.controllers")
@EntityScan("iftm.GradeIF.models")
@SpringBootApplication
public class GradeIfApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(GradeIfApplication.class, args);
	}

	private final AlunoRepository repositoryAlunos;
	private final CursoRepository repositoryCursos;
    private final ObjectMapper objectMapper;

    public GradeIfApplication(AlunoRepository repositoryAlunos, ObjectMapper objectMapper, CursoRepository repositoryCursos) {
        this.repositoryAlunos = repositoryAlunos;
		this.repositoryCursos = repositoryCursos;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Carregando dados...");
		if(repositoryAlunos.count() == 0) {
			try (InputStream inputStream = TypeReference.class.getResourceAsStream("/data/alunos.json")) {
				repositoryAlunos.saveAll(objectMapper.readValue(inputStream,new TypeReference<List<Aluno>>(){}));
			}
		}
		if(repositoryCursos.count() == 0) {
			try (InputStream inputStream = TypeReference.class.getResourceAsStream("/data/cursos.json")) {
				repositoryCursos.saveAll(objectMapper.readValue(inputStream, new TypeReference<List<Curso>>(){}));
			}
		}
    }
}
