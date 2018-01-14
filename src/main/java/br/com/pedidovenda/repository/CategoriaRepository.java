package br.com.pedidovenda.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.pedidovenda.model.Categoria;

public class CategoriaRepository implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public Categoria porId(Long id){
		return manager.find(Categoria.class, id);
	}
	
	public List<Categoria> raizes(){
		
		return manager.createQuery("from Categoria c where c.categoriaPai is null", Categoria.class).getResultList();
	}
	
	public List<Categoria> subCategorias(Long categoriaPai){
		
		return manager.createQuery("from Categoria where categoriaPai = " + categoriaPai,
            				Categoria.class).getResultList();
		
	}

}
