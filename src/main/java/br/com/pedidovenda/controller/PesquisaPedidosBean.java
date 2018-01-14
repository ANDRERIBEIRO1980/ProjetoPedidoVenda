package br.com.pedidovenda.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

/*import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
*/
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/*import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
*/
import br.com.pedidovenda.model.Pedido;
import br.com.pedidovenda.model.StatusPedido;
import br.com.pedidovenda.repository.PedidoRepository;
import br.com.pedidovenda.repository.filter.PedidoFilter;

@Named
@javax.faces.view.ViewScoped
public class PesquisaPedidosBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private PedidoRepository pedidoRepositorio;
	
	private PedidoFilter filtro;
	private LazyDataModel<Pedido> model;
	
	public PesquisaPedidosBean() {
		
		filtro = new PedidoFilter();
		
		model = new LazyDataModel<Pedido>() {

			private static final long serialVersionUID = 1L;
			
			@Override
			public List<Pedido> load(int first, int pageSize, 
					String sortField, SortOrder sortOrder, 
					Map<String, Object> filters) {
				
				filtro.setPrimeiroRegistro(first);
				filtro.setQuantidadeRegistros(pageSize);
				filtro.setPropriedadeOrdenacao(sortField);
				filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));
				
				setRowCount(pedidoRepositorio.quantidadeFiltrados(filtro));
				
				return pedidoRepositorio.filtrados(filtro);
			}
			
		};
	}
	
	/*public void preProcessPDF(Object document) throws IOException, BadElementException, DocumentException {
		 
		Document pdf = (Document) document;
		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		//String logo = servletContext.getRealPath("") + File.separator + "images" + File.separator + "alerta.png";
		
		//D:\AmbienteEclipseNeon\ProjetoPedidoVenda\src\main\webapp\resources\images\alerta.png
		
		String logo = "D:" + File.separator + "AmbienteEclipseNeon" + File.separator + "ProjetoPedidoVenda" + File.separator +
				       "src" + File.separator + "main" + File.separator + "webapp" + File.separator + 
				            "resources" + File.separator + "images" + File.separator + "alerta.png";
		System.out.println("logo="+logo);
		pdf.add(Image.getInstance(logo));
		
	}
	
	public void posProcessarXls(Object documento) {
		
		HSSFWorkbook planilha = (HSSFWorkbook) documento;
		HSSFSheet folha = planilha.getSheetAt(0);
		HSSFRow cabecalho = folha.getRow(0);
		HSSFCellStyle estiloCelula = planilha.createCellStyle();
		Font fonteCabecalho = planilha.createFont();
		
		fonteCabecalho.setColor(IndexedColors.WHITE.getIndex());
		fonteCabecalho.setBold(true);		
		fonteCabecalho.setFontHeightInPoints((short) 16);
		
		estiloCelula.setFont(fonteCabecalho);
		estiloCelula.setFillForegroundColor(IndexedColors.BLACK.getIndex());
		estiloCelula.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		
		for (int i = 0; i < cabecalho.getPhysicalNumberOfCells(); i++) {
			cabecalho.getCell(i).setCellStyle(estiloCelula);
			
		}
		
	}*/
	
	public StatusPedido[] getStatuses() {
		return StatusPedido.values();
	}
	
	public PedidoFilter getFiltro() {
		return filtro;
	}
	
	public LazyDataModel<Pedido> getModel() {
		return model;
	}
	public void setModel(LazyDataModel<Pedido> model) {
		this.model = model;
	}
}