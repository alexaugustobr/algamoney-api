package com.algamoney.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;

// Biblioteca spring-security-oauth2 (pom.xml)
// Classe oauth para interceptar e verificar as permissões das requisições do Client (angular)
// adiciona novos endpoints a aplicação: Ex: /oauth/token
// Classe para uso em produção
// depende da configuração spring.profiles.active=oauth-security no application.properties

// @EnableWebSecurity	// a partir do Spring Boot 2.1.5 não é mais necessário essa anotação
@Profile("oauth-security") // Classe só fica ativa se no application.properties -> spring.profiles.active=oauth-security
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)	// anotação para adicionar a segurança das autorizações das ROLES nos métodos http
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	// A partir do Spring Boot 2.1.5 a autenticação do usuário passou para a classe AuthorizationServerConfig
	// Também não é mais necessário anotar a classe com EnableWebSecurity
	/*
	// Interface usada para autenticar usuário cadastrado no banco
	// Interface implementada pelo AppUserDetailsService que será injetada aqui pelo spring
	@Autowired
	private UserDetailsService userDetailsService;
	*/
	
	// ---------------------------------------------------------------------------------------------------------
	// A partir do Spring Boot 2.1.5 a autenticação do usuário passou para a classe AuthorizationServerConfig
	// Também não é mais necessário anotar a classe com EnableWebSecurity
	
	/*
	// Método para autenticar o usuário em memória
	@Autowired		// injeta o AuthenticationManagerBuilder
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		// Validação em memória do usuário e senha que deverá ser passado na requisição
		auth.inMemoryAuthentication()
			.withUser("admin").password("admin").roles("ROLE");		// usuário, senha e permissões
	}
	*/
	
	/*
	// Método para autenticar o usuário de acordo com o cadastro em banco
	@Autowired		// injeta o AuthenticationManagerBuilder
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		// passwordEncoder() -> método que retorna o tipo da criptografia da senha para que o Spring possa validar 
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	*/
	
	// ---------------------------------------------------------------------------------------------------------
	
	// Método para configurar as autorizações das requisições
	// sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	// 												-> só para a garantir desabilitação de sessão
	// 												-> já faz isso no método subsequente
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/categorias").permitAll()
				.anyRequest().authenticated()
				.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.csrf().disable();
	}
	
	// Método para desabilitar sessão de usuário (não guarda estado)
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.stateless(true);
	}
	
	// A partir do Spring Boot 2.1.5 este método passou para a classe OAuthSecurityConfig
	/*
	// Método que retorna o tipo de criptografia da senha do usuário
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	*/
	
	// Método para conseguir fazer a segurança das autorizações das ROLES nos métodos http com OAuth2
	@Bean
	public MethodSecurityExpressionHandler createExpressionHandler() {
		return new OAuth2MethodSecurityExpressionHandler();
	}
	
}