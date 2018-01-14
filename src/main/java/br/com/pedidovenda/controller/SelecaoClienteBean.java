package br.com.pedidovenda.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;

import br.com.pedidovenda.model.Cliente;
import br.com.pedidovenda.repository.ClienteRepository;

@Named
@ViewScoped
public class SelecaoClienteBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ClienteRepository clienteRepositorio;
	
	private String nome;
	
	private List<Cliente> clientesFiltrados;
	
	public void pesquisar() {
		clientesFiltrados = clienteRepositorio.porNome(nome);
	}

	public void selecionar(Cliente cliente) {
		
		RequestContext.getCurrentInstance().closeDialog(cliente);
		
	}
	
	public void abrirDialogo() {
		
		Map<String, Object> opcoes = new HashMap<>();
		opcoes.put("modal", true);
		opcoes.put("resizable", false);
		opcoes.put("contentHeight", 470);
		opcoes.put("top", 50);
		
		RequestContext.getCurrentInstance().openDialog("/dialogos/SelecaoCliente", opcoes, null);
		
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Cliente> getClientesFiltrados() {
		return clientesFiltrados;
	}

}