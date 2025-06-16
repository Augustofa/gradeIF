package iftm.GradeIF.impls;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// Erro quando colocado na pasta "configs", manter em "impls"
@Configuration
public class AppConfig {
    @Bean
    public BCryptPasswordEncoder senhaEncoder(){
        return new BCryptPasswordEncoder();
    }
}
