package br.com.pedidovenda.repository.filter;

import java.io.Serializable;

public class ClienteFilter implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String documentoFederal;
	private String nome;
	
	public String getDocumentoFederal() {
		return documentoFederal;
	}
	public void setDocumentoFederal(String documentoFederal) {
		this.documentoFederal = documentoFederal;
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
	

}
