package br.com.pedidovenda.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.pedidovenda.model.ItemPedido;
import br.com.pedidovenda.model.Pedido;
import br.com.pedidovenda.repository.PedidoRepository;
import br.com.pedidovenda.util.jpa.Transactional;

public class EstoqueService implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Inject
	private PedidoRepository pedidoRepositorio;
	
	
	@Transactional
	public void baixarItensEstoque(Pedido pedido){
		
		pedido = (Pedido) this.pedidoRepositorio.porId(pedido.getId());
		
		for (ItemPedido item : pedido.getItens()){
			
			item.getProduto().baixarEstoque(item.getQuantidade());
			
		}
	}

	public void retornarItensEstoque(Pedido pedido) {
	
		pedido = (Pedido) this.pedidoRepositorio.porId(pedido.getId());
		
		for (ItemPedido item: pedido.getItens()){
			item.getProduto().adicionarEstoque(item.getQuantidade());
		}
		
	}
	

}
