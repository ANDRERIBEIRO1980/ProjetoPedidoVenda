package teste;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import br.com.pedidovenda.model.Cliente;
import br.com.pedidovenda.model.EnderecoEntrega;
import br.com.pedidovenda.model.FormaPagamento;
import br.com.pedidovenda.model.ItemPedido;
import br.com.pedidovenda.model.Pedido;
import br.com.pedidovenda.model.Produto;
import br.com.pedidovenda.model.StatusPedido;
import br.com.pedidovenda.model.Usuario;

public class TestePedido {
	public static void main(String[] args) {
		
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("PedidoPU");
		EntityManager manager = factory.createEntityManager();
		
		EntityTransaction trx = manager.getTransaction();
		trx.begin();
		
		Cliente cliente = manager.find(Cliente.class, 2L);
		Produto produto = manager.find(Produto.class, 3L);
		Usuario vendedor = manager.find(Usuario.class, 2L);
		
		EnderecoEntrega enderecoEntrega = new EnderecoEntrega();
		enderecoEntrega.setLogradouro("Rua dos Mercados");
		enderecoEntrega.setNumero("1000");
		enderecoEntrega.setCidade("Uberlândia");
		enderecoEntrega.setUf("MG");
		enderecoEntrega.setCep("38400-123");
		
		Pedido pedido = new Pedido();
		pedido.setCliente(cliente);
		pedido.setDataCriacao(new Date());
		pedido.setDataEntrega(new Date());
		pedido.setFormaPagamento(FormaPagamento.CARTAO_CREDITO);
		pedido.setObservacao("Aberto das 08 às 18h");
		pedido.setStatus(StatusPedido.ORCAMENTO);
		pedido.setValorDesconto(BigDecimal.ZERO);
		pedido.setValorFrete(BigDecimal.ZERO);
		pedido.setValorTotal(new BigDecimal("23.2"));
		pedido.setVendedor(vendedor);
		pedido.setEnderecoEntrega(enderecoEntrega);
		
		ItemPedido item = new ItemPedido();
		item.setProduto(produto);
		item.setQuantidade(10);
		item.setValorUnitario(new BigDecimal("2.32"));
		item.setPedido(pedido);
		
		pedido.getItens().add(item);
		
		manager.persist(pedido);
		
		trx.commit();
}
}
