package br.com.sanara.loja.modelo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "categorias_chave_composta")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaChaveComposta {

    @EmbeddedId //indica ao jpa que o id é nessa classe categoriaId
    private CategoriaId id;

}
