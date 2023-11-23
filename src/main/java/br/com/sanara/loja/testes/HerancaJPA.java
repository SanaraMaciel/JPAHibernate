package br.com.sanara.loja.testes;

import br.com.sanara.loja.dao.*;
import br.com.sanara.loja.modelo.*;
import br.com.sanara.loja.util.JPAUtil;
import br.com.sanara.loja.vo.RelatorioDeVendasPorDiaVo;
import br.com.sanara.loja.vo.RelatorioDeVendasVo;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

public class HerancaJPA {

    public static void main(String[] args) {
        PopulaBanco();
        fazerPedido();
        //testaMetodos();

        EntityManager em = JPAUtil.getEntityManager();
        Pedido pedido = em.find(Pedido.class, 1l); // não funciona com lombok só getters e setters para trazer tod0 o obj
        System.out.println("Pedido: " + pedido.getData());
        System.out.println("Pedido qtd: " + pedido.getItens().size());
        System.out.println("Pedido cliente: " + pedido.getCliente().getDadosPessoais().getNome());

        //em.close();

        /*
            coluna criada qdo usa herança com o strategy = InheritanceType.SINGLE_TABLE - uma tabela só
            DTYPE varchar(31) not null
             Como é um tabelão, quando fizermos um select, hibernate, ele precisa saber:
             eu trouxe um registro dessa única tabela, esse registro,
             ele é uma instância de livro ou de informática? inserindo o tipo nessa coluna
        */


        //consulta com join fetch
        PedidoDAO pedidoDAO = new PedidoDAO(em);
        Pedido pedidoFetch = pedidoDAO.buscarPedidoComCliente(1L);
        System.out.println("Pedido cliente Fetch: " + pedidoFetch.getCliente().getDadosPessoais().getNome());

        //consulta dinâmicas método verboso
        ProdutoDAO produtoDAO = new ProdutoDAO(em);
        List<Produto> prods = produtoDAO.buscarPorParametros("PS5", null, null);
        prods.forEach(p -> System.out.println("Produtos: " + p));


        //consulta com criteria api
        List<Produto> produtosCriteria = produtoDAO.buscarPorParametrosComCriteria("Dell", null, null);
        produtosCriteria.forEach(p1 -> System.out.println("ProdutosCriteria: " + p1));


        //chave composta
        CategoriaId categoriaId = new CategoriaId("TESTE", "COMPOSTA");
        CategoriaChaveComposta cat = new CategoriaChaveComposta(categoriaId);
        CategoriaChaveCompostaDAO catDAO = new CategoriaChaveCompostaDAO(em);

        em.getTransaction().begin();
        catDAO.cadastrar(cat);
        em.getTransaction().commit();

        CategoriaChaveComposta categoriaComposta = catDAO.buscarPorId(categoriaId);
        System.out.println("Categoria chave: " + categoriaComposta);

    }


    private static void testaMetodos() {

        EntityManager em = JPAUtil.getEntityManager();
        ProdutoDAO produtoDao = new ProdutoDAO(em);
        PedidoDAO pedidoDAO = new PedidoDAO(em);

        Produto p = produtoDao.buscarPorId(1l);
        System.out.println(p.getPreco());

        List<Produto> todos = produtoDao.buscarPorNomeDaCategoria("CELULARES");
        todos.forEach(p2 -> System.out.println(p2.getNome()));

        //namedQuery
        List<Produto> todosProd = produtoDao.buscarPorNomeDaCategoriaNamedQuery("CELULARES");
        todosProd.forEach(prods -> System.out.println("NamedQuery: " + prods.getNome()));

        BigDecimal precoDoProduto = produtoDao.buscarPrecoDoProdutoComNome("Xiaomi Redmi");
        System.out.println("Preco do Produto: " + precoDoProduto);

        BigDecimal valorTotalVendido = pedidoDAO.valorTotalVendido();
        System.out.println("VALOR TOTAL VENDIDO: " + valorTotalVendido);

        List<RelatorioDeVendasVo> relatorio = pedidoDAO.relatorioDeVendas();
        List<RelatorioDeVendasPorDiaVo> relatorio2 = pedidoDAO.relatoriosDeVendasPorDia();

        for (RelatorioDeVendasVo obj : relatorio) {
            relatorio.forEach(System.out::println);
        }

        for (RelatorioDeVendasPorDiaVo obj : relatorio2) {
            relatorio2.forEach(System.out::println);
        }

    }

    private static void fazerPedido() {
        EntityManager em = JPAUtil.getEntityManager();
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

        em.close();

    }

    private static Produto buscaProdutoPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        ProdutoDAO produtoDao = new ProdutoDAO(em);
        return produtoDao.buscarPorId(id);
    }

    private static Cliente buscaClientePorId() {
        EntityManager em = JPAUtil.getEntityManager();
        ClienteDAO clienteDao = new ClienteDAO(em);
        return clienteDao.buscarPorId(1l);

    }

    private static void PopulaBanco() {
        EntityManager em = JPAUtil.getEntityManager();
        ProdutoDAO produtoDao = new ProdutoDAO(em);
        CategoriaDAO categoriaDao = new CategoriaDAO(em);
        ClienteDAO clienteDao = new ClienteDAO(em);

        Categoria celulares = new Categoria("CELULARES");
        Categoria videogames = new Categoria("VIDEOGAMES");
        Categoria informatica = new Categoria("INFORMATICA");

        Produto celular = new Produto("Xiaomi Redmi", "Celular Xiomi", new BigDecimal("800"), celulares);
        Produto videogame = new Produto("PS5", "Playstation 5", new BigDecimal("5000"), videogames);
        Produto notebook = new Produto("Dell ", "Dell inspirion", new BigDecimal("8000"), informatica);
        Produto pc = new Produto("Dell ", "Dell inspirion PC", new BigDecimal("18000"), informatica);

        em.getTransaction().begin();

        categoriaDao.cadastrar(celulares);
        categoriaDao.cadastrar(videogames);
        categoriaDao.cadastrar(informatica);
        produtoDao.cadastrar(celular);
        produtoDao.cadastrar(videogame);
        produtoDao.cadastrar(notebook);
        produtoDao.cadastrar(pc);

        Cliente cliente = new Cliente("Sanara", "156123456");
        clienteDao.cadastrar(cliente);

        em.getTransaction().commit();

        fazerPedido();

        em.close();
    }

}
