package br.com.sanara.loja.modelo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "produtos")
@Data
@NamedQuery(name = "Produto.produtosPorCategoria", query = "SELECT p FROM Produto p WHERE p.categoria.nome = :nome")
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // indica ao jpa que esta é uma classe mãe p/ ser utilizada na herança com a estratégia de uma só tabela
@Inheritance(strategy = InheritanceType.JOINED) // indica ao jpa que esta é uma classe mãe strategy = InheritanceType.SINGLE_TABLE com a estratégia de cada filha ser uma tabela
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String descricao;

    private BigDecimal preco;

    @Column(name = "data_cadastro")
    private LocalDate dataCadastro = LocalDate.now();

    @ManyToOne(fetch = FetchType.LAZY)
    private Categoria categoria;

    public Produto() {
    }

    public Produto(String nome, String descricao, BigDecimal preco, Categoria categoria) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.categoria = categoria;
    }

}
