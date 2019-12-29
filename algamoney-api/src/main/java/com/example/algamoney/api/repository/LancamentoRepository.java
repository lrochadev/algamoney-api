package com.example.algamoney.api.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.repository.lancamento.LancamentoRepositoryQuery;

@Repository
public interface LancamentoRepository extends CrudRepository<Lancamento, Long>, LancamentoRepositoryQuery  {

}
