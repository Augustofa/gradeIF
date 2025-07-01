package iftm.GradeIF.security;

import iftm.GradeIF.models.Usuario;
import iftm.GradeIF.repositories.UsuarioRepository;
import iftm.GradeIF.services.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UsuarioServiceImpl implements IUsuarioService, UserDetailsService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    private final BCryptPasswordEncoder senhaEncoder;

    public UsuarioServiceImpl(BCryptPasswordEncoder senhaEncoder) {
        this.senhaEncoder = senhaEncoder;
    }

    @Override
    public Integer saveUser(Usuario usuario){
        String senha = usuario.getSenha();
        String senhaCod = senhaEncoder.encode(senha);
        usuario.setSenha(senhaCod);
        usuario = usuarioRepository.save(usuario);
        return usuario.getId();
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<Usuario> optUsuario = usuarioRepository.findByLogin(login);

        User springUser = null;

        if(optUsuario.isEmpty()){
            throw new UsernameNotFoundException("Usuario com login: " + login + "não encontrado");
        }else{
            Usuario usuario = optUsuario.get();
            List<String> permissoes = usuario.getPermissoes();
            Set<GrantedAuthority> ga = new HashSet<>();
            for(String permissao : permissoes){
                ga.add(new SimpleGrantedAuthority(permissao));
            }

            springUser = new User(login, usuario.getSenha(), ga);
        }

        return springUser;
    }
}
