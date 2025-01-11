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

import iftm.GradeIF.controllers.AlunoController;
import iftm.GradeIF.controllers.DisciplinaController;
import iftm.GradeIF.models.Aluno;
import iftm.GradeIF.models.Curso;
import iftm.GradeIF.models.Disciplina;
import iftm.GradeIF.models.Horario;
import iftm.GradeIF.models.Professor;
import iftm.GradeIF.repositories.AlunoRepository;
import iftm.GradeIF.repositories.CursoRepository;
import iftm.GradeIF.repositories.DisciplinaRepository;
import iftm.GradeIF.repositories.HorarioRepository;
import iftm.GradeIF.repositories.ProfessorRepository;

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
	private final ProfessorRepository repositoryProfessores;
	private final HorarioRepository repositoryHorarios;
	private final DisciplinaRepository repositoryDisciplinas;

    private final ObjectMapper objectMapper;

    public GradeIfApplication(AlunoRepository repositoryAlunos, ObjectMapper objectMapper, CursoRepository repositoryCursos, ProfessorRepository repositoryProfessores, HorarioRepository repositoryHorarios, DisciplinaRepository repositoryDisciplinas) {
        this.repositoryAlunos = repositoryAlunos;
		this.repositoryCursos = repositoryCursos;
		this.repositoryProfessores = repositoryProfessores;
		this.repositoryHorarios = repositoryHorarios;
		this.repositoryDisciplinas = repositoryDisciplinas;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Carregando dados...");
		if(repositoryCursos.count() == 0) {
			try (InputStream inputStream = TypeReference.class.getResourceAsStream("/data/cursos.json")) {
				repositoryCursos.saveAll(objectMapper.readValue(inputStream, new TypeReference<List<Curso>>(){}));
			}
		}
		if(repositoryAlunos.count() == 0) {
			try (InputStream inputStream = TypeReference.class.getResourceAsStream("/data/alunos.json")) {
				// repositoryAlunos.saveAll(objectMapper.readValue(inputStream,new TypeReference<List<Aluno>>(){}));
				AlunoController alunoController = new AlunoController(repositoryAlunos, repositoryCursos);
				alunoController.saveAllAlunos(objectMapper.readValue(inputStream, new TypeReference<List<Aluno>>(){}));
			}
		}
		if(repositoryCursos.count() == 0) {
			try (InputStream inputStream = TypeReference.class.getResourceAsStream("/data/cursos.json")) {
				repositoryCursos.saveAll(objectMapper.readValue(inputStream, new TypeReference<List<Curso>>(){}));
			}
		}
		if(repositoryProfessores.count() == 0) {
			try (InputStream inputStream = TypeReference.class.getResourceAsStream("/data/professores.json")) {
				repositoryProfessores.saveAll(objectMapper.readValue(inputStream, new TypeReference<List<Professor>>(){}));
			}
		}
		if(repositoryHorarios.count() == 0) {
			try (InputStream inputStream = TypeReference.class.getResourceAsStream("/data/horarios.json")) {
				repositoryHorarios.saveAll(objectMapper.readValue(inputStream, new TypeReference<List<Horario>>(){}));
			}
		}
		if(repositoryDisciplinas.count() == 0) {
			try (InputStream inputStream = TypeReference.class.getResourceAsStream("/data/disciplinas.json")) {
				DisciplinaController disciplinaController = new DisciplinaController(repositoryDisciplinas, repositoryProfessores, repositoryHorarios);
				disciplinaController.saveAllDisciplinas(objectMapper.readValue(inputStream, new TypeReference<List<Disciplina>>(){}));
			}
		}
    }
}
