package com.algamoney.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// Classe necessária para que seja possível injetar a propriedade AuthenticationManager na classe AuthorizationServerConfig
// A partir do Spring Boot 2.1.5 foi criada esta classe para prover uma instância de AuthenticationManager e de PasswordEncoder
// Classe para uso em produção
// depende da configuração spring.profiles.active=oauth-security no application.properties

@Profile("oauth-security") // Classe só fica ativa se no application.properties -> spring.profiles.active=oauth-security
@Configuration
@EnableWebSecurity
public class OAuthSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
    
    // A partir do Spring Boot 2.1.5 este método saiu da classe ResourceServerConfig e passou para cá
    // Método que retorna o tipo de criptografia da senha do usuário
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
    @Bean
    public PasswordEncoder passwordEncoder() {
    	return NoOpPasswordEncoder.getInstance();
    }
    */

}
