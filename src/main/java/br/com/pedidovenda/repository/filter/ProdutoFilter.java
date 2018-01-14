package br.com.pedidovenda.repository.filter;

import java.io.Serializable;

import br.com.pedidovenda.validation.SKU;

public class ProdutoFilter implements Serializable{

	private static final long serialVersionUID = 1L;
	private String sku;
	private String nome;
	
	@SKU
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	

}
