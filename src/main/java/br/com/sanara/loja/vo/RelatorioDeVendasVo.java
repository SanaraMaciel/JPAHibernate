package br.com.sanara.loja.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioDeVendasVo {

    private String nomeProduto;

    private Long qtdVendida;

    private LocalDate dataUltimaVenda;

}
