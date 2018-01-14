package br.com.pedidovenda.recursos;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Placar implements Serializable{

	private static final long serialVersionUID = 1L;
	private String usuario;
	private String pontos;
	private Long id;
	
	public Placar() {
	}
	
	@Id
	@SequenceGenerator(name="placar_seq", initialValue=1, sequenceName="PLACAR_SEQ")
	@GeneratedValue(strategy=GenerationType.AUTO,generator="placar_seq")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public String getPontos() {
		return pontos;
	}
	public void setPontos(String pontos) {
		this.pontos = pontos;
	}

	@Override
	public String toString() {
		return "Placar [usuario=" + usuario + ", pontos=" + pontos + ", id=" + id + "]";
	}
	
	
	
}
