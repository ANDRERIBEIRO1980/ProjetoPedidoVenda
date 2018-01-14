package br.com.pedidovenda.controller;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.com.pedidovenda.model.Produto;
import br.com.pedidovenda.repository.ProdutoRepository;
import br.com.pedidovenda.repository.filter.ProdutoFilter;
import br.com.pedidovenda.service.ProdutoService;
import br.com.pedidovenda.util.jsf.FacesUtil;

@Named
@javax.faces.view.ViewScoped
public class PesquisaProdutosBean  implements Serializable{

	private static final long serialVersionUID = 1L;
	private List<Produto> produtosFiltrados;
	private ProdutoFilter filtro;
	private Produto produtoExcluir;
	
	@Inject
	private ProdutoRepository produtoRepositorio;
	
	@Inject
	private ProdutoService produtoService;
	
	public PesquisaProdutosBean(){
		filtro = new ProdutoFilter();
	}
	
	public void pesquisar(){		
		produtosFiltrados = produtoRepositorio.filtrados(filtro);		
	}

	public void excluir(){
		
		produtoService.excluir(produtoExcluir);
		pesquisar();
		FacesUtil.addInfoMessage("Produto " + produtoExcluir.getNome() + " excluído com sucesso.");
		
	}
	
	public List<Produto> getProdutosFiltrados() {
		return produtosFiltrados;
	}
	
	public void setProdutosFiltrados(List<Produto> produtosFiltrados) {
		this.produtosFiltrados = produtosFiltrados;
	}

	public ProdutoFilter getFiltro() {
		return filtro;
	}

	public void setFiltro(ProdutoFilter filtro) {
		this.filtro = filtro;
	}
	
	public Produto getProdutoExcluir() {
		return produtoExcluir;
	}
	public void setProdutoExcluir(Produto produtoExcluir) {
		this.produtoExcluir = produtoExcluir;
	}
	
	
}