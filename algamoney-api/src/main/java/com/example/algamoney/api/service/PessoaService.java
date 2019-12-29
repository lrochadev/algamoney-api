package com.example.algamoney.api.service;

import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.repository.PessoaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class PessoaService {

    private final PessoaRepository pessoaRepository;

    @Autowired
    public PessoaService(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

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
        return pessoaRepository.findById(codigo).orElseThrow(() -> new EmptyResultDataAccessException(1));
    }
}
