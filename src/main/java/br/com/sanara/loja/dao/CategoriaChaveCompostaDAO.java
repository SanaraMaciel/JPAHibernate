package br.com.sanara.loja.dao;

import br.com.sanara.loja.modelo.CategoriaChaveComposta;
import br.com.sanara.loja.modelo.CategoriaId;
import br.com.sanara.loja.modelo.Produto;

import javax.persistence.EntityManager;

public class CategoriaChaveCompostaDAO {

    private EntityManager em;

    public CategoriaChaveCompostaDAO(EntityManager em) {
        this.em = em;
    }

    public void cadastrar(CategoriaChaveComposta categoria) {
        this.em.persist(categoria);
    }

    public CategoriaChaveComposta buscarPorId(CategoriaId id) {
        return em.find(CategoriaChaveComposta.class, id);
    }

}
