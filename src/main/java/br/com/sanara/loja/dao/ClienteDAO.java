package br.com.sanara.loja.dao;

import br.com.sanara.loja.modelo.Cliente;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

public class ClienteDAO {

    private EntityManager em;

    public ClienteDAO(EntityManager em) {
        this.em = em;
    }

    public void cadastrar(Cliente cliente) {
        this.em.persist(cliente);
    }

    public void atualizar(Cliente cliente) {
        this.em.merge(cliente);
    }

    public void remover(Cliente cliente) {
        cliente = em.merge(cliente);
        this.em.remove(cliente);
    }

    public Cliente buscarPorId(Long id) {
        return em.find(Cliente.class, id);
    }

    public List<Cliente> buscarTodos() {
        String jpql = "SELECT c FROM clientecp";
        return em.createQuery(jpql, Cliente.class).getResultList();
    }

    public List<Cliente> buscarPorNome(String nome) {
        String jpql = "SELECT c FROM cliente c WHERE c.nome = :nome";
        return em.createQuery(jpql, Cliente.class)
                .setParameter("nome", nome)
                .getResultList();
    }

    public List<Cliente> buscarClientes(String nome, LocalDate dataNascimento) {
        String jpql = "SELECT c FROM Cliente c WHERE 1 = 1";
        if (nome != null && !nome.trim().isEmpty()) {
            jpql += "AND c.nome = :nome ";
        }
        if (dataNascimento != null) {
            jpql += " AND c.dataNascimento = :dataNascimento ";
        }
        TypedQuery<Cliente> query = em.createQuery(jpql, Cliente.class);
        if (nome != null && !nome.trim().isEmpty()) {
            query.setParameter("nome", nome);
        }
        if (dataNascimento != null) {
            query.setParameter("dataNascimento", dataNascimento);
        }
        return query.getResultList();
    }


}
