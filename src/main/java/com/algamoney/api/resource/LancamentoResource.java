package com.algamoney.api.resource;

import com.algamoney.api.event.RecursoCriadoEvent;
import com.algamoney.api.exceptionhandler.AlgamoneyExceptionHandler.Erro;
import com.algamoney.api.model.Lancamento;
import com.algamoney.api.repository.LancamentoRepository;
import com.algamoney.api.repository.filter.LancamentoFilter;
import com.algamoney.api.repository.projection.ResumoLancamento;
import com.algamoney.api.service.LancamentoService;
import com.algamoney.api.service.exception.PessoaInexistenteOuInativaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

// Classe que disponibiliza recursos de /lancamentos para os clientes

@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {

	@Autowired
	LancamentoRepository lancamentoRepository;
	
	@Autowired
	private LancamentoService lancamentoService;
	
	// Injeta a classe RecursoCriadoListener, que implementa esta interface
	@Autowired
	private ApplicationEventPublisher publisher;
	
	// Injeta um MessageSource, que representa o arquivo de mensagens messages.properties
	@Autowired
	private MessageSource messageSource;
	
	// LISTAR LANCAMENTOS ----------------------------------------------------------------
	/*
	@GetMapping
	public List<Lancamento> listar() {
		return lancamentoRepository.findAll();
	}
	*/
	
	/*
	@GetMapping
	public List<Lancamento> pesquisar(LancamentoFilter lancamentoFilter) {
		return lancamentoRepository.filtrar(lancamentoFilter);
	}
	*/

	// verifica os escopos - se o usuário e os clientes (angular e mobile) tem autorização para chamar esse método
	// Para que esta anotação funcione foi necessário a anotação @EnableGlobalMethodSecurity e o método createExpressionHandler()
	// na classe ResourceServerConfig
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	@GetMapping
	public Page<Lancamento> pesquisar(LancamentoFilter lancamentoFilter, Pageable pageable) {
		return lancamentoRepository.filtrar(lancamentoFilter, pageable);
	}
	// -----------------------------------------------------------------------------------
	
	// LISTAR LANCAMENTOS RESUMIDO -------------------------------------------------------
	// verifica os escopos - se o usuário e os clientes (angular e mobile) tem autorização para chamar esse método
	// Para que esta anotação funcione foi necessário a anotação @EnableGlobalMethodSecurity e o método createExpressionHandler()
	// na classe ResourceServerConfig
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	@GetMapping(params = "resumo")
	public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable) {
		return lancamentoRepository.resumir(lancamentoFilter, pageable);
	}
	// -----------------------------------------------------------------------------------
	
	// BUSCAR PELO CÓDIGO ----------------------------------------------------------------
	// verifica os escopos - se o usuário e os clientes (angular e mobile) tem autorização para chamar esse método
	// Para que esta anotação funcione foi necessário a anotação @EnableGlobalMethodSecurity e o método createExpressionHandler()
	// na classe ResourceServerConfig
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	@GetMapping("/{codigo}")
	public ResponseEntity<?> buscarPeloCodigo(@PathVariable Long codigo) {
		Lancamento obj = lancamentoRepository.findOne(codigo);
		return obj !=null ? ResponseEntity.ok(obj) : ResponseEntity.notFound().build();
	}
	// -----------------------------------------------------------------------------------
	
	// INSERIR LANÇAMENTO ----------------------------------------------------------------
	// @RequestBody: o objeto será passado no body da requisição
	// @Valid: valida as propriedades do objeto de acordo com as anotações de validação cada um deles
	/*
	@PostMapping
	public ResponseEntity<Lancamento> criar(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {
		Lancamento lancamentoSalvo = lancamentoRepository.save(lancamento);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSalvo.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);
	}
	*/

	// verifica os escopos - se o usuário e os clientes (angular e mobile) tem autorização para chamar esse método
	// Para que esta anotação funcione foi necessário a anotação @EnableGlobalMethodSecurity e o método createExpressionHandler()
	// na classe ResourceServerConfig
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and #oauth2.hasScope('write')")
	@PostMapping
											// @RequestBody: o objeto será passado no body da requisição
											// @Valid: valida as propriedades do objeto de acordo com as anotações de validação cada um deles
	public ResponseEntity<Lancamento> criar(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {
		Lancamento lancamentoSalvo = lancamentoService.salvar(lancamento);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSalvo.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);
	}
	// -----------------------------------------------------------------------------------
	
	// DELETAR LANÇAMENTO ----------------------------------------------------------------
	// verifica os escopos - se o usuário e os clientes (angular e mobile) tem autorização para chamar esse método
	// Para que esta anotação funcione foi necessário a anotação @EnableGlobalMethodSecurity e o método createExpressionHandler()
	// na classe ResourceServerConfig
	@PreAuthorize("hasAuthority('ROLE_REMOVER_LANCAMENTO') and #oauth2.hasScope('write')")
	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)	// código de resposta do método em caso de sucesso
	public void remover(@PathVariable Long codigo) {
		lancamentoRepository.delete(codigo);
	}
	// -----------------------------------------------------------------------------------
	
	// RESPOSTA DE PESSOA INEXISTENTE OU INATIVA -----------------------------------------
	@ExceptionHandler({ PessoaInexistenteOuInativaException.class })
	public ResponseEntity<Object> handlePessoaInexistenteOuInativaException(PessoaInexistenteOuInativaException ex) {
		String mensagemUsuario = messageSource.getMessage("pessoa.inexistente-ou-inativa", null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return ResponseEntity.badRequest().body(erros);
	}
	
	// ALTERAR LANÇAMENTO ----------------------------------------------------------------
	// verifica os escopos - se o usuário e os clientes (angular e mobile) tem autorização para chamar esse método
	// Para que esta anotação funcione foi necessário a anotação @EnableGlobalMethodSecurity e o método createExpressionHandler()
	// na classe ResourceServerConfig	
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and #oauth2.hasScope('write')")
	@PutMapping("/{codigo}")
	public ResponseEntity<Lancamento> atualizar(@PathVariable Long codigo, @Valid @RequestBody Lancamento lancamento) {
		try {
			Lancamento lancamentoSalvo = lancamentoService.atualizar(codigo, lancamento);
			return ResponseEntity.ok(lancamentoSalvo);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}
	// -----------------------------------------------------------------------------------
}
