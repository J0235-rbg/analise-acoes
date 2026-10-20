package com.analiseAcoes.stocks.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.analiseAcoes.stocks.model.AnaliseInvestidores;
import com.analiseAcoes.stocks.model.Stock;
import com.analiseAcoes.stocks.repository.AnaliseInvestidoresRepository;
import com.analiseAcoes.stocks.repository.StockRepository;
import com.analiseAcoes.stocks.service.scraper.FundamentusScraperService;
import com.analiseAcoes.stocks.service.strategies.InvestidorStrategy;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service 
@RequiredArgsConstructor 
@Slf4j 
public class AnaliseOrquestradorService {

    private final FundamentusScraperService scraperService;
    private final StockRepository stockRepository;
    private final AnaliseInvestidoresRepository analiseRepository;
    private final List<InvestidorStrategy> strategies;

    /**
     * Método agendado para sincronizar e processar as ações.
     */
    @Scheduled
    @Transactional
    public void sincronizarEProcessar(){
        log.info("Iniciando sincronização diaria de ações...");

        try{
            List<Stock> acoes = scraperService.extrairAcoes();

            for(Stock acao : acoes){
                // Salva ou atualiza a cotação/dados da ação
                Stock acaoSalva = stockRepository.save(acao);

                // Recupera analise existente ou cria uma nova
                AnaliseInvestidores analise = acaoSalva.getAnalise();
                if(analise == null){
                    analise = AnaliseInvestidores.builder()
                        .acao(acaoSalva)
                        .build();
                }

                // aplica cada estrategia 
                for(InvestidorStrategy strategy : strategies){
                    strategy.analisar(acaoSalva, analise);
                }

                analise.setDataCalculo(LocalDateTime.now());
                analiseRepository.save(analise);
            }

            log.info("Processamento concluído com sucesso para {} ações.", acoes.size());
        } catch (Exception e){
            log.error("Falha ao sincronizar e processar ações: {}", e);
        }
    }
}
