package br.com.sanara.loja.modelo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Table(name = "cliente")
@Data
@NoArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded //indica que a classe é embutível dentro de uma entidade
    //é bom para "quebrar" os atributos de forma que diminua os atributos da classe
    private DadosPessoais dadosPessoais;

    public Cliente(String nome, String cpf) {
        this.dadosPessoais = new DadosPessoais(nome, cpf);
    }

    //método delegate utilizado para evitar erros ao criar classes embedded
    //assim não é necessário mudar em muitas partes do código
    public String getNome() {
        return this.dadosPessoais.getNome();
    }


}
