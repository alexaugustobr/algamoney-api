package com.algamoney.api.token;

import org.apache.catalina.util.ParameterMap;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Stream;

// Classe para interceptar a requisição de Novo Access Token e pegar o Refresh Token que vem no cookie

// Esse filtro foi criado porque o Refresh Token não virá mais no corpo da requisição,
// pois já é enviado automaticamente pelo http dentro do cookie.
// O parâmetro refresh_token será criado dinamicamente

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)		// Ordered.HIGHEST_PRECEDENCE: alta prioridade, analisa a requisição antes de todo mundo
public class RefreshTokenCookiePreProcessorFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		// Realiza castings para poder acessar a requisição
		HttpServletRequest req = (HttpServletRequest) request;
		
		 //  Verifica se a requisição é para o oauth/token e
		 //  se a parâmetro grant_type contem o valor "refresh_token" e
		 //  se existe algum cookie
		if ("/oauth/token".equalsIgnoreCase(req.getRequestURI()) 
				&& "refresh_token".equals(req.getParameter("grant_type"))
				&& req.getCookies() != null) {
			
			// Este trecho de código pode ser substituido pela API de Stream que veio a partir do Java 8
			/*
			// Procura pelo cookie refreshToken
			for (Cookie cookie : req.getCookies()) {
				
				if (cookie.getName().equals("refreshToken")) {
					// Extrai o valor do cookie
					String refreshToken = cookie.getValue();
					
					// Como não é possível manipular os parâmetros da requisição original,
					// sobreescreve a requisição original com uma nova que contenha o parâmetro refresh_token
					req = new MyServletRequestWrapper(req, refreshToken);
				}
			}
			*/
			String refreshToken = Stream
					.of(req.getCookies())
					.filter(cookie -> "refreshToken".equals(cookie.getName()))
					.findFirst()
					.map(cookie -> cookie.getValue())
					.orElse(null);
			
			req = new MyServletRequestWrapper(req, refreshToken);
		}
		
		// Continua o processamento
		chain.doFilter(req, response);
	}

	@Override
	public void destroy() {

	}

	// Classe local criada apenas com o propósito de manipular a requisição original para acrescentar o parâmetro refresh_token,
	// já que não é possível manipulá-la diretamente 
	static class MyServletRequestWrapper extends HttpServletRequestWrapper {

		private String refreshToken;
		
		// Construtor recebendo a requisição original e o refreshToken
		public MyServletRequestWrapper(HttpServletRequest request, String refreshToken) {
			super(request);
			this.refreshToken = refreshToken;
		}
		
		@Override
		public Map<String, String[]> getParameterMap() {
			// Cria um novo mapa de parâmetros inserindo os parâmetro originais da requisição
			ParameterMap<String, String[]> map = new ParameterMap<>(getRequest().getParameterMap());
			
			// Insere um novo parâmetro de nome refresh_token
			// refresh_token é o nome que o oauth2 vai usar para recuperar o Refresh Token
			map.put("refresh_token", new String[] { refreshToken });
			
			// Trava o mapa para que não seja possível inserir mais nada
			map.setLocked(true);
			
			return map;
		}
		
	}

}
