package br.com.pedidovenda.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import br.com.pedidovenda.model.Cliente;
import br.com.pedidovenda.repository.filter.ClienteFilter;
import br.com.pedidovenda.service.NegocioException;

public class ClienteRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public List<Cliente> porNome(String nome) {
		return this.manager.createQuery("from Cliente " +
				"where upper(nome) like :nome", Cliente.class)
				.setParameter("nome", "%" + nome.toUpperCase() + "%")
				.getResultList();
	}
	
	public void guardar(Cliente cliente) {

		try {
			manager.merge(cliente);
			manager.flush();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new NegocioException("Falha ao salvar Cliente.");
		}
	}

	@SuppressWarnings("unchecked")
	public List<Cliente> filtrados(ClienteFilter filtro) {

		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Cliente.class);
		
		if (StringUtils.isNotBlank(filtro.getDocumentoFederal())){
			criteria.add(Restrictions.eq("documentoReceitaFederal", filtro.getDocumentoFederal()));
		}
		
		if (StringUtils.isNotBlank(filtro.getNome())){
			criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
		}
		
		return criteria.addOrder(Order.asc("nome")).list();
		
	}

	public void excluirCliente(Cliente clienteExcluir) {

		try{
			Cliente cliente = manager.find(Cliente.class, clienteExcluir.getId());
			manager.remove(cliente);
			manager.flush();	
		} catch (PersistenceException e){
			e.printStackTrace();
			Throwable t = e;
			boolean cont = true;
			while (t != null) {
				if (t.getMessage().contains("FK_NMX283I28KPFBNJWLN34XU8LM")) {
					cont = false;
					throw new NegocioException("Impossível excluir Cliente pois o mesmo possuí Pedidos.");
				}
				t = t.getCause();
			}
			if (cont) {				
				throw new NegocioException("Falha ao excluir Cliente." );
			}		
		}
	}

	public Cliente porId(Long id) {
		return manager.find(Cliente.class, id);		
	}

	public Cliente porEmail(String email) {

			try{
				
				Cliente cliente = manager.createQuery("from Cliente c where c.email = :email", Cliente.class)
		                 .setParameter("email",email)
		                 .getSingleResult();
				
				return cliente;
				
			} catch (NoResultException e){
				return null;
			}
		
	}


	public boolean emailJaExiste(Cliente cliente) {
		
		Long q = (Long ) manager.createQuery("select count(c) from Cliente c where email = :email", Long.class)
				                .setParameter("email", cliente.getEmail())
				                .getSingleResult();
		
		return q>0?true:false;
	}
	

}
