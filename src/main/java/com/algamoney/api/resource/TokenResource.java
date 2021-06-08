package com.algamoney.api.resource;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algamoney.api.config.property.AlgamoneyApiProperty;

// Classe para dar logout na aplicação.
// A idéia é limpar o cookie refreshToken, dar valor nulo para ele, impedindo dessa forma o acesso

@Profile("oauth-security")
@RestController
@RequestMapping("/tokens")
public class TokenResource {
	
	// Classe de configuração de ambiente (desenvolvimento ou produção)
	@Autowired
	private AlgamoneyApiProperty algamoneyApiProperty;

	@DeleteMapping("/revoke")
	public void revoke(HttpServletRequest req, HttpServletResponse resp) {
		
		// Cria um cookie com valor nulo
		Cookie cookie = new Cookie("refreshToken", null);
		
		// Faz dele um cookie de http (acessível apenas em http)
		cookie.setHttpOnly(true);
		
		// Informa se o cookie vai funcionar apenas de forma segura (https) - true ou false
		// cookie.setSecure(false); // TODO: Em producao sera true
		cookie.setSecure(algamoneyApiProperty.getSeguranca().isEnableHttps());
		
		// Informa o caminho pra qual o cookie deve ser enviado para o browser
		cookie.setPath(req.getContextPath() + "/oauth/token");
		
		// Informa o tempo de expiração do cookie para 0 (agora)
		cookie.setMaxAge(0);
		
		// Adiciona o cookie na resposta
		resp.addCookie(cookie);
		
		// Seta status da resposta como 204
		resp.setStatus(HttpStatus.NO_CONTENT.value());
	}
	
}
