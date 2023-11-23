package br.com.sanara.loja.testes;

import br.com.sanara.loja.dao.CategoriaDAO;
import br.com.sanara.loja.dao.ClienteDAO;
import br.com.sanara.loja.dao.PedidoDAO;
import br.com.sanara.loja.dao.ProdutoDAO;
import br.com.sanara.loja.modelo.*;
import br.com.sanara.loja.vo.RelatorioDeVendasVo;
import br.com.sanara.loja.vo.RelatorioDeVendasPorDiaVo;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

public class CadastroDeProduto {

    public static void main(String[] args) {
        PopulaBanco();
        fazerPedido();
        testaMetodos();
    }

    private static void testaMetodos() {

        EntityManager em = br.com.sanara.loja.util.JPAUtil.getEntityManager();
        ProdutoDAO produtoDao = new ProdutoDAO(em);

        Produto p = produtoDao.buscarPorId(1l);
        System.out.println(p.getPreco());

        List<Produto> todos = produtoDao.buscarPorNomeDaCategoria("CELULARES");
        todos.forEach(p2 -> System.out.println(p2.getNome()));

        //namedQuery
        List<Produto> todosProd = produtoDao.buscarPorNomeDaCategoriaNamedQuery("CELULARES");
        todosProd.forEach(prods -> System.out.println("NamedQuery: " + prods.getNome()));

        BigDecimal precoDoProduto = produtoDao.buscarPrecoDoProdutoComNome("Xiaomi Redmi");
        System.out.println("Preco do Produto: " + precoDoProduto);
    }

    private static void fazerPedido() {
        EntityManager em = br.com.sanara.loja.util.JPAUtil.getEntityManager();
        em.getTransaction().begin();

        Cliente cliente = buscaClientePorId();
        Pedido pedido = new Pedido(cliente);
        pedido.adicionarItem(new ItemPedido(10, pedido, buscaProdutoPorId(1l)));
        pedido.adicionarItem(new ItemPedido(40, pedido, buscaProdutoPorId(2l)));

        Pedido pedido2 = new Pedido(cliente);
        pedido2.adicionarItem(new ItemPedido(2, pedido2, buscaProdutoPorId(3l)));

        PedidoDAO pedidoDAO = new PedidoDAO(em);
        pedidoDAO.cadastrar(pedido);
        pedidoDAO.cadastrar(pedido2);

        em.getTransaction().commit();

        BigDecimal valorTotalVendido = pedidoDAO.valorTotalVendido();
        System.out.println("VALOR TOTAL VENDIDO: " + valorTotalVendido);

        /*List<Object[]> relatorio = pedidoDAO.relatorioDeVendas();

        for (Object[] obj : relatorio) {
            System.out.println("Relatorio 1: " + obj[0]);
            System.out.println("Relatorio 2: " + obj[1]);
            System.out.println("Relatorio 3:" + obj[2]);
        }*/

        List<RelatorioDeVendasVo> relatorio = pedidoDAO.relatorioDeVendas();
        List<RelatorioDeVendasPorDiaVo> relatorio2 = pedidoDAO.relatoriosDeVendasPorDia();

        for (RelatorioDeVendasVo obj : relatorio) {
            relatorio.forEach(System.out::println);
        }

        for (RelatorioDeVendasPorDiaVo obj : relatorio2) {
            relatorio2.forEach(System.out::println);
        }

        em.close();

    }

    private static Produto buscaProdutoPorId(Long id) {
        EntityManager em = br.com.sanara.loja.util.JPAUtil.getEntityManager();
        ProdutoDAO produtoDao = new ProdutoDAO(em);
        return produtoDao.buscarPorId(id);
    }

    private static Cliente buscaClientePorId() {
        EntityManager em = br.com.sanara.loja.util.JPAUtil.getEntityManager();
        ClienteDAO clienteDao = new ClienteDAO(em);
        return clienteDao.buscarPorId(1l);

    }

    private static void PopulaBanco() {
        Categoria celulares = new Categoria("CELULARES");
        Categoria videogames = new Categoria("VIDEOGAMES");
        Categoria informatica = new Categoria("INFORMATICA");

        Produto celular = new Produto("Xiaomi Redmi", "Celular Xiomi", new BigDecimal("800"), celulares);
        Produto videogame = new Produto("PS5", "Playstation 5", new BigDecimal("5000"), videogames);
        Produto notebook = new Produto("Dell ", "Dell inspirion", new BigDecimal("8000"), informatica);

        EntityManager em = br.com.sanara.loja.util.JPAUtil.getEntityManager();
        ProdutoDAO produtoDao = new ProdutoDAO(em);
        CategoriaDAO categoriaDao = new CategoriaDAO(em);
        ClienteDAO clienteDao = new ClienteDAO(em);

        em.getTransaction().begin();

        categoriaDao.cadastrar(celulares);
        categoriaDao.cadastrar(videogames);
        categoriaDao.cadastrar(informatica);
        produtoDao.cadastrar(celular);
        produtoDao.cadastrar(videogame);
        produtoDao.cadastrar(notebook);


        Cliente cliente = new Cliente("Sanara", "156123456");
        clienteDao.cadastrar(cliente);

        em.getTransaction().commit();
        em.close();
    }

}
