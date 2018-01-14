package br.com.pedidovenda.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.pedidovenda.model.Produto;
import br.com.pedidovenda.repository.ProdutoRepository;
import br.com.pedidovenda.util.cdi.CDIServiceLocator;

@FacesConverter(forClass=Produto.class)
public class ProdutoConverter implements Converter{

	//@Inject NÃO É POSSIVEL INJETAR DEPENDENCIAS EM CONVERSORES
	private ProdutoRepository produtoRepositorio;
	
	public ProdutoConverter(){
		//USADO A CLASSE CDIServiceLocator COMO ALTERNATIVA A INJEÇÃO DE DEPENDENCIA
		produtoRepositorio = CDIServiceLocator.getBean(ProdutoRepository.class);
	}
	
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String value) {
		
		if (value!=null ){			
			return produtoRepositorio.porId(new Long(value));
		}
		return null;
		
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
		
		if (value != null){
			Produto produto = (Produto) value;
			return produto.getId() ==null ? null : produto.getId().toString();
		}
		return "";
	}

}
