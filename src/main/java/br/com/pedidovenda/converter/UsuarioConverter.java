package br.com.pedidovenda.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.pedidovenda.model.Usuario;
import br.com.pedidovenda.repository.UsuarioRepository;
import br.com.pedidovenda.util.cdi.CDIServiceLocator;

@FacesConverter(forClass=Usuario.class)
public class UsuarioConverter implements Converter{

	//@Inject NÃO É POSSIVEL INJETAR DEPENDENCIAS EM CONVERSORES
	private UsuarioRepository usuarioRepositorio;
	
	public UsuarioConverter(){
		//USADO A CLASSE CDIServiceLocator COMO ALTERNATIVA A INJEÇÃO DE DEPENDENCIA
		usuarioRepositorio = CDIServiceLocator.getBean(UsuarioRepository.class);
	}
	
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String value) {
		
		if (value!=null ){			
			return usuarioRepositorio.porId(new Long(value));
		}
		return null;
		
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
		
		if (value != null){
			Usuario usuario = (Usuario) value;
			return usuario.getId() ==null ? null : usuario.getId().toString();
		}
		return "";
	}

}
