package com.example.algamoney.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "lancamento")
@EqualsAndHashCode
public class Lancamento {

	public static final String CODIGO = "codigo";
	public static final String DESCRICAO = "descricao";
	public static final String DATA_VENCIMENTO = "dataVencimento";
	public static final String DATA_PAGAMENTO = "dataPagamento";
	public static final String VALOR = "valor";
	public static final String TIPO = "tipo";
	public static final String NOME_PESSOA = "pessoa";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;

	@NotNull
	private String descricao;

	@NotNull
	@Column(name = "data_vencimento")
	private LocalDate dataVencimento;

	@Column(name = "data_pagamento")
	private LocalDate dataPagamento;

	@NotNull
	private BigDecimal valor;

	private String observacao;

	@NotNull
	@Enumerated(EnumType.STRING)
	private TipoLancamento tipo;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "codigo_categoria")
	private Categoria categoria;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "codigo_pessoa")
	private Pessoa pessoa;
}