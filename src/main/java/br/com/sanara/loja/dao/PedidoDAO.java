package br.com.sanara.loja.dao;

import br.com.sanara.loja.modelo.Pedido;
import br.com.sanara.loja.vo.RelatorioDeVendasVo;
import br.com.sanara.loja.vo.RelatorioDeVendasPorDiaVo;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

public class PedidoDAO {

    private EntityManager em;

    public PedidoDAO(EntityManager em) {
        this.em = em;
    }

    public void cadastrar(Pedido pedido) {
        this.em.persist(pedido);
    }

    public void atualizar(Pedido pedido) {
        this.em.merge(pedido);
    }

    public void remover(Pedido pedido) {
        pedido = em.merge(pedido);
        this.em.remove(pedido);
    }

    public Pedido buscarPorId(Long id) {
        return em.find(Pedido.class, id);
    }

    public List<Pedido> buscarTodos() {
        String jpql = "SELECT p FROM pedido p";
        return em.createQuery(jpql, Pedido.class).getResultList();
    }

    public List<Pedido> buscarPorNome(String nome) {
        String jpql = "SELECT p FROM pedido p WHERE p.nome = :nome";
        return em.createQuery(jpql, Pedido.class)
                .setParameter("nome", nome)
                .getResultList();
    }

    public List<Pedido> buscarPorNomeDaCategoria(String nome) {
        String jpql = "SELECT p FROM pedido p WHERE p.categoria.nome = :nome";
        return em.createQuery(jpql, Pedido.class)
                .setParameter("nome", nome)
                .getResultList();
    }

    public BigDecimal buscarPrecoDopedidoComNome(String nome) {
        String jpql = "SELECT p.preco FROM pedido p WHERE p.nome = :nome";
        return em.createQuery(jpql, BigDecimal.class)
                .setParameter("nome", nome)
                .getSingleResult();
    }

    public BigDecimal valorTotalVendido() {
        String jpql = "SELECT SUM(p.valorTotal) FROM Pedido p";
        return em.createQuery(jpql, BigDecimal.class).getSingleResult();
    }

    //consulta retornando com uma lista de array de object
    public List<Object[]> relatorioDeVendasObject() {

        String jpql = "SELECT prod.nome, SUM(item.quantidade), MAX(p.data) "
                + "FROM Pedido p "
                + "JOIN p.itens item "
                + "JOIN item.produto prod "
                + "GROUP BY prod.nome "
                + "ORDER BY item.quantidade DESC";

        return em.createQuery(jpql, Object[].class).getResultList();
    }

    //removendo o object genérico array object transformando ele em uma classe
    //Vo é o padrão que é utilizado para demonstrar que essa classe não possui entidade no banco
    // que é apenas uma representação - Value Object (objeto de valor)
    public List<RelatorioDeVendasVo> relatorioDeVendas() {

        String jpql = "SELECT new br.com.sanara.loja.vo.RelatorioDeVendasVo("
                + "prod.nome, SUM(item.quantidade), MAX(p.data)) "
                + "FROM Pedido p "
                + "JOIN p.itens item "
                + "JOIN item.produto prod "
                + "GROUP BY prod.nome "
                + "ORDER BY item.quantidade DESC ";

        return em.createQuery(jpql, RelatorioDeVendasVo.class).getResultList();
    }

    public List<RelatorioDeVendasPorDiaVo> relatoriosDeVendasPorDia() {
        String jpql = "SELECT new br.com.sanara.loja.vo.RelatorioDeVendasPorDiaVo(" +
                " produto.nome, SUM(pedido.valorTotal), pedido.data) " +
                " FROM Pedido pedido" +
                " JOIN pedido.itens itens " +
                " JOIN itens.produto produto " +
                " GROUP BY produto.nome, pedido.data ";

        return em.createQuery(jpql, RelatorioDeVendasPorDiaVo.class).getResultList();
    }

    //busca pedido e traz junto tbm dados do cliente join fetch
    public Pedido buscarPedidoComCliente(Long id) {
        return em.createQuery("SELECT p FROM Pedido p JOIN FETCH p.cliente WHERE p.id = :id", Pedido.class)
                .setParameter("id", id)
                .getSingleResult();
    }


}
