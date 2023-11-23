package br.com.sanara.loja.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;



@Entity
@Table(name = "pedido")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Pedido {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate data = LocalDate.now();

    @Column(name = "valor_total")
    private BigDecimal valorTotal = new BigDecimal(0.00);

    @ManyToOne(fetch = FetchType.LAZY)
    private Cliente cliente;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<ItemPedido> itens = new ArrayList<>();

    public void adicionarItem(ItemPedido item) {
        item.setPedido(this);
        this.itens.add(item);
        this.valorTotal = this.valorTotal.add(item.getValor());
    }

    public Pedido(Cliente cliente) {
        this.cliente = cliente;
    }


}
