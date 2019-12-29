package com.example.algamoney.api.repository;

import com.example.algamoney.api.model.Pessoa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoaRepository extends PagingAndSortingRepository<Pessoa, Long> {

	Page<Pessoa> findByNomeContaining(String nome, Pageable pageable);
}
