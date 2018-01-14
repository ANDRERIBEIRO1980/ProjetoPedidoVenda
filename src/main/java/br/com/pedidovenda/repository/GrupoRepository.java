package br.com.pedidovenda.repository;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.pedidovenda.model.Grupo;

public class GrupoRepository implements Serializable{

	@Inject
	EntityManager manager;
	
	private static final long serialVersionUID = 1L;

	public Object porId(Long id) {
		return manager.find(Grupo.class, id);
	}
	
	

}
