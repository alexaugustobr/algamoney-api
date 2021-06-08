package com.algamoney.api.config;

import com.algamoney.api.config.token.CustomTokenEnhancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Arrays;

// Biblioteca spring-security-oauth2 (pom.xml)
// Classe para dar autorização ao Client (angular) para acessar a aplicação (fazer requisições)
// adiciona novos endpoints a aplicação: Ex: /oauth/token
// Classe para uso em produção
// depende da configuração spring.profiles.active=oauth-security no application.properties

@Profile("prod") // Classe só fica ativa se no application.properties -> spring.profiles.active=oauth-security
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	// A partir do Spring Boot 2.1.5 a autenticação do usuário saiu da classe ResourceServerConfig e passou para cá
	// Interface usada para autenticar usuário cadastrado no banco
	// Interface implementada pelo AppUserDetailsService que será injetada aqui pelo spring
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	// ----------------------------------------------------------------------------------------------------------------------
	
	/*
	// Método para configurar o cliente (angular) que vai acessar a aplicação
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()						// define a configuração em memória
			.withClient("angular")				// nome do cliente que vai acessar a aplicação
			.secret("@ngul@r0")					// senha do cliente
			.scopes("read", "write")			// define escopos para o cliente (o que esse cliente vai poder fazer)
			.authorizedGrantTypes("password")	// define o tipo de fluxo como Password Flow (recebe login e senha do usuário)
			.accessTokenValiditySeconds(1800);	// Quantos segundos o token vai ficar ativo (30 min)
	}
	*/
	
	/*
	// Método para configurar o cliente (angular) que vai acessar a aplicação
	// Ao pedir um Access Token, sempre vem junto um Refresh Token
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()						// define a configuração em memória
			.withClient("angular")				// nome do cliente que vai acessar a aplicação
			.secret("@ngul@r0")					// senha do cliente
			.scopes("read", "write")			// define escopos para o cliente (o que esse cliente vai poder fazer)
			.authorizedGrantTypes("password", "refresh_token")	// define o fluxo como Password Flow (usuário e senha) e Refresh Token
			.accessTokenValiditySeconds(20)				// Quantos segundos o token vai ficar ativo
			.refreshTokenValiditySeconds(3600 * 24);	// Quantos segundos o refresh_token vai ficar ativo (24 horas)
	}
	*/

	/*
	// Método para configurar o cliente (angular) que vai acessar a aplicação
	// Ao pedir um Access Token, sempre vem junto um Refresh Token
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()						// define a configuração em memória
			.withClient("angular")				// nome do cliente que vai acessar a aplicação
			.secret("$2a$10$bsEHO7E23o97cbj8zxEfuu6HcvlxyNTffq70YEt3CrMmqk8iYBZii")		// senha do cliente encodada (@ngul@r0)
			.scopes("read", "write")			// define escopos para o cliente (o que esse cliente vai poder fazer)
			.authorizedGrantTypes("password", "refresh_token")	// define o fluxo como Password Flow (usuário e senha) e Refresh Token
			.accessTokenValiditySeconds(1800)			// Quantos segundos o token vai ficar ativo (30 min)
			.refreshTokenValiditySeconds(3600 * 24);	// Quantos segundos o refresh_token vai ficar ativo (24 horas)
	}
	*/

	// Método para configurar os dois clientes que podem acessar a aplicação (angular e mobile)
	// Ao pedir um Access Token, sempre vem junto um Refresh Token
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()							// define a configuração em memória
				.withClient("angular")				// nome do primeiro cliente que pode acessar a aplicação
				.secret("$2a$10$UulIOlI7RMWzWhzrd89tseXY0upIR1PHr9Axlt11IuXH/6aHFMloG")	// senha do cliente encodada (@ngul@r0 - $2a$10$UulIOlI7RMWzWhzrd89tseXY0upIR1PHr9Axlt11IuXH/6aHFMloG)
				.scopes("read", "write")			// define escopos para o cliente (o que esse cliente vai poder fazer)
				.authorizedGrantTypes("password", "refresh_token") // define o fluxo como Password Flow (usuário e senha) e Refresh Token
				.accessTokenValiditySeconds(30)				// Quantos segundos o token vai ficar ativo
				.refreshTokenValiditySeconds(3600 * 24)		// Quantos segundos o refresh_token vai ficar ativo (24 horas)
			.and()	// Próximo cliente
				.withClient("mobile")				// nome do segundo cliente que pode acessar a aplicação
				.secret("$2a$10$lSPgttP.CmbX2hWgz5PDpuE51rrViRdOjdg37/qI2laT2JnPD5LG6") // senha do cliente encodada (m0b1l30)
				.scopes("read")						// define escopos para o cliente (o que esse cliente vai poder fazer)
				.authorizedGrantTypes("password", "refresh_token") // define o fluxo como Password Flow (usuário e senha) e Refresh Token
				.accessTokenValiditySeconds(1800)			// Quantos segundos o token vai ficar ativo (30 min)
				.refreshTokenValiditySeconds(3600 * 24);	// Quantos segundos o refresh_token vai ficar ativo (24 horas)
	}

	// ----------------------------------------------------------------------------------------------------------------------
	
	/*
	// Método para armazenar o token
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints
			.tokenStore(tokenStore())						// define onde o token será armazenado
			.authenticationManager(authenticationManager);	// valida usuário e senha (configuração no ResourceServerConfig)
	}
	*/
	
	/*
	// Método para armazenar o JWT
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints
			.tokenStore(tokenStore())						// define onde o JWT será armazenado
			.accessTokenConverter(accessTokenConverter())	// define a palavra secreta do JWT
			.authenticationManager(authenticationManager);	// valida usuário e senha (configuração no ResourceServerConfig)
	}
	*/

	/*
	// Método para armazenar o JWT
	// reuseRefreshTokens(false) - a cada novo Access Token, um novo Refresh Token virá junto
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints
			.tokenStore(tokenStore())						// define onde o JWT será armazenado
			.accessTokenConverter(accessTokenConverter())	// define a palavra secreta do JWT
			.reuseRefreshTokens(false)						// define que um novo Refresh Token também será enviado a cada pedido de novo Access Token 
			.authenticationManager(authenticationManager);	// valida usuário e senha (configuração no ResourceServerConfig)
	}
	*/
	
	/*
	// Método para armazenar o JWT
	// A partir do Spring Boot 2.1.5 a autenticação do usuário (userDetailsService)
	// saiu da classe ResourceServerConfig e passou para cá
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
	    endpoints
	        .tokenStore(tokenStore())						// define onde o JWT será armazenado
	        .accessTokenConverter(accessTokenConverter())	// define a palavra secreta do JWT
	        .reuseRefreshTokens(false)						// define que um novo Refresh Token também será enviado a cada pedido de novo Access Token 
	        .userDetailsService(userDetailsService)			// valida usuário e senha
	        .authenticationManager(authenticationManager);
	}
	*/

	@Override
	// Método para armazenar o JWT
	// A partir do Spring Boot 2.1.5 a autenticação do usuário (userDetailsService)
	// saiu da classe ResourceServerConfig e passou para cá
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		// Cria um tipo de token melhorado que pode receber mais detalhes, para passar o nome do usuário para o front-end
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		
		// Passa para o token melhorado (tokenEnhancerChain) o token normal (accessTokenConverter()) +
		// uma implementação da interface TokenEnhancer para obter o nome do usuário
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter()));
		
		endpoints
			.tokenStore(tokenStore())						// define onde o JWT será armazenado
			.tokenEnhancer(tokenEnhancerChain)				// passa o token melhorado
			.reuseRefreshTokens(false)						// define que um novo Refresh Token também será enviado a cada pedido de novo Access Token
			.userDetailsService(userDetailsService)			// valida usuário e senha
			.authenticationManager(authenticationManager);
	}

	// ----------------------------------------------------------------------------------------------------------------------
	
	/*
	// Método que define onde o token será armazenado
	@Bean
	public TokenStore tokenStore() {
		return new InMemoryTokenStore();	// Armazena o token em memória
	}
	*/
	
	// Método que define onde o JWT será armazenado
	@Bean
	public TokenStore tokenStore() {
		// Na verdade esse objeto não armazena o token, é usado apenas para valida-lo
		return new JwtTokenStore(accessTokenConverter());
	}
	
	// **************************************************************************************************************
	
	// Método que define a palavra secreta do JWT
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
		accessTokenConverter.setSigningKey("algaworks");
		return accessTokenConverter;
	}
	
	// Retorna um objeto que implementa a interface TokenEnhancer
	@Bean
	public TokenEnhancer tokenEnhancer() {
	    return new CustomTokenEnhancer();
	}
	
}
