package br.com.pedidovenda.controller;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.hibernate.Session;

import br.com.pedidovenda.util.jsf.FacesUtil;
import br.com.pedidovenda.util.report.ExecutorRelatorio;

@Named
@RequestScoped
public class RelatoriosBean implements Serializable {

	private static final long serialVersionUID = 1L;
    
	//parametros do relatório de pedidos emitidos
	private Date dataInicio;
	private Date dataFim;
	
	//parametros do relatório de produtos
	private BigDecimal valorUnitarioDe;
	private BigDecimal valorUnitarioAte;
	

	@Inject
	private FacesContext facesContext;

	@Inject
	private HttpServletResponse response;

	@Inject
	private EntityManager manager;

	public void abreRelatorio(String relatorio) {
		
		Map<String, Object> parametros = new HashMap<>();
		ExecutorRelatorio executor = null;
		
		if (relatorio.equals("PedidosEmitidos")){
			parametros.put("data_inicio", this.dataInicio);
			parametros.put("data_fim", this.dataFim);		
			executor = new ExecutorRelatorio("/relatorios/relatorio_pedidos_emitidos.jasper",
					this.response, parametros/*, "Pedidos emitidos.pdf"*/);	
		}
		
		if (relatorio.equals("Produtos")){
			parametros.put("valorUnitarioDe", this.valorUnitarioDe);
			parametros.put("valorUnitarioAte", this.valorUnitarioAte);		
			executor = new ExecutorRelatorio("/relatorios/relatorio_produtos.jasper",
					this.response, parametros/*, "Produtos.pdf"*/);	
		}
		
		Session session = manager.unwrap(Session.class);
		session.doWork(executor);
		
		if (executor.isRelatorioGerado()) {
			facesContext.responseComplete();
		} else {

			FacesUtil.addErrorMessage("A execução do relatório não retornou dados.");
			
			//comando para permitir a exibição da mensagem em outra pagina após o redirect usando flash scope
			FacesContext.getCurrentInstance()
		      .getExternalContext()
		      .getFlash().setKeepMessages(true);
			
			//também é possivel incluir um objeto no escopo Flash
			FacesContext.getCurrentInstance()
		      .getExternalContext()
		      .getFlash().put("parametro", this); 
			
			redirect("/relatorios/RelatorioVazio.xhtml?faces-redirect=true");
			                                          
			
		}
	}

	private void redirect(String page) {
		try {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ExternalContext externalContext = facesContext.getExternalContext();
			String contextPath = externalContext.getRequestContextPath();
	
			externalContext.redirect(contextPath + page);
			facesContext.responseComplete();
		} catch (IOException e) {
			throw new FacesException(e);
		}
	}
	
	@NotNull
	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	@NotNull
	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public BigDecimal getValorUnitarioDe() {
		return valorUnitarioDe;
	}

	public void setValorUnitarioDe(BigDecimal valorUnitarioDe) {
		this.valorUnitarioDe = valorUnitarioDe;
	}

	public BigDecimal getValorUnitarioAte() {
		return valorUnitarioAte;
	}

	public void setValorUnitarioAte(BigDecimal valorUnitarioAte) {
		this.valorUnitarioAte = valorUnitarioAte;
	}

	@Override
	public String toString() {
		return "RelatoriosBean [dataInicio=" + dataInicio + ", dataFim=" + dataFim + ", valorUnitarioDe="
				+ valorUnitarioDe + ", valorUnitarioAte=" + valorUnitarioAte + ", facesContext=" + facesContext
				+ ", response=" + response + ", manager=" + manager + "]";
	}

	
	
}