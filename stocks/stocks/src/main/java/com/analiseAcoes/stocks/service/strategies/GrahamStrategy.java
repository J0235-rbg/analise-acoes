package com.analiseAcoes.stocks.service.strategies;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;

import com.analiseAcoes.stocks.model.AnaliseInvestidores;
import com.analiseAcoes.stocks.model.Stock;

@Component 
public class GrahamStrategy implements InvestidorStrategy{

    @Override
    public void analisar(Stock stock, AnaliseInvestidores analise){

        BigDecimal lpa = stock.getLpa();
        BigDecimal vpa = stock.getVpa();
        BigDecimal cotacao = stock.getCotacao();

        // Graham exige LPA e VPA estritamente positivos
        if(lpa != null && vpa != null && cotacao != null
            && lpa.compareTo(BigDecimal.ZERO) > 0
            && vpa.compareTo(BigDecimal.ZERO) > 0){

                // 22.5 * LPA * VPA
                BigDecimal base = new BigDecimal("22.5").multiply(lpa).multiply(vpa);

                // Raiz quadrade do produto
                double precoJustoDouble = Math.sqrt(base.doubleValue());
                BigDecimal precoJusto = BigDecimal.valueOf(precoJustoDouble).setScale(2, RoundingMode.HALF_UP);
                analise.setPrecoJustoGraham(precoJusto);

                if(precoJusto.compareTo(BigDecimal.ZERO) > 0){
                    BigDecimal margem = precoJusto.subtract(cotacao)
                    .divide(precoJusto, 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"))
                    .setScale(2, RoundingMode.HALF_UP);

                    analise.setMargemSegurancaGraham(margem);
                    analise.setAprovadoGraham(cotacao.compareTo(precoJusto) <= 0);
                }
            } else {
                analise.setAprovadoGraham(false);
            }
    }
}
