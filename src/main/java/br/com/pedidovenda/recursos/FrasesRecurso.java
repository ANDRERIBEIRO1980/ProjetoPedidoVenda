package br.com.pedidovenda.recursos;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.eclipse.persistence.jaxb.MarshallerProperties;

import com.google.gson.Gson;


@Path("frase")
public class FrasesRecurso {
	
	public static List<Frase> frases = new ArrayList<>();
	
	public EntityManager manager = Persistence.createEntityManagerFactory("PedidoPU").createEntityManager();
	
	static{
		
		Frase frase0 = new Frase(0,"Alura, Cursos online de tecnologia que reinventam sua carreira.",10);
		Frase frase1 = new Frase(1,"Debuggers não consertam erros, apenas os exibem em slow motion.",20);
		Frase frase2 = new Frase(2,"Caelum, Ensino e Inovação.",30);
		Frase frase3 = new Frase(3,"Existem duas tarefas difíceis na Ciência da Computação: invalidação de cache e nomear as coisas.",40);
		Frase frase4 = new Frase(4,"Ciência da computação é tão sobre computadores quanto astronomia é sobre telescópios.",35);
		Frase frase5 = new Frase(5,"Na minha máquina funciona.",5);
		Frase frase6 = new Frase(6,"Hardware é o que você chuta, software é o que você xinga.",7);		
		
		frases.add(frase0);
		frases.add(frase1);
		frases.add(frase2);
		frases.add(frase3);
		frases.add(frase4);
		frases.add(frase5);
		frases.add(frase6);
		
	}
	

	
	
	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Response buscarPorId(@PathParam("id") int id){
		
		    System.out.println("buscarPorId: " + id);
		    
		    try {
				
				JAXBContext jc;
				
				jc = JAXBContext.newInstance(Frase.class);
				
				// Create the Marshaller Object using the JaxB Context
		        Marshaller marshaller = jc.createMarshaller();
		        
		        // Set the Marshaller media type to JSON or XML
		        marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
		        
		        // Set it to true if you need to include the JSON root element in the JSON output
		        marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, true);
		        
		        // Set it to true if you need the JSON output to formatted
		        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		        
		        // Marshal the employee object to JSON and print the output to console
		        StringWriter writer = new StringWriter();
		        
		        try{		        
		        	Frase frase = frases.get(id);
			        marshaller.marshal(frase, writer);
			        return Response.ok(writer.toString(), MediaType.APPLICATION_JSON).build();		        	
		        } catch (Exception e){
		        	//return Response.ok("Frase nao encontrada", MediaType.APPLICATION_JSON).build();
		        	return Response.status(404).entity("Frase não localizada.").build();
		        }
		        
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	       return null;
		  
		
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscar(){
		
		System.out.println("buscar todos:");
		
		try {
			
			JAXBContext jc;
			
			jc = JAXBContext.newInstance(Frase.class);
			
			// Create the Marshaller Object using the JaxB Context
	        Marshaller marshaller = jc.createMarshaller();
	        
	        // Set the Marshaller media type to JSON or XML
	        marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
	        
	        // Set it to true if you need to include the JSON root element in the JSON output
	        marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, true);
	        
	        // Set it to true if you need the JSON output to formatted
	        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	        
	        // Marshal the employee object to JSON and print the output to console
	        StringWriter writer = new StringWriter();
	        marshaller.marshal(frases, writer);
	        
	        System.out.println(writer);
	        //return writer.toString();
	        return Response.ok(writer.toString(), MediaType.APPLICATION_JSON).build();
	        
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
       return null;		
	}	
	
	public void salvaPlacar(List<Placar> listaPlacar){		
		
		manager.getTransaction().begin();		
		for (Placar placar:listaPlacar){			
			Placar p = new Placar();
			p = placar;
			System.out.println("salvando placarzozo da lista: " + p);
			manager.merge(p);	 

		}
		manager.getTransaction().commit();
		
	}
	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String salvaPlacar(String conteudo){
		
		Gson gson = new Gson();	
		System.out.println(conteudo);
		Placar[] p = gson.fromJson(conteudo, Placar[].class);		
		List<Placar> placar = Arrays.asList(p);		
		salvaPlacar(placar);
		
		//URI uri = URI.create("/placar/" + 1);
		//return Response.created(uri).build();
		
		return "Ok";
	}
	
	public List<Placar> buscaPlacarDB() {
		return manager.createQuery("from Placar", Placar.class).getResultList();		
	}	
	
	@GET
	@Path("/placar")
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscaPlacar(){
		
		System.out.println("busca placar zozo:");
		
		List<Placar> listaPlacar = buscaPlacarDB();
		
		try {			
			JAXBContext jc;			
			jc = JAXBContext.newInstance(Placar.class);			
	        Marshaller marshaller = jc.createMarshaller();
	        marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
	        marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, true);
	        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	        StringWriter writer = new StringWriter();
	        marshaller.marshal(listaPlacar, writer);
	        
	        System.out.println(writer);
	        return Response.ok(writer.toString(), MediaType.APPLICATION_JSON).build();
	        
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
       return null;		
	}	
	
	
	@Path("{id}")
	@DELETE
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})		
	public Response removePlacar(@PathParam("id") long id){
		
			    
		Placar placar = manager.find(Placar.class, id);
		manager.getTransaction().begin();		
     	manager.remove(placar);	 
		manager.getTransaction().commit();
		System.out.println("excluido id=" + id);
		return Response.ok().build();
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