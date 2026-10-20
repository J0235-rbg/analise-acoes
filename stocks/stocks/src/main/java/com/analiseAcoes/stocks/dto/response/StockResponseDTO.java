package com.analiseAcoes.stocks.dto.response;

import java.math.BigDecimal;

import lombok.*;

@Data 
@Builder 
@NoArgsConstructor
@AllArgsConstructor 
public class StockResponseDTO {

    private String ticker;
    private String nomeEmpresa;
    private BigDecimal cotacao;

    // Métricas liberadas no Plano Free
    private Integer scoreBuffett;
    private Boolean aprovadoBuffett;
    private BigDecimal roic;
    private BigDecimal roe;
    private BigDecimal dividaLiquidaEbitda;

    // Métricas exclusivas do Plano PRO (retornam null se for Free)
    private BigDecimal precoJustoGraham;
    private BigDecimal margemSegurancaGraham;
    private Boolean aprovadoGraham;
    private BigDecimal precoTetoBazin;
    private Boolean aprovadoBazin;
    private Integer rankingGreenblatt;

    // Flag para a interface React saber se exibe o blur / cadeado
    private boolean proOnly;
}
