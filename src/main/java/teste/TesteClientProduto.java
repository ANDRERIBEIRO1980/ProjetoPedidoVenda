package teste;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import com.thoughtworks.xstream.XStream;

import br.com.pedidovenda.model.Produto;


public class TesteClientProduto {

	public static void main (String args[]){
		
		System.out.println("testaCarrinho");
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8080/PedidoVenda");
        
        //unico objeto
        //String conteudo = target.path("/recursos/produto/3").request().get(String.class);
        //Produto produto = (Produto) new XStream().fromXML(conteudo);                  
        //System.out.println(conteudo);
        
        //colecao list de objetos
        String conteudo = target.path("/recursos/produto/3").request().get(String.class);
        List<Produto> produtos = (List<Produto>) new XStream().fromXML(conteudo);
        for (Produto p: produtos){
        	System.out.println("Produto: " + p);
        }
        
        //List<Produto> produtos = target.path("/produtos").request().get(new GenericType<List<Produto>>(){});
        //List<Produto> carrinhos = target.path("/produto/").request().get(new GenericType<List<Produto>>));
        
        
    }
	
}
