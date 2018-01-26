package br.com.pedidovenda.controller;

import java.io.Serializable;

import javax.enterprise.event.Event;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.pedidovenda.model.Pedido;
import br.com.pedidovenda.service.EmissaoPedidoService;
import br.com.pedidovenda.util.jsf.FacesUtil;

@Named
@ViewScoped
public class EmissaoPedidoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EmissaoPedidoService emissaoPedidoService;

	//injeta o Pedido com a mesma referencia que foi produzido no bean CadastroPedidoBean
	@Inject
	@PedidoEdicao
	private Pedido pedido;

	//cria evento do CDI para atualizar o pedido que esta no CadastroPedidoBean, 
	//isso irá atualizar o pedido que foi produzido em CadastroPedidoBean e atualizado em EmissaoPedidoBean
	@Inject
	private Event<PedidoAlteradoEvent> pedidoAlteradoEvent;

	public void emitirPedido() {
		
		//mesma referencia de CadastroPedidoBean
		this.pedido.removerItemVazio();

		try {

			this.pedido = this.emissaoPedidoService.emitir(this.pedido);

			// lança um evento CDI para atualizar o pedido em CadastroPedidoBean
			this.pedidoAlteradoEvent.fire(new PedidoAlteradoEvent(this.pedido));

			FacesUtil.addInfoMessage("Pedido emitido com sucesso.");

		} finally {
			this.pedido.adicionarItemVazio();
		}

	}

}
