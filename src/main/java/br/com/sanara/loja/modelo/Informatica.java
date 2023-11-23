package br.com.sanara.loja.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "informatica")
public class Informatica extends Produto{

    private String marca;

    private Integer modelo;

}
