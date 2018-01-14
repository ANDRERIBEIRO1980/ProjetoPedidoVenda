package br.com.pedidovenda.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.pedidovenda.model.Produto;
import br.com.pedidovenda.repository.ProdutoRepository;
import br.com.pedidovenda.util.jpa.Transactional;

public class ProdutoService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private ProdutoRepository produtoRepositorio;
	
	@Transactional
	public Produto salvar(Produto produto){
		
		Produto produtoExistente = produtoRepositorio.porSku(produto.getSku());
		
		if (produtoExistente != null && !produtoExistente.equals(produto)) {
			throw new NegocioException("Já existe um produto com o SKU informado.");
		}
		
		return produtoRepositorio.guardar(produto);
		
	}
	
	@Transactional
	public void excluir(Produto produto){
		
		produtoRepositorio.excluir(produto);
		
	}

}
