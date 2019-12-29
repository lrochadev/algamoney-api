package com.example.algamoney.api.model;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class Endereco {

	private String logradouro;
	private String numero;
	private String complemento;
	private String bairro;
	private String cep;
	private String cidade;
	private String estado;
}