package br.com.pedidovenda.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.pedidovenda.model.Cliente;
import br.com.pedidovenda.repository.ClienteRepository;
import br.com.pedidovenda.util.cdi.CDIServiceLocator;

@FacesConverter(forClass=Cliente.class)
public class ClienteConverter implements Converter{

	//@Inject NÃO É POSSIVEL INJETAR DEPENDENCIAS EM CONVERSORES
	private ClienteRepository clienteRepositorio;
	
	public ClienteConverter(){
		//USADO A CLASSE CDIServiceLocator COMO ALTERNATIVA A INJEÇÃO DE DEPENDENCIA
		clienteRepositorio = CDIServiceLocator.getBean(ClienteRepository.class);
	}
	
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String value) {
		
		if (value!=null ){	
			Long codigo;
			try{
				codigo = new Long(value);	
			  } catch (Exception e)
			{
				return null;  
			}
			
			return clienteRepositorio.porId(codigo);
		}
		return null;
		
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
		
		if (value != null){
			Cliente cliente = (Cliente) value;
			return cliente.getId() ==null ? null : cliente.getId().toString();
		}
		return "";
	}

}
