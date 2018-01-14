package br.com.pedidovenda.recursos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Frase {

	private Integer id;
	private String frase;
	private Integer tempo;
	
	public Frase() {
	}

	public Frase(Integer id, String frase, Integer tempo) {
		this.id = id;
		this.frase = frase;
		this.tempo = tempo;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFrase() {
		return frase;
	}
	public void setFrase(String frase) {
		this.frase = frase;
	}
	public Integer getTempo() {
		return tempo;
	}
	public void setTempo(Integer tempo) {
		this.tempo = tempo;
	}
	
	
	
}
