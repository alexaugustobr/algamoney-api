package com.algamoney.api.resource;

import com.algamoney.api.event.RecursoCriadoEvent;
import com.algamoney.api.model.Categoria;
import com.algamoney.api.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

// Classe que disponibiliza recursos de /categorias para os clientes

/*
STATUS CODE:
2XX -> SUCESSO
4XX -> ERRO DO CLIENTE
5XX -> ERRO DO SERVIDOR 
*/

// @CrossOrigin					// permite a todas as origens chamar todos os métodos desse controlador
@RestController
@RequestMapping("/categorias")
public class CategoriaResource {
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	// Injeta a classe RecursoCriadoListener 
	@Autowired
	private ApplicationEventPublisher publisher;

	// LISTAR CATEGORIAS -------------------------------------------------------------
	
	// @CrossOrigin											// permite a todas as origens chamar este método
	// @CrossOrigin(maxAge = 10)							// maxAge -> em quanto tempo o browse vai fazer essa requisição
	// @CrossOrigin(origins = {"http://localhost:8000"})	// origins -> quais as origens permitidas
	// @CrossOrigin(allowedHeaders = {"..."})				// allowedHeaders -> quais os headers permitidos
	// @PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA')") // verifica se o usuário tem autorização para chamar esse método
	
	// verifica os escopos - se o usuário e os clientes (angular e mobile) tem autorização para chamar esse método
	// Para que esta anotação funcione foi necessário a anotação @EnableGlobalMethodSecurity e o método createExpressionHandler()
	// na classe ResourceServerConfig
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read')")
	@GetMapping
	public List<Categoria> listar() {
		return categoriaRepository.findAll();
	}
	
	/*
	@GetMapping
	public ResponseEntity<?> listar() {
		List<Categoria> categorias = categoriaRepository.findAll();
		return !categorias.isEmpty() ? ResponseEntity.ok(categorias) : ResponseEntity.noContent().build();
	}
	*/
	// -------------------------------------------------------------------------------
	
	// BUSCAR PELO CÓDIGO ------------------------------------------------------------
	/*
	@GetMapping("/{codigo}")
	public Categoria buscarPeloCodigo(@PathVariable Long codigo) {
		Optional<Categoria> obj = categoriaRepository.findById(codigo);
		return obj.orElse(null);
	}
	*/
	
	// @PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA')") // verifica se o usuário tem autorização para chamar esse método
	
	// verifica os escopos - se o usuário e os clientes (angular e mobile) tem autorização para chamar esse método
	// Para que esta anotação funcione foi necessário a anotação @EnableGlobalMethodSecurity e o método createExpressionHandler()
	// na classe ResourceServerConfig
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read')")
	@GetMapping("/{codigo}")
	public ResponseEntity<?> buscarPeloCodigo(@PathVariable Long codigo) {
		Categoria obj = categoriaRepository.findOne(codigo);
		return obj != null ? ResponseEntity.ok(obj) : ResponseEntity.notFound().build();
	}
	// -------------------------------------------------------------------------------
	
	// INSERIR CATEGORIA -------------------------------------------------------------
	// @RequestBody: o objeto será recebido no body da requisição
	// @Valid: usa as anotações de validação das propriedades do objeto
	
	/*
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void criar(@RequestBody Categoria categoria) {
		categoriaRepository.save(categoria);
	}
	*/
	
	/*
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void criar(@RequestBody Categoria categoria, HttpServletResponse response) {
		Categoria categoriaSalva = categoriaRepository.save(categoria);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
			.buildAndExpand(categoriaSalva.getCodigo()).toUri();
		response.setHeader("Location", uri.toASCIIString());
	}
	*/
	
	/*
	@PostMapping
	public ResponseEntity<Categoria> criar(@RequestBody Categoria categoria, HttpServletResponse response) {
		Categoria categoriaSalva = categoriaRepository.save(categoria);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
			.buildAndExpand(categoriaSalva.getCodigo()).toUri();
		response.setHeader("Location", uri.toASCIIString());
		
		return ResponseEntity.created(uri).body(categoriaSalva);
	}
	*/
	
	/*
	@PostMapping
	public ResponseEntity<Categoria> criar(@Valid @RequestBody Categoria categoria, HttpServletResponse response) {
		Categoria categoriaSalva = categoriaRepository.save(categoria);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
			.buildAndExpand(categoriaSalva.getCodigo()).toUri();
		response.setHeader("Location", uri.toASCIIString());
		
		return ResponseEntity.created(uri).body(categoriaSalva);
	}
	*/
	
	// @PreAuthorize("hasAuthority('ROLE_CADASTRAR_CATEGORIA')") // verifica se o usuário tem autorização para chamar esse método
	
	// verifica os escopos - se o usuário e os clientes (angular e mobile) tem autorização para chamar esse método
	// Para que esta anotação funcione foi necessário a anotação @EnableGlobalMethodSecurity e o método createExpressionHandler()
	// na classe ResourceServerConfig
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_CATEGORIA') and #oauth2.hasScope('write')")
	@PostMapping
	public ResponseEntity<Categoria> criar(@Valid @RequestBody Categoria categoria, HttpServletResponse response) {
		Categoria categoriaSalva = categoriaRepository.save(categoria);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, categoriaSalva.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(categoriaSalva);
	}
	// -------------------------------------------------------------------------------

}
