package com.algamoney.api.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algamoney.api.model.Lancamento;
import com.algamoney.api.model.Pessoa;
import com.algamoney.api.repository.LancamentoRepository;
import com.algamoney.api.repository.PessoaRepository;
import com.algamoney.api.service.exception.PessoaInexistenteOuInativaException;

@Service
public class LancamentoService {
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired 
	private LancamentoRepository lancamentoRepository;

	public Lancamento salvar(Lancamento lancamento) {
		Pessoa pessoa = pessoaRepository.findOne(lancamento.getPessoa().getCodigo());
		if (pessoa == null || pessoa.isInativo()) {
			throw new PessoaInexistenteOuInativaException();
		}
		
		return lancamentoRepository.save(lancamento);
	}

	public Lancamento atualizar(Long codigo, Lancamento lancamento) {
		// Busca o lançamento atual no banco
		Lancamento lancamentoSalvo = buscarLancamentoExistente(codigo);
		
		// Se a pessoa do lançamento atual é diferente da pessoa que está sendo atualizada
		if (!lancamento.getPessoa().equals(lancamentoSalvo.getPessoa())) {
			// Valida se a pessoa está cadastrada
			validarPessoa(lancamento);
		}

		// Copia as propriedades que não foram informadas para o objeto que vai atualizar
		BeanUtils.copyProperties(lancamento, lancamentoSalvo, "codigo");

		// Salva no banco
		return lancamentoRepository.save(lancamentoSalvo);
	}

	private void validarPessoa(Lancamento lancamento) {
		Pessoa obj = pessoaRepository.findOne(lancamento.getPessoa().getCodigo());
		if (obj == null) {
			throw new PessoaInexistenteOuInativaException();
		}
	}

	private Lancamento buscarLancamentoExistente(Long codigo) {
		Lancamento obj = lancamentoRepository.findOne(codigo);
		if (obj == null) {
			throw new IllegalArgumentException();
		}
		return obj;
	}
	
}