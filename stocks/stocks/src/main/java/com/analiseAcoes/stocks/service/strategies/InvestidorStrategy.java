package com.analiseAcoes.stocks.service.strategies;

import com.analiseAcoes.stocks.model.AnaliseInvestidores;
import com.analiseAcoes.stocks.model.Stock;

public interface InvestidorStrategy {

    void analisar(Stock stock, AnaliseInvestidores analise);  

}
