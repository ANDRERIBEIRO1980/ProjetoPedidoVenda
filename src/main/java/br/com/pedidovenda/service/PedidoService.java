package br.com.pedidovenda.service;

import java.io.Serializable;
import java.util.Date;

import javax.inject.Inject;

import br.com.pedidovenda.model.Pedido;
import br.com.pedidovenda.model.StatusPedido;
import br.com.pedidovenda.repository.PedidoRepository;
import br.com.pedidovenda.util.jpa.Transactional;

public class PedidoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PedidoRepository pedidoRepositorio;
	
	@Transactional
	public Pedido salvar(Pedido pedido) {
		
		if (pedido.isNovo()) {
			pedido.setDataCriacao(new Date());
			pedido.setStatus(StatusPedido.ORCAMENTO);
		}
		
		pedido.recalcularValorTotal();
		
		if( pedido.isNaoAlteravel()){
			throw new NegocioException("Pedido não pode ser alterado no status "
			                         + pedido.getStatus().getDescricao() + ".");
		}
		
		if(pedido.getItens().isEmpty()){
			throw new NegocioException("O pedido deve possuir pelo menos um item.");
		}

		if(pedido.isValorTotalNegativo()){
			throw new NegocioException("Valor Total do pedido não pode ser negativo.");
		}
		
		pedido = this.pedidoRepositorio.guardar(pedido);
		return pedido;
	}
	
}
