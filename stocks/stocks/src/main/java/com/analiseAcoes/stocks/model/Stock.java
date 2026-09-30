package com.analiseAcoes.stocks.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "stocks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock {

    @Id
    @Column(length = 10, nullable = false)
    private String ticker;

    private String nomeEmpresa;

    @Column(precision = 10, scale = 2)
    private BigDecimal cotacao;
    
    // indicadores de valuation
    @Column(precision = 10, scale = 2)
    private BigDecimal pl; // preço ou lucro

    @Column(precision = 10, scale = 2)
    private BigDecimal pvp; // Preço / Valor Patrimonial

    @Column(precision = 10, scale = 2)
    private BigDecimal psr; 

    @Column(precision = 10, scale = 2)
    private BigDecimal dividendYield; // Percentual nos últimos 12 meses

    // indicadores de rentabilidade e Eficiencia
    @Column(precision = 10, scale = 2)
    private BigDecimal roic; // Retorno sobre o Capital Investido

    @Column(precision = 10, scale = 2)
    private BigDecimal roe; // Retorno sobre o Patrimônio Líquido

    @Column(precision = 10, scale = 2)
    private BigDecimal margemLiquida; // Margem Líquida

    //Indicadores de endividamento e Liquidez
    @Column(precision = 10, scale = 2)
    private BigDecimal dividaLiquidaEbtida;

    @Column(precision = 10, scale = 2)
    private BigDecimal liquidezCorrente;

    // Dados por ação para cálculos de Graham/ Bazin
    @Column(precision = 10, scale = 2)
    private BigDecimal lpa; // Lucro por Ação

    @Column(precision = 10, scale = 2)
    private BigDecimal vpa; // Valor Patrimonial por Ação

    private LocalDateTime dataAtualizacao = LocalDateTime.now();

    // Relacionamento 1:1 com os scores calculados
    @OneToOne(mappedBy = "stocks", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private AnaliseInvestidores analise;



}
