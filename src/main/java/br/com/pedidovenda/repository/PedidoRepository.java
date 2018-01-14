package br.com.pedidovenda.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import br.com.pedidovenda.model.Pedido;
import br.com.pedidovenda.model.Usuario;
import br.com.pedidovenda.model.vo.DataValor;
import br.com.pedidovenda.repository.filter.PedidoFilter;

@RequestScoped
public class PedidoRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	@SuppressWarnings({ "unchecked" })
	public Map<Date, BigDecimal> valoresTotaisPorData(Integer numeroDeDias, Usuario criadoPor) {
		Session session = manager.unwrap(Session.class);
		
		numeroDeDias -= 1;
		
		Calendar dataInicial = Calendar.getInstance();
		dataInicial = DateUtils.truncate(dataInicial, Calendar.DAY_OF_MONTH);
		dataInicial.add(Calendar.DAY_OF_MONTH, numeroDeDias * -1);
		
		Map<Date, BigDecimal> resultado = criarMapaVazio(numeroDeDias, dataInicial);
		
		Criteria criteria = session.createCriteria(Pedido.class);
		
		// select date(data_criacao) as data, sum(valor_total) as valor 
		// from pedido where data_criacao >= :dataInicial and vendedor_id = :criadoPor 
		// group by date(data_criacao)
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.sqlGroupProjection("to_date(data_criacao) as data", 
						"to_date(data_criacao)", new String[] { "data" }, 
						new Type[] { StandardBasicTypes.DATE } ))
				.add(Projections.sum("valorTotal").as("valor"))
			)
			.add(Restrictions.ge("dataCriacao", dataInicial.getTime()));
		
		if (criadoPor != null) {
			criteria.add(Restrictions.eq("vendedor", criadoPor));
		}
		
		List<DataValor> valoresPorData = criteria
				.setResultTransformer(Transformers.aliasToBean(DataValor.class)).list();
		
		System.out.println("dados");
		for (DataValor dataValor : valoresPorData) {
			resultado.put(dataValor.getData(), dataValor.getValor());
		}
		
		return resultado;
	}

	private Calendar retornaData(Integer numeroDeDias){
		Calendar dataInicial = Calendar.getInstance();
		dataInicial = DateUtils.truncate(dataInicial, Calendar.DAY_OF_MONTH);
		dataInicial.add(Calendar.DAY_OF_MONTH, numeroDeDias * -1);
		return dataInicial;
	}
	
	
	@SuppressWarnings("unchecked")
	public Map<Date, BigDecimal> valoresTotaisPorDataJPQL(Integer numeroDeDias, Usuario criadoPor) {
		numeroDeDias -= 1;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Calendar dataInicial = retornaData(numeroDeDias);
		Map<Date, BigDecimal> resultado = criarMapaVazio(numeroDeDias, dataInicial);
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("select new br.com.pedidovenda.model.vo.DataValor(trunc(p.dataCriacao) , sum(p.valorTotal)) "
				  + " from Pedido p join p.vendedor v"
				 + " where p.dataCriacao >= to_date('" + sdf.format(dataInicial.getTime()) + "', 'DD/MM/YYYY')");
		
				  if (criadoPor!=null){
					sql.append(" and v.id = " + criadoPor.getId());	
					}				  
				  
				 sql.append(" group by trunc(p.dataCriacao) ");

	    List<DataValor> valoresPorData = null;
		try{
			valoresPorData = manager.createQuery(sql.toString()).getResultList();	
			for (DataValor dataValor : valoresPorData) {
				resultado.put(dataValor.getData(), dataValor.getValor());
			}
		} catch (Exception e){
			System.out.println(e.getMessage());		
		}		
		return resultado;
	}

	public List<Object[]> totalVendasPorVendedor(Integer numeroDeDias){		
		numeroDeDias -= 1;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Calendar dataInicial = retornaData(numeroDeDias);
		return manager.createQuery("select v.nome, sum(p.valorTotal) from Pedido p join p.vendedor v where p.dataCriacao >= to_date('" + sdf.format(dataInicial.getTime()) + "', 'DD/MM/YYYY') group by v.nome", Object[].class).getResultList();		
	}
	
	private Map<Date, BigDecimal> criarMapaVazio(Integer numeroDeDias,
			Calendar dataInicial) {
		dataInicial = (Calendar) dataInicial.clone();
		Map<Date, BigDecimal> mapaInicial = new TreeMap<>();

		for (int i = 0; i <= numeroDeDias; i++) {
			mapaInicial.put(dataInicial.getTime(), BigDecimal.ZERO);
			dataInicial.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		return mapaInicial;
	}
	
	@SuppressWarnings("unchecked")
	public List<Pedido> filtrados(PedidoFilter filtro) {
		
		Criteria criteria = criarCriteriaParaFiltro(filtro);
		
		criteria.setFirstResult(filtro.getPrimeiroRegistro());
		criteria.setMaxResults(filtro.getQuantidadeRegistros());
		
		if (filtro.isAscendente() && filtro.getPropriedadeOrdenacao() != null) {
			criteria.addOrder(Order.asc(filtro.getPropriedadeOrdenacao()));
		} else if (filtro.getPropriedadeOrdenacao() != null) {
			criteria.addOrder(Order.desc(filtro.getPropriedadeOrdenacao()));
		}
		
		return criteria.list();
	}
	
	public int quantidadeFiltrados(PedidoFilter filtro) {
		
		Criteria criteria = criarCriteriaParaFiltro(filtro);
		
		criteria.setProjection(Projections.rowCount());
		
		return ((Number) criteria.uniqueResult()).intValue();
	}
	
	@SuppressWarnings("unused")
	private Criteria criarCriteriaParaFiltro(PedidoFilter filtro) {
		
		Session session = this.manager.unwrap(Session.class);
		
		Criteria criteria = session.createCriteria(Pedido.class)
				.createAlias("cliente", "cliente")
				.createAlias("vendedor", "v");
		
		if (filtro.getNumeroDe() != null) {
			// id deve ser maior ou igual (ge = greater or equals) a filtro.numeroDe
			criteria.add(Restrictions.ge("id", filtro.getNumeroDe()));
		}

		if (filtro.getNumeroAte() != null) {
			// id deve ser menor ou igual (le = lower or equal) a filtro.numeroDe
			criteria.add(Restrictions.le("id", filtro.getNumeroAte()));
		}

		if (filtro.getDataCriacaoDe() != null) {
			criteria.add(Restrictions.ge("dataCriacao", filtro.getDataCriacaoDe()));
		}
		
		if (filtro.getDataCriacaoAte() != null) {
			criteria.add(Restrictions.le("dataCriacao", filtro.getDataCriacaoAte()));
		}
		
		if (StringUtils.isNotBlank(filtro.getNomeCliente())) {
			// acessamos o nome do cliente associado ao pedido pelo alias "c", criado anteriormente
			criteria.add(Restrictions.ilike("c.nome", filtro.getNomeCliente(), MatchMode.ANYWHERE));
		}
		
		if (StringUtils.isNotBlank(filtro.getNomeVendedor())) {
			// acessamos o nome do vendedor associado ao pedido pelo alias "v", criado anteriormente
			criteria.add(Restrictions.ilike("v.nome", filtro.getNomeVendedor(), MatchMode.ANYWHERE));
		}
		
		if (filtro.getStatuses() != null && filtro.getStatuses().length > 0) {
			// adicionamos uma restriÃ§Ã£o "in", passando um array de constantes da enum StatusPedido
			criteria.add(Restrictions.in("status", filtro.getStatuses()));
		}
		
		return criteria;
	}
	
	public Pedido guardar(Pedido pedido) {
		return this.manager.merge(pedido);
	}

	public Object porId(Long id) {

		return manager.find(Pedido.class, id);
		
	}
	
}