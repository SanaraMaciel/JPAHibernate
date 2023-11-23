package br.com.sanara.loja.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioDeVendasPorDiaVo {

    private String nomeDoProduto;

    private BigDecimal valorVendido;

    private LocalDate dataDasVendas;


}
