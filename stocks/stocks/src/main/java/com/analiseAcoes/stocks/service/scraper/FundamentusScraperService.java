package com.analiseAcoes.stocks.service.scraper;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.analiseAcoes.stocks.model.Stock;


@Service 
public class FundamentusScraperService {

    private static final String URL = "https://www.fundamentus.com.br/resultado.php";

    public List<Stock> extrairAcoes() throws IOException{
        List<Stock> acoes = new ArrayList<>();

        Document doc = Jsoup.connect(URL)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .timeout(15000)
                .get();
        
        Elements linhas = doc.select("#resultado tbody tr");
        for (Element linha : linhas){
            Elements colunas = linha.select("td");
            if(colunas.size() > 18) continue;

            String ticker = colunas.get(0).text().trim();
            if(ticker.isEmpty()) continue;

            Stock acao = Stock.builder()
                    .ticker(ticker)
                    .cotacao(parseBigDecimal(colunas.get(1).text()))
                    .pl(parseBigDecimal(colunas.get(2).text()))
                    .pvp(parseBigDecimal(colunas.get(3).text()))
                    .psr(parseBigDecimal(colunas.get(4).text()))
                    .dividendYield(parseBigDecimal(colunas.get(5).text()))
                    .margemLiquida(parseBigDecimal(colunas.get(8).text()))
                    .liquidezCorrente(parseBigDecimal(colunas.get(9).text()))
                    .roic(parseBigDecimal(colunas.get(15).text()))
                    .roe(parseBigDecimal(colunas.get(16).text()))
                    .dividaLiquidaEbtida(parseBigDecimal(colunas.get(19).text()))
                    .dataAtualizacao(LocalDateTime.now())
                    .build();
            acoes.add(acao);

        }
        return acoes;
    }

    private BigDecimal parseBigDecimal(String valorTexto){
        if(valorTexto == null || valorTexto.isEmpty() || valorTexto.equals("-")){
            return BigDecimal.ZERO;
        }
        try{
            String limpo = valorTexto.replace(".", "")
                    .replace(",", ".")
                    .replace("%", "")
                    .trim();
            return new BigDecimal(limpo);
        } catch (Exception e){
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }
}
