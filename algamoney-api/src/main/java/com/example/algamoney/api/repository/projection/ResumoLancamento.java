package com.example.algamoney.api.repository.projection;

import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.model.TipoLancamento;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ResumoLancamento {
	private Long codigo;
	private String descricao;
	private LocalDate dataVencimento;
	private LocalDate dataPagamento;
	private BigDecimal valor;
	private TipoLancamento tipo;
	private Pessoa pessoa;
}