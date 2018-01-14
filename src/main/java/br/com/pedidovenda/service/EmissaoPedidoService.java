package br.com.pedidovenda.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.pedidovenda.model.Pedido;
import br.com.pedidovenda.model.StatusPedido;
import br.com.pedidovenda.repository.PedidoRepository;
import br.com.pedidovenda.util.jpa.Transactional;

public class EmissaoPedidoService implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Inject 
	private PedidoService pedidoService;
	
	@Inject
	private PedidoRepository pedidoRepositorio;
	
	@Inject
	private EstoqueService estoqueService;
	
	@Transactional
	public Pedido emitir(Pedido pedido){
		
		pedido = this.pedidoService.salvar(pedido);
		
		if(pedido.isNaoEmissivel()){
			
			throw new NegocioException("Pedido não pode ser emitido com status "  
			                   + pedido.getStatus().getDescricao());
			
		}
		
		this.estoqueService.baixarItensEstoque(pedido);
		
		pedido.setStatus(StatusPedido.EMITIDO);
		
		pedido = this.pedidoRepositorio.guardar(pedido);		
		
		return pedido;
		
	}

}
