package com.example.algamoney.api.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.repository.PessoaRepository;

@Service
public class PessoaService {

	@Autowired
	private PessoaRepository pessoaRepository;
	
	public Pessoa atualizar(Long codigo, Pessoa pessoa) {
		Pessoa pessoaBd = buscarPessoaPorCodigo(codigo);
		BeanUtils.copyProperties(pessoa, pessoaBd, "codigo");
		return pessoaRepository.save(pessoaBd);
	}

	public void atualizarPropriedadeAtivo(Long codigo, Boolean ativo) {
		Pessoa pessoaBd = buscarPessoaPorCodigo(codigo);
		pessoaBd.setAtivo(ativo);
		pessoaRepository.save(pessoaBd);
	}
	
	private Pessoa buscarPessoaPorCodigo(Long codigo) {
		Pessoa pessoaBd = pessoaRepository.findOne(codigo);
		if (pessoaBd == null) {
			throw new EmptyResultDataAccessException(1);
		}
		
		return pessoaBd;
	}
}
