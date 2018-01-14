package br.com.pedidovenda.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.pedidovenda.model.Usuario;
import br.com.pedidovenda.repository.UsuarioRepository;
import br.com.pedidovenda.util.jpa.Transactional;

public class UsuarioService implements Serializable{

	private static final long serialVersionUID = 1L;
	@Inject
	UsuarioRepository usuarioRepositorio;
	
	@Transactional
	public void salvar(Usuario usuario){	
		
		 Usuario usuarioExistente = usuarioRepositorio.verificaNomeJaExiste(usuario);
		
		 if (usuarioExistente !=null && !usuario.equals(usuarioExistente)){
			 throw new NegocioException("Nome de usuário já está cadastrado.");
		 }
		 
		 usuarioRepositorio.guardar(usuario);
		 
	}

	@Transactional
	public void excluir(Usuario usuarioExcluir) {
		
		usuarioRepositorio.excluir(usuarioExcluir);
		
	}
	
	
}
