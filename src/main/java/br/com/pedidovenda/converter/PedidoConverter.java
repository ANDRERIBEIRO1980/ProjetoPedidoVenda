package br.com.pedidovenda.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.pedidovenda.model.Pedido;
import br.com.pedidovenda.repository.PedidoRepository;
import br.com.pedidovenda.util.cdi.CDIServiceLocator;

@FacesConverter(forClass=Pedido.class)
public class PedidoConverter implements Converter{

	//@Inject NÃO É POSSIVEL INJETAR DEPENDENCIAS EM CONVERSORES
	private PedidoRepository pedidoRepositorio;
	
	public PedidoConverter(){
		//USADO A CLASSE CDIServiceLocator COMO ALTERNATIVA A INJEÇÃO DE DEPENDENCIA
		pedidoRepositorio = CDIServiceLocator.getBean(PedidoRepository.class);
	}
	
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String value) {
		
		if (value!=null ){			
			return pedidoRepositorio.porId(new Long(value));
		}
		return null;
		
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
		
		if (value != null){
			Pedido pedido = (Pedido) value;
			return pedido.getId() ==null ? null : pedido.getId().toString();
		}
		return "";
	}

}
