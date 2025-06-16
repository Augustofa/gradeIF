package iftm.GradeIF.controllers;

import iftm.GradeIF.models.Aluno;
import iftm.GradeIF.models.Usuario;
import iftm.GradeIF.repositories.AlunoRepository;
import iftm.GradeIF.services.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UsuarioController {
    private final IUsuarioService usuarioService;

    @Autowired
    private AlunoRepository alunoRepository;

    public UsuarioController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String login(){
        return "usuarios/loginUser";
    }

    @GetMapping("/register")
    public String registrar(){
        return "usuarios/registerUser";
    }

    @PostMapping("/saveUser")
    public String salvaUsuario(@ModelAttribute Usuario usuario, @ModelAttribute("cpf") String cpf, @RequestParam(value = "permissoes") List<String> permissoes, Model model){
        System.out.println("Permissoes: " + permissoes);
        if(permissoes.contains("Aluno")){
            Aluno aluno = alunoRepository.findByCpf(cpf);
            if(aluno == null){
                model.addAttribute("msg", "Aluno com CPF '" + cpf + "' não encontrado");
                return "usuarios/registerUser";
            }
            usuario.setAluno(aluno);
        }
        usuario.setPermissoes(permissoes);

        System.out.println("\n Salvando " + usuario + "\n");

        Integer id = usuarioService.saveUser(usuario);
        String msg = "Usuario '" + id + "' cadastrado com sucesso!";
        model.addAttribute("msg", msg);
        return "usuarios/registerUser";
    }

    @GetMapping("/accessDenied")
    public String getAccessDeniedPage(){
        return "usuarios/accessDeniedPage";
    }
}
