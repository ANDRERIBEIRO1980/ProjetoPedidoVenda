package br.com.pedidovenda.controller;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.SelectEvent;

import br.com.pedidovenda.model.Cliente;
import br.com.pedidovenda.model.EnderecoEntrega;
import br.com.pedidovenda.model.FormaPagamento;
import br.com.pedidovenda.model.ItemPedido;
import br.com.pedidovenda.model.Pedido;
import br.com.pedidovenda.model.Produto;
import br.com.pedidovenda.model.Usuario;
import br.com.pedidovenda.repository.ClienteRepository;
import br.com.pedidovenda.repository.ProdutoRepository;
import br.com.pedidovenda.repository.UsuarioRepository;
import br.com.pedidovenda.service.NegocioException;
import br.com.pedidovenda.service.PedidoService;
import br.com.pedidovenda.util.jsf.FacesUtil;
import br.com.pedidovenda.validation.SKU;

@Named
@javax.faces.view.ViewScoped
public class CadastroPedidoBean  implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private UsuarioRepository usuarioRepositorio;
	
	@Inject
	private ClienteRepository clienteRepositorio;
	
	@Inject
	private PedidoService pedidoService;
	
	@Inject
	private ProdutoRepository produtoRepositorio;
	
	private String sku;
	
	//produz um pedido que é a referencia em CadastroPedidoBean
	//essa referencia poderá ser injeta atraves de @Inject @PedidoEdicao em outros beans como EmissaoPedidoBean ou CancelamentoPedidoBean por exemplo	
	@Produces 
	@PedidoEdicao
	private Pedido pedido;	
	
	private List<Usuario> vendedores;
	
	private Produto produtoLinhaEditavel;
	
	public CadastroPedidoBean() {		
		limpar();		
	}
	
	public void inicializar(){		
		
		vendedores = usuarioRepositorio.vendedores();	
		pedido.adicionarItemVazio();
		recalcularPedido();
		
	}
	
	public void clienteSelecionado(SelectEvent event) {	
		pedido.setCliente((Cliente) event.getObject());
	}
	
	public List<Cliente> completarCliente(String nome) {
		return this.clienteRepositorio.porNome(nome);
	}

	public FormaPagamento[] getFormasPagamento() {
		return FormaPagamento.values();
	}
	
	public void limpar(){
		pedido = new Pedido();	
		pedido.setEnderecoEntrega(new EnderecoEntrega());
	}
	
	//escuta o evento de emissao de pedido e atualiza o pedido aqui no bean CadastroPedidoBean
	//esse evento será lancado por EmissaoPedidoBean quando realizar a emissao de pedido
	public void pedidoAlterado(@Observes PedidoAlteradoEvent event){
		this.pedido = event.getPedido();
	}
	
	public void salvar() {
		
		this.pedido.removerItemVazio();
		
		try {
			this.pedido = this.pedidoService.salvar(this.pedido);
		
			FacesUtil.addInfoMessage("Pedido salvo com sucesso!");
		} finally {
			this.pedido.adicionarItemVazio();
		}
	}
	
	public List<Produto> completarProduto(String nome) {
		return this.produtoRepositorio.porNome(nome);
	}
	
	public void carregarProdutoPorSku() {
		if (StringUtils.isNotEmpty(this.sku)) {
			this.produtoLinhaEditavel = this.produtoRepositorio.porSku(this.sku);
			if (this.produtoLinhaEditavel==null){
				throw new NegocioException("O códido do SKU informado não foi localizado.");
			}
			this.carregarProdutoLinhaEditavel();
		}
	}
	
	public void carregarProdutoLinhaEditavel() {
		
		ItemPedido item = this.pedido.getItens().get(0);
		
		if (this.produtoLinhaEditavel != null) {
			
			if (this.existeItemComProduto(this.produtoLinhaEditavel)) {
				FacesUtil.addErrorMessage("Já existe um item no pedido com o produto informado.");
			} else {
				item.setProduto(this.produtoLinhaEditavel);
				item.setValorUnitario(this.produtoLinhaEditavel.getValorUnitario());
				
				this.pedido.adicionarItemVazio();
				this.produtoLinhaEditavel = null;
				this.sku = null;
				
				this.pedido.recalcularValorTotal();
			}
			
		}
	}
	
	public void atualizarQuantidade(ItemPedido item, int linha) {
		if (item.getQuantidade() < 1) {
			//nao pode excluir a primeira linha, com o index 0 pois é a linha com os inputs para adcionar novo item
			if (linha == 0) {
				item.setQuantidade(1);
			} else {
				this.getPedido().getItens().remove(linha);
			}
		}
		
		this.pedido.recalcularValorTotal();
	}
	
	private boolean existeItemComProduto(Produto produto) {
		boolean existeItem = false;
		
		for (ItemPedido item : pedido.getItens()) {
			if (produto.equals(item.getProduto())) {
				existeItem = true;
				break;
			}
		}
		
		return existeItem;
	}
	
	public void recalcularPedido() {
		if (this.pedido != null) {
			this.pedido.recalcularValorTotal();
		}
	}
	
	public Pedido getPedido() {
		return pedido;
	}
	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}
	
	public List<Usuario> getVendedores() {
		return vendedores;
	}
	public void setVendedores(List<Usuario> vendedores) {
		this.vendedores = vendedores;
	}
	
	public boolean isEditando(){
		return pedido.getId()!=null;
	}
	
	public Produto getProdutoLinhaEditavel() {
		return produtoLinhaEditavel;
	}
	public void setProdutoLinhaEditavel(Produto produtoLinhaEditavel) {
		this.produtoLinhaEditavel = produtoLinhaEditavel;
	}
	
	@SKU
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
}