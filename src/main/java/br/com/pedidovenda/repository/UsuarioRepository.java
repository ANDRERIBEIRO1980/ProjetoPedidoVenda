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

import br.com.pedidovenda.model.Grupo;
import br.com.pedidovenda.model.Usuario;
import br.com.pedidovenda.repository.filter.UsuarioFilter;
import br.com.pedidovenda.service.NegocioException;

public class UsuarioRepository implements Serializable {

	private static final long serialVersionUID = 1L;
	@Inject
	EntityManager manager;

	@SuppressWarnings("unchecked")
	public List<Usuario> filtrados(UsuarioFilter filtro) {

		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Usuario.class);

		if (StringUtils.isNotBlank(filtro.getNome())) {
			criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
		}

		return criteria.addOrder(Order.asc("nome")).list();

	}

	public void excluir(Usuario usuario){
		try {
			
			Usuario usuarioExcluir = manager.find(Usuario.class, usuario.getId());
			manager.remove(usuarioExcluir);
			manager.flush();
			
		} catch (PersistenceException e) {

			Throwable t = e;
			boolean cont = true;
			while (t != null) {
				if (t.getMessage().contains("FK_3DFL9EW9N4U959LO4PW4Q1WCB")) {
					cont = false;
					throw new NegocioException("Impossível excluir usuário pois o mesmo possuí Pedidos.");
				}
				t = t.getCause();
			}
			if (cont) {
				throw new NegocioException("Falha ao excluir Usuário." );
			}
			
		}
	}
	
	public void guardar(Usuario usuario) {

		try {
			manager.merge(usuario);
			manager.flush();
		} catch (PersistenceException e) {

			Throwable t = e;
			boolean cont = true;
			while (t != null) { // email duplicado
				if (t.getMessage().contains("UK_4TDEHXJ7DH8GHFC68KBWBSBLL")) {
					cont = false;
					throw new NegocioException("E-mail já cadastrado.");
				}
				t = t.getCause();
			}
			if (cont) {
				throw new NegocioException("Falha ao salvar usuário.");
			}
		}
	}

	public Usuario verificaNomeJaExiste(Usuario usuario) {

		try {
			  return (Usuario) manager.createQuery("from Usuario c where upper(c.nome) = upper(:nome)")
					.setParameter("nome", usuario.getNome()).getSingleResult();

		} catch (NoResultException e) {
			return null;
		}

	}

	public List<Usuario> vendedores() {
		// TODO filtrar apenas vendedores (por um grupo especÃ­fico)
		return this.manager.createQuery("from Usuario", Usuario.class)
				.getResultList();
	}
	
	public List<Grupo> listaGrupos() {
		return manager.createQuery("from Grupo", Grupo.class).getResultList();
	}

	public Object porId(Long id) {
		return manager.find(Usuario.class, id);
	}

	public Usuario porEmail(String email) {
		
		Usuario usuario = null;
		try{
			
			usuario = manager.createQuery("from Usuario where upper(email) = :email", Usuario.class)
				      .setParameter("email", email.toUpperCase()).getSingleResult();
			
		} catch (NoResultException e)
		{
			e.printStackTrace();
		}
		
		return usuario;
		
	}

}
