package br.com.pedidovenda.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.pedidovenda.model.Pedido;
import br.com.pedidovenda.model.StatusPedido;
import br.com.pedidovenda.repository.PedidoRepository;
import br.com.pedidovenda.util.jpa.Transactional;

public class CancelamentoPedidoService implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Inject	
	private PedidoRepository pedidoRepositorio;
	
	@Inject EstoqueService estoqueService;
	

	@Transactional
	public Pedido cancelar(Pedido pedido) {
	
		pedido = (Pedido) this.pedidoRepositorio.porId(pedido.getId());
		
		if (pedido.isNaoCancelavel()){
			throw new NegocioException("Pedido não pode ser cancelado no status " + 
		                              pedido.getStatus().getDescricao());
		}
		
		if (pedido.isEmitido()){
			this.estoqueService.retornarItensEstoque(pedido);
		}
		
		pedido.setStatus(StatusPedido.CANCELADO);
		
		pedido = this.pedidoRepositorio.guardar(pedido);
		
		return pedido;
	}

}
