package com.analiseAcoes.stocks.dto.response;

import java.math.BigDecimal;

import com.analiseAcoes.stocks.model.AnaliseInvestidores;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@Builder 
@NoArgsConstructor 
@AllArgsConstructor 
public class StockRankingDTO {

    private String ticker;
    private String nomeEmpresa;
    private BigDecimal cotacao;
    private BigDecimal pl;
    private BigDecimal pvp;
    private BigDecimal roic;
    private BigDecimal roe;
    private BigDecimal dividendYield;

    // Métricas específicas das estratégias
    private Integer scoreBuffett;
    private Boolean aprovadoBuffett;

    private BigDecimal precoJustoGraham;
    private BigDecimal margemSegurancaGraham;
    private Boolean aprovadoGraham;

    private BigDecimal precoTetoBazin;
    private Boolean aprovadoBazin;

    private Integer rankingGreenblatt;

    public static StockRankingDTO fromEntity(AnaliseInvestidores entity){
        var acao = entity.getAcao();
        return StockRankingDTO.builder()
                .ticker(acao.getTicker())
                .nomeEmpresa(acao.getNomeEmpresa())
                .cotacao(acao.getCotacao())
                .pl(acao.getPl())
                .pvp(acao.getPvp())
                .roic(acao.getRoic())
                .roe(acao.getRoe())
                .dividendYield(acao.getDividendYield())
                .scoreBuffett(entity.getScoreBuffett())
                .aprovadoBuffett(entity.getAprovadoBuffett())
                .precoJustoGraham(entity.getPrecoJustoGraham())
                .margemSegurancaGraham(entity.getMargemSegurancaGraham())
                .aprovadoGraham(entity.getAprovadoGraham())
                .precoTetoBazin(entity.getPrecoTetoBazin())
                .aprovadoBazin(entity.getAprovadoBazin())
                .rankingGreenblatt(entity.getRankingGreenblatt())
                .build();
    }

}
