package br.com.pedidovenda.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.pedidovenda.model.Grupo;
import br.com.pedidovenda.repository.GrupoRepository;
import br.com.pedidovenda.util.cdi.CDIServiceLocator;

@FacesConverter(forClass=Grupo.class)
public class GrupoConverter implements Converter{

	//@Inject NÃO É POSSIVEL INJETAR DEPENDENCIAS EM CONVERSORES
	private GrupoRepository grupoRepositorio;
	
	public GrupoConverter(){
		//USADO A CLASSE CDIServiceLocator COMO ALTERNATIVA A INJEÇÃO DE DEPENDENCIA
		grupoRepositorio = CDIServiceLocator.getBean(GrupoRepository.class);
	}
	
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String value) {
		
		if (value!=null ){			
			return grupoRepositorio.porId(new Long(value));
		}
		return null;
		
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
		
		if (value != null){
			Grupo grupo = (Grupo) value;
			return grupo.getId() ==null ? null : grupo.getId().toString();
		}
		return "";
	}

}
