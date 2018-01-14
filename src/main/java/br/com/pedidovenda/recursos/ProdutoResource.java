package br.com.pedidovenda.recursos;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.pedidovenda.model.Produto;
import br.com.pedidovenda.repository.ProdutoRepository;

@Path("produto")
public class ProdutoResource {
	
	@Inject
	private ProdutoRepository produtos;
	
	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Response buscar(@PathParam("id") Long id){
		
		Produto produto = produtos.porId(id);

		if (produto!=null){
			String xmlGerado = (String) produto.toXML();
			return Response.ok(xmlGerado, MediaType.APPLICATION_XML).build();
		}				
		else{
			return Response.status(404).entity("Produto não localizado.").build();
		}	
		
	}
	
/*	@POST
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_XML)
	public Response adiciona(String conteudo){
		
		Carrinho carrinho = (Carrinho) new XStream().fromXML(conteudo);
		System.out.println("carrinhozozo: " + carrinho);
		//rotina para salvar carrinho
		URI uri = URI.create("/carrinhos/" + carrinho.getId());
		return Response.created(uri).build();
	}
	
	@Path("{id}/produtos/{produtoId}")
	@DELETE
	public Response removeProduto(@PathParam("id") long id,
								@PathParam("produtoId") long produtoId ){
		
		System.out.println("parametro para exclusao id=" + id + " produtoId=" + produtoId);
		Carrinho carrinho = new CarrinhoDAO().busca(id);
		carrinho.remove(produtoId);
		
		return Response.ok().build();
	}
	
	//nessa uri irá alterar somente a quantidade, para evitar que se altere o preco
	//forçando a alteração somente no campo quantidade
	@Path("{id}/produtos/{produtoId}/quantidade")
	@PUT
	public Response alteraProduto(String conteudo,
			                      @PathParam("id") long id,
	   							  @PathParam("produtoId") long produtoId ){
		
		System.out.println("parametro para alteracao id= " + id + " produtoId=" + produtoId);
		System.out.println("conteudo para alteracao = " + conteudo);
		
		Carrinho carrinho = new CarrinhoDAO().busca(id);
		Produto produto = (Produto) new XStream().fromXML(conteudo);
		carrinho.trocaQuantidade(produto);
		
		return Response.ok().build();
	}
	*/
}