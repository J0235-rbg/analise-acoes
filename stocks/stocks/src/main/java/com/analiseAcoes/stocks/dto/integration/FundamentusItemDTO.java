package com.analiseAcoes.stocks.dto.integration;

import java.math.BigDecimal;

import lombok.Data;

@Data 
public class FundamentusItemDTO {
    private String ticker;
    private BigDecimal cotacao;
    private BigDecimal pl;
    private BigDecimal pvp;
    private BigDecimal psr;
    private BigDecimal dividendYield;
    private BigDecimal roic;
    private BigDecimal roe;
    private BigDecimal margemLiquida;
    private BigDecimal liquidezCorrente;
    private BigDecimal dividaLiquidaEbitda;
}
