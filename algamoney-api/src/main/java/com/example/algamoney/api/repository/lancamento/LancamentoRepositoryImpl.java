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
import org.springframework.util.StringUtils;

import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.model.Lancamento_;
import com.example.algamoney.api.repository.filter.LancamentoFilter;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery {

	@Autowired
	EntityManager entityManager;
	
	@Override
	public List<Lancamento> filtrar(LancamentoFilter lancamentoFilter) {
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		
		CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class);
		
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
		
		criteria.where(predicates);
		
		TypedQuery<Lancamento> query = entityManager.createQuery(criteria);
		
		return query.getResultList();
		 
	}

	private Predicate[] criarRestricoes(LancamentoFilter lancamentoFilter, CriteriaBuilder builder, Root<Lancamento> root) {
		
		List<Predicate> predicates = new ArrayList<>();
		
		// Classes geradas pelo JPA META MODEL GEN
		if (!StringUtils.isEmpty(lancamentoFilter.getDescricao())) {
			predicates.add(builder.like(builder.lower(root.get(Lancamento_.descricao)), "%" + lancamentoFilter.getDescricao().toLowerCase() + "%"));
		}
		
		if (lancamentoFilter.getDataVencimentoDe() != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento), lancamentoFilter.getDataVencimentoDe()));
		}

		if (lancamentoFilter.getDataVencimentoAte() != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento), lancamentoFilter.getDataVencimentoAte()));
		}
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}
	
	// Teste
	/*
	private List<Lancamento> filtrarJpql(LancamentoFilter lancamentoFilter) {
		StringBuilder sb = new StringBuilder();

		sb.append(" from Lancamento where ");
		
		if (!StringUtils.isEmpty(lancamentoFilter.getDescricao())) {
			sb.append(Lancamento_.descricao.getName() + " like '%" + lancamentoFilter.getDescricao().toLowerCase() + "'");
		}
		
		if (lancamentoFilter.getDataVencimentoDe() != null) {
			sb.append(" and " + Lancamento_.dataVencimento + " = " + lancamentoFilter.getDataVencimentoDe());
		}

		if (lancamentoFilter.getDataVencimentoAte() != null) {
			sb.append(" and " + Lancamento_.dataVencimento + " = " + lancamentoFilter.getDataVencimentoAte());
		}
		
		TypedQuery<Lancamento> query = (TypedQuery<Lancamento>) entityManager.createQuery(sb.toString());
		
		return (List<Lancamento>) query.getResultList();
	}
	*/
}
