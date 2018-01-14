package br.com.pedidovenda.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.com.pedidovenda.model.Grupo;
import br.com.pedidovenda.model.Usuario;
import br.com.pedidovenda.repository.UsuarioRepository;
import br.com.pedidovenda.service.UsuarioService;
import br.com.pedidovenda.util.jsf.FacesUtil;

@Named
@javax.faces.view.ViewScoped
public class CadastroUsuarioBean  implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private List<Grupo> listaGrupos = new ArrayList<>();
	private Usuario usuario;
	private Grupo grupo;
	private Grupo grupoExcluir;
	
	@Inject
	private UsuarioService usuarioService;
	
	@Inject
	private UsuarioRepository usuarioRepositorio;
	
	public CadastroUsuarioBean(){
		
		usuario = new Usuario();
	}

	public void inicializar(){
		
		listaGrupos = usuarioRepositorio.listaGrupos();
		
	}
	
	public void salvar()
	{	
		usuario.setNome(usuario.getNome().toUpperCase());
		usuarioService.salvar(usuario);
		usuario = new Usuario();
		FacesUtil.addInfoMessage("Usuário salvo com sucesso.");		
	}
	
	public void adicionarGrupo(){		
		if (!usuario.getGrupos().contains(grupo))
		{
			usuario.getGrupos().add(grupo);
			grupo = new Grupo();	
		}else{
			FacesUtil.addErrorMessage("Grupo já cadastrado para o usuário");
		}		
	}
	
	public void excluirGrupo(){
		usuario.getGrupos().remove(grupoExcluir);
	}
	
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public Grupo getGrupo() {
		return grupo;
	}
	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}
	
	public List<Grupo> getListaGrupos() {
		return listaGrupos;
	}
	public void setListaGrupos(List<Grupo> listaGrupos) {
		this.listaGrupos = listaGrupos;
	}
	
	public Grupo getGrupoExcluir() {
		return grupoExcluir;
	}
	public void setGrupoExcluir(Grupo grupoExcluir) {
		this.grupoExcluir = grupoExcluir;
	}
	
	public boolean isEditando(){
		return usuario.getId()!=null;
	}
}
