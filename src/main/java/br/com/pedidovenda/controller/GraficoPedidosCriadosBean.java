package br.com.pedidovenda.controller;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.PieChartModel;

import br.com.pedidovenda.repository.PedidoRepository;
import br.com.pedidovenda.security.UsuarioLogado;
import br.com.pedidovenda.security.UsuarioSistema;

@Named
@RequestScoped
public class GraficoPedidosCriadosBean {

		private static final int QTDE_DIAS_RELATORIO = 15;

		private static DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM");
	
		@Inject
		@UsuarioLogado
		private UsuarioSistema usuarioLogado;
		
		@Inject
		private PedidoRepository pedidoRepositorio;
	
	    private LineChartModel lineModel1 ;
	    
	    private PieChartModel pieModel;
	     
	    public void preRender() {
	        createLineModels();
	        createPieModel();
	    }
	 
	    private void createPieModel() {
	    	pieModel = new PieChartModel();
	         
	    	List<Object[]> resultado = pedidoRepositorio.totalVendasPorVendedor(QTDE_DIAS_RELATORIO);
	    	for (Object[] linha : resultado){
	    		pieModel.set( linha[0].toString() , new BigDecimal( linha[1].toString() ));
	    	}
	        pieModel.setTitle("Total de Vendas por Vendedor");	        
	        pieModel.setLegendPosition("e");	        
	        pieModel.setFill(false);
	        pieModel.setShowDataLabels(true);
	        pieModel.setDiameter(150);
	    }
	    
	    private void createLineModels() {
	    	lineModel1 = initLinearModel();	    	
	        lineModel1.setTitle("Pedidos Criados");
	        lineModel1.setLegendPosition("e");
	        lineModel1.setAnimate(true);	        
	        this.lineModel1.getAxes().put(AxisType.X, new CategoryAxis());	        
	    }
	     
	    private LineChartModel initLinearModel() {
	        
	    	LineChartModel model = new LineChartModel();
	 
	        LineChartSeries series = new LineChartSeries();
	        series.setLabel("Todos os Pedidos");	 
	        Map<Date, BigDecimal> valoresPorData = this.pedidoRepositorio.valoresTotaisPorDataJPQL(QTDE_DIAS_RELATORIO, null);	        
			for (Date data : valoresPorData.keySet()) {
	        	series.set(DATE_FORMAT.format(data), valoresPorData.get(data));
			}
			
			LineChartSeries series2 = new LineChartSeries();
  		    series2.setLabel("Meus Pedidos");	 
	        Map<Date, BigDecimal> valoresPorData2 = this.pedidoRepositorio.valoresTotaisPorDataJPQL(QTDE_DIAS_RELATORIO, usuarioLogado.getUsuario());
	        for (Date data : valoresPorData.keySet()) {	    
	        	series2.set(DATE_FORMAT.format(data), valoresPorData2.get(data));
			}
	   	        
			model.addSeries(series);
			model.addSeries(series2);
	        return model;
	        
	    }

	    public LineChartModel getLineModel1() {
	        return lineModel1;
	    }
	    
	    public void setLineModel1(LineChartModel lineModel1) {
			this.lineModel1 = lineModel1;
		}
	    
	    public PieChartModel getPieModel() {
			return pieModel;
		}
	    
	    
}
