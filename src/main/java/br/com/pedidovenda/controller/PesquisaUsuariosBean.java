package br.com.pedidovenda.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.com.pedidovenda.model.Usuario;
import br.com.pedidovenda.repository.UsuarioRepository;
import br.com.pedidovenda.repository.filter.UsuarioFilter;
import br.com.pedidovenda.service.UsuarioService;
import br.com.pedidovenda.util.jsf.FacesUtil;

@Named
@javax.faces.view.ViewScoped
public class PesquisaUsuariosBean  implements Serializable{


	private static final long serialVersionUID = 1L;
	private List<Usuario> usuariosFiltrados = new ArrayList<Usuario>();
    private UsuarioFilter filtro;
    private Usuario usuarioExcluir;
	
	@Inject
	private UsuarioRepository usuarioRepositorio;
	
	@Inject
	private UsuarioService usuarioService;
	
	public PesquisaUsuariosBean() {
		filtro = new UsuarioFilter();
		usuariosFiltrados = new ArrayList<Usuario>();
	}

	public void pesquisar(){		
		usuariosFiltrados = usuarioRepositorio.filtrados(filtro);		
	}
	
	public void excluir(){
		
		usuarioService.excluir(usuarioExcluir);
		usuariosFiltrados.remove(usuarioExcluir);
		FacesUtil.addInfoMessage("Usuário excluído com sucesso.");
	}
	
	public List<Usuario> getUsuariosFiltrados() {
		return usuariosFiltrados;
	}
	public void setUsuariosFiltrados(List<Usuario> usuariosFiltrados) {
		this.usuariosFiltrados = usuariosFiltrados;
	}
	
	public UsuarioFilter getFiltro() {
		return filtro;
	}
	public void setFiltro(UsuarioFilter filtro) {
		this.filtro = filtro;
	}
	
	public Usuario getUsuarioExcluir() {
		return usuarioExcluir;
	}
	
	public void setUsuarioExcluir(Usuario usuarioExcluir) {
		this.usuarioExcluir = usuarioExcluir;
	}
	
}