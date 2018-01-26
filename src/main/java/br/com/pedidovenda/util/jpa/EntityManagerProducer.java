package br.com.pedidovenda.util.jpa;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ApplicationScoped//para colocar a factory no escopo de application
public class EntityManagerProducer {
	
	private EntityManagerFactory factory;
	
	public EntityManagerProducer(){
		factory = Persistence.createEntityManagerFactory("PedidoPU");
	}
	
	@Produces
	@RequestScoped //esse método tem scopo de requisicao, apos a requisicao esse metodo sera descartado
	public EntityManager createEntityManager(){
		return factory.createEntityManager();
	}
	
	//@Disposes fecha a manager ao final da requisicao	
	public void closeEntityManager(@Disposes EntityManager manager){
		manager.close();
	}
	
}
