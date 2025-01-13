package iftm.GradeIF;

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import iftm.GradeIF.controllers.GradePeriodoController;
import iftm.GradeIF.models.Aluno;
import iftm.GradeIF.models.Curso;
import iftm.GradeIF.models.Disciplina;
import iftm.GradeIF.models.GradePeriodo;
import iftm.GradeIF.models.Horario;
import iftm.GradeIF.models.Professor;
import iftm.GradeIF.repositories.AlunoRepository;
import iftm.GradeIF.repositories.CursoRepository;
import iftm.GradeIF.repositories.DisciplinaRepository;
import iftm.GradeIF.repositories.GradeAlunoRepository;
import iftm.GradeIF.repositories.GradePeriodoRepository;
import iftm.GradeIF.repositories.HorarioRepository;
import iftm.GradeIF.repositories.ProfessorRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;

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
	private final GradePeriodoRepository repositoryGradePeriodo;
	private final GradeAlunoRepository repositoryGradeAlunos;

	private final ObjectMapper objectMapper;
	private final EntityManager entityManager;

	@SuppressWarnings("unused")
	@Autowired
	private final EntityManagerFactory entityManagerFactory;

	public GradeIfApplication(AlunoRepository repositoryAlunos, ObjectMapper objectMapper, CursoRepository repositoryCursos, ProfessorRepository repositoryProfessores, HorarioRepository repositoryHorarios, DisciplinaRepository repositoryDisciplinas, EntityManager entityManager, EntityManagerFactory entityManagerFactory, GradePeriodoRepository repositoryGradePeriodo, GradeAlunoRepository repositoryGradeAlunos) {
		this.repositoryAlunos = repositoryAlunos;
		this.repositoryCursos = repositoryCursos;
		this.repositoryProfessores = repositoryProfessores;
		this.repositoryHorarios = repositoryHorarios;
		this.repositoryDisciplinas = repositoryDisciplinas;
		this.repositoryGradePeriodo = repositoryGradePeriodo;
		this.repositoryGradeAlunos = repositoryGradeAlunos;
		this.objectMapper = objectMapper;
		this.entityManager = entityManager;
		this.entityManagerFactory = entityManagerFactory;
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {
        System.out.println("Carregando dados...");

		// Metamodel metamodel = entityManager.getMetamodel();
		// EntityType<Aluno> alunoEntityType = metamodel.entity(Aluno.class);
		// Set<SingularAttribute<? super Aluno, ?>> attributes = alunoEntityType.getSingularAttributes();

		// System.out.println("Attributes of Aluno:");
		// for (SingularAttribute<? super Aluno, ?> attribute : attributes) {
		// 	System.out.println(attribute.getName());
		// }
		
		// repositoryCursos.deleteAll();
		if(repositoryCursos.count() == 0) {
			try (InputStream inputStream = TypeReference.class.getResourceAsStream("/data/cursos.json")) {
				repositoryCursos.saveAll(objectMapper.readValue(inputStream, new TypeReference<List<Curso>>(){}));
			}
		}
		// repositoryAlunos.deleteAll();
		if(repositoryAlunos.count() == 0) {
			try (InputStream inputStream = TypeReference.class.getResourceAsStream("/data/alunos.json")) {
				// repositoryAlunos.saveAll(objectMapper.readValue(inputStream,new TypeReference<List<Aluno>>(){}));
				AlunoController alunoController = new AlunoController(repositoryAlunos, repositoryCursos);
				alunoController.saveAllAlunos(objectMapper.readValue(inputStream, new TypeReference<List<Aluno>>(){}));
			}
		}
		// repositoryProfessores.deleteAll();
		if(repositoryProfessores.count() == 0) {
			try (InputStream inputStream = TypeReference.class.getResourceAsStream("/data/professores.json")) {
				repositoryProfessores.saveAllAndFlush(objectMapper.readValue(inputStream, new TypeReference<List<Professor>>(){}));
			}
		}
		// repositoryHorarios.deleteAll();
		if(repositoryHorarios.count() == 0) {
			try (InputStream inputStream = TypeReference.class.getResourceAsStream("/data/horarios.json")) {
				repositoryHorarios.saveAllAndFlush(objectMapper.readValue(inputStream, new TypeReference<List<Horario>>(){}));
			}
		}
		
		// repositoryDisciplinas.deleteAll();
		if(repositoryDisciplinas.count() == 0) {
			try (InputStream inputStream = TypeReference.class.getResourceAsStream("/data/disciplinas.json")) {
				DisciplinaController disciplinaController = new DisciplinaController(repositoryDisciplinas, repositoryProfessores, repositoryHorarios, entityManager);
				disciplinaController.saveAllDisciplinas(objectMapper.readValue(inputStream, new TypeReference<List<Disciplina>>(){}));
			}
		}
		// repositoryGradePeriodo.deleteAll();
		if(repositoryGradePeriodo.count() == 0) {
			try (InputStream inputStream = TypeReference.class.getResourceAsStream("/data/gradePeriodos.json")) {
				GradePeriodoController gradePeriodoController = new GradePeriodoController(repositoryGradePeriodo, repositoryCursos, repositoryDisciplinas, entityManager);
				gradePeriodoController.saveAllGradePeriodos(objectMapper.readValue(inputStream, new TypeReference<List<GradePeriodo>>(){}));
			}
		}

		// repositoryGradeAlunos.deleteAll();
    }
}
