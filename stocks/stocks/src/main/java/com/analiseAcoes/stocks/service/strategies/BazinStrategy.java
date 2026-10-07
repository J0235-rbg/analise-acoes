package com.analiseAcoes.stocks.service.strategies;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;

import com.analiseAcoes.stocks.model.AnaliseInvestidores;
import com.analiseAcoes.stocks.model.Stock;

@Component 
public class BazinStrategy implements InvestidorStrategy {

    @Override
    public void analisar(Stock stock, AnaliseInvestidores analise){
        BigDecimal dy = stock.getDividendYield();
        BigDecimal cotacao = stock.getCotacao();

        if(dy != null && cotacao != null && dy.compareTo(BigDecimal.ZERO) > 0){
            // Dividendo em valor monetario estimado: Cotação * (DY/100)
            BigDecimal dividendoPago = cotacao.multiply(dy.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP));

            // Preço teto Bazin = Dividendo Pago / 0.06
            BigDecimal precoTeto = dividendoPago.divide(new BigDecimal("0.06"), 2, RoundingMode.HALF_UP);

            // Aprovado se DY >= 6% e cotação <= preço teto
            boolean aprovado = dy.compareTo(new BigDecimal("6.0")) >= 0 && cotacao.compareTo(precoTeto) <= 0;
            analise.setAprovadoBazin(aprovado);   
        } else {
            analise.setAprovadoBazin(false);
        }
    }
}
