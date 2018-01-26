package br.com.pedidovenda.controller;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.pedidovenda.model.Pedido;
import br.com.pedidovenda.service.CancelamentoPedidoService;
import br.com.pedidovenda.util.jsf.FacesUtil;

@Named
@RequestScoped
public class CancelamentoPedidoBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Inject
	private CancelamentoPedidoService cancelamentoPedidoService;
	
	//cria evento do CDI para atualizar o pedido que esta no CadastroPedidoBean, 
	//isso irá atualizar o pedido que foi produzido em CadastroPedidoBean e atualizado em CancelamentoPedidoBean
	@Inject
	private Event<PedidoAlteradoEvent> pedidoAlteradoEvent;
	
	//injeta o Pedido com a mesma referencia que foi produzido no bean CadastroPedidoBean
	@Inject
	@PedidoEdicao
	private Pedido pedido;
	
	public void cancelar(){
		
		this.pedido = this.cancelamentoPedidoService.cancelar(this.pedido);
		this.pedidoAlteradoEvent.fire(new PedidoAlteradoEvent(this.pedido));
		
		FacesUtil.addInfoMessage("Pedido cancelado com sucesso!");
		
	}

}
