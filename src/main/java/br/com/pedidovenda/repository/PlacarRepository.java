package br.com.pedidovenda.repository;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.pedidovenda.recursos.Placar;
import br.com.pedidovenda.util.jpa.Transactional;

public class PlacarRepository {

	@Inject
	public EntityManager manager;
	
	public List<Placar> buscaPlacar() {

		return manager.createQuery("from Placar", Placar.class).getResultList();
		
	}

	@Transactional
	public void salvaPlacar(Placar placar){
		
		manager.merge(placar);
		
	}
	
}
