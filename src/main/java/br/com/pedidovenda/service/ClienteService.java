package br.com.pedidovenda.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.pedidovenda.model.Cliente;
import br.com.pedidovenda.repository.ClienteRepository;
import br.com.pedidovenda.util.jpa.Transactional;

public class ClienteService implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Inject
	ClienteRepository clienteRepositorio;

	@Transactional
	public void salvar(Cliente cliente){
		
		Cliente clienteExistente = clienteRepositorio.porEmail(cliente.getEmail());
		
		if (emailJaExiste(cliente) && !cliente.equals(clienteExistente)){
			throw new NegocioException("E-mail já cadastrado.");
		}
		
		clienteRepositorio.guardar(cliente);		
	}

	public boolean emailJaExiste(Cliente cliente){
		return clienteRepositorio.emailJaExiste(cliente);
	}

	@Transactional
	public void excluirCliente(Cliente clienteExcluir) {

		clienteRepositorio.excluirCliente(clienteExcluir);
		
	}
	
}
