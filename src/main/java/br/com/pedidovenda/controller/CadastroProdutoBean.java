package br.com.pedidovenda.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.pedidovenda.model.Categoria;
import br.com.pedidovenda.model.Produto;
import br.com.pedidovenda.repository.CategoriaRepository;
import br.com.pedidovenda.service.ProdutoService;
import br.com.pedidovenda.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroProdutoBean implements Serializable{

	private static final long serialVersionUID = 1L;

	private Produto produto;	
	private List<Categoria> categoriasRaizes;
	private Categoria categoriaPai;
	private List<Categoria> listaSubCategorias = new ArrayList<>();

	public CadastroProdutoBean(){
		limpar();
	}

	@Inject
	private CategoriaRepository categoriaRepositorio;
	
	@Inject
	private ProdutoService cadastroProdutoService;
	
	public void inicializar(){	
		
		System.out.println("inicializar");
		
		categoriasRaizes = categoriaRepositorio.raizes();
		
		//PARA POPULAR A CATEGORIA AUTOMATICAMENTE QUANDO NA ALTERACAO
		if (this.categoriaPai != null) {
			filtrarSubCategorias();
		}
	}
	
	public void limpar(){
		produto = new Produto();
		categoriaPai = null;
		listaSubCategorias = new ArrayList<>();
	}
	
	public void salvar() {
	
		produto = cadastroProdutoService.salvar(produto);
		limpar();
		FacesUtil.addInfoMessage("Produto salvo com sucesso.");	
		
	}
	
	public void filtrarSubCategorias(){
		
		if (categoriaPai==null){
			listaSubCategorias = new ArrayList<>();
		}else{
			listaSubCategorias = categoriaRepositorio.subCategorias(categoriaPai.getId());	
		}
	}
	
	public Produto getProduto() {
		return produto;
	}
	
	public void setProduto(Produto produto) {		
		this.produto = produto;
		/*PRECISA COLOCAR ESSE IF PARA POPULAR OS COMBO DE CATEGORIA PAI*/
		if (produto != null){
			this.categoriaPai = this.produto.getCategoria().getCategoriaPai();
		}
	}
	
	
	public List<Categoria> getCategoriasRaizes() {
		return categoriasRaizes;
	}
	public void setCategoriasRaizes(List<Categoria> categoriasRaizes) {
		this.categoriasRaizes = categoriasRaizes;
	}
	
	public Categoria getCategoriaPai() {
		return categoriaPai;
	}
	public void setCategoriaPai(Categoria categoriaPai) {
		this.categoriaPai = categoriaPai;
	}
	
	public List<Categoria> getListaSubCategorias() {
		return listaSubCategorias;
	}
	public void setListaSubCategorias(List<Categoria> listaSubCategorias) {
		this.listaSubCategorias = listaSubCategorias;
	}
	
	public boolean isEditando(){
		return produto.getId() != null;
	}
}