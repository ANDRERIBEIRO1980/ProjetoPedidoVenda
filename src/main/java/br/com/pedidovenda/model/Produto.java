package br.com.pedidovenda.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.thoughtworks.xstream.XStream;
import br.com.pedidovenda.service.NegocioException;
import br.com.pedidovenda.validation.SKU;

@Entity
public class Produto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String nome;
	private String sku;
	private BigDecimal valorUnitario;
	private Integer quantidadeEstoque;
	private Categoria categoria;
	
	
	@Id
	@SequenceGenerator(name="produto_seq" , sequenceName="PRODUTO_SEQ" , initialValue=1)
	@GeneratedValue(strategy=GenerationType.AUTO, generator="produto_seq")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@NotBlank
	@Size(max=80)
	@Column(nullable=false, length=80)
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
	/*@Pattern(regexp="([a-zA-Z]{2}\\d{4,18})?")*/
	@NotBlank	
	@SKU
	@Column(nullable=false, length=20, unique=true)
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}	
	
	@NotNull
	@Min(value=1, message="deve ser acima ou igual a R$ 1,00")
	@Column( name="valor_unitario", nullable=false, precision=10, scale=2)
	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}
	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}
	
	@NotNull(message="é obrigatório") //mensagem customizada para esse atributo
	@Min(0)
	@Max(value=9999, message="tem um valor muito alto")
	@Column(name="quantidade_estoque", nullable=false, length=5)
	public Integer getQuantidadeEstoque() {
		return quantidadeEstoque;
	}
	public void setQuantidadeEstoque(Integer quantidadeEstoque) {
		this.quantidadeEstoque = quantidadeEstoque;
	}
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "categoria_id", nullable=false)
	public Categoria getCategoria() {
		return categoria;
	}
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Produto other = (Produto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Produto [id=" + id + ", nome=" + nome + ", sku=" + sku + ", valorUnitario=" + valorUnitario
				+ ", quantidadeEstoque=" + quantidadeEstoque + ", categoria=" + categoria + "]";
	}
	
	public void baixarEstoque(Integer quantidade) {

			int novaQuantidade = this.getQuantidadeEstoque() - quantidade;
			
			if (novaQuantidade<0){
				throw new NegocioException("Não há disponibilidade de " + 
			                quantidade + " itens do produto " + this.getSku());
			}
			
			this.setQuantidadeEstoque(novaQuantidade);
		
	}
	
	public void adicionarEstoque(Integer quantidade) {
		
		this.setQuantidadeEstoque(getQuantidadeEstoque() + quantidade);
		
	}
	public Object toXML() {
		
		XStream xstream = new XStream();
		String xmlGerado = xstream.toXML(this);
		return xmlGerado;
		
	}
	
	
	
	

}
