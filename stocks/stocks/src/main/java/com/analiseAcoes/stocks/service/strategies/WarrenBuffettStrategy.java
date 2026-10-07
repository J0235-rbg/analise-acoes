package com.analiseAcoes.stocks.service.strategies;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.analiseAcoes.stocks.model.AnaliseInvestidores;
import com.analiseAcoes.stocks.model.Stock;

@Component 
public class WarrenBuffettStrategy implements InvestidorStrategy{

    @Override 
    public void analisar(Stock stock, AnaliseInvestidores analise){
        int score = 0;

        // Análise do Moat
        // ROE > 15% (+25 pontos) - Vantagem competitiva
        if(stock.getRoe() != null && stock.getRoe().compareTo(new BigDecimal("15.0")) >= 0){
            score += 25;
        }

        // ROIC > 12% (+25 pontos) - Vantagem competitiva / Moat
        if(stock.getRoic() != null && stock.getRoic().compareTo(new BigDecimal("12.0")) >= 0){
            score += 25;
        }

        // Divida Liquida / EBITDA < 2.5 (+25 pontos) - Baixo endividamento
        if(stock.getDividaLiquidaEbtida() != null 
            && stock.getDividaLiquidaEbtida().compareTo(BigDecimal.ZERO) >= 0
            && stock.getDividaLiquidaEbtida().compareTo(new BigDecimal("2.5")) <= 0){
                score += 25;
        }

        // Margem Liquida > 10% (+25 pontos)
        if(stock.getMargemLiquida() != null && stock.getMargemLiquida().compareTo(new BigDecimal("10.0")) >= 0){
            score += 25;
        }

        analise.setScoreBuffett(score);
        analise.setAprovadoBuffett(score >= 75);
    }

}
