package com.example.algamoney.api.repository.lancamento;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.repository.filter.LancamentoFilter;
import com.example.algamoney.api.repository.projection.ResumoLancamento;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery {

	@Autowired
	EntityManager entityManager;
	
	@Override
	public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable) {
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		
		CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class);
		
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
		
		criteria.where(predicates);
		
		TypedQuery<Lancamento> query = entityManager.createQuery(criteria);
		
		adicionarRestricoesDePaginacao(query, pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter));
		 
	}

	private Predicate[] criarRestricoes(LancamentoFilter lancamentoFilter, CriteriaBuilder builder, Root<Lancamento> root) {
		
		List<Predicate> predicates = new ArrayList<>();
		
		// Classes geradas pelo JPA META MODEL GEN
		if (!StringUtils.isEmpty(lancamentoFilter.getDescricao())) {
			predicates.add(builder.like(builder.lower(root.get(Lancamento.DESCRICAO)), "%" + lancamentoFilter.getDescricao().toLowerCase() + "%"));
		}
		
		if (lancamentoFilter.getDataVencimentoDe() != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get(Lancamento.DATA_VENCIMENTO), lancamentoFilter.getDataVencimentoDe()));
		}

		if (lancamentoFilter.getDataVencimentoAte() != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get(Lancamento.DATA_VENCIMENTO), lancamentoFilter.getDataVencimentoAte()));
		}
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}
	
	private void adicionarRestricoesDePaginacao(TypedQuery<?> query, Pageable pageable) {
		int paginaAtual = pageable.getPageNumber();
		int totalRegistrosPorPagina = pageable.getPageSize();
		int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;
		
		query.setFirstResult(primeiroRegistroDaPagina);
		query.setMaxResults(totalRegistrosPorPagina);
	}
	
	private Long total(LancamentoFilter lancamentoFilter) {
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
		
		criteria.where(predicates);
		
		criteria.select(builder.count(root));
		
		return entityManager.createQuery(criteria).getSingleResult();
	}

	@Override
	public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ResumoLancamento> criteria = builder.createQuery(ResumoLancamento.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		criteria.select(builder.construct(ResumoLancamento.class,
				root.get(Lancamento.CODIGO),
				root.get(Lancamento.DESCRICAO),
				root.get(Lancamento.DATA_VENCIMENTO),
				root.get(Lancamento.DATA_PAGAMENTO),
				root.get(Lancamento.VALOR),
				root.get(Lancamento.TIPO),
				root.get(Lancamento.NOME_PESSOA)));
		
		Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
		
		criteria.where(predicates);
		
		TypedQuery<ResumoLancamento> query = entityManager.createQuery(criteria);
		
		adicionarRestricoesDePaginacao(query, pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter));
	}
}
