package iftm.GradeIF.services;

import iftm.GradeIF.models.Usuario;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.beans.BeanProperty;

@Component
public interface IUsuarioService {
    public Integer saveUser(Usuario usuario);
}
