package com.analiseAcoes.stocks.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.analiseAcoes.stocks.dto.response.StockResponseDTO;
import com.analiseAcoes.stocks.model.AnaliseInvestidores;
import com.analiseAcoes.stocks.model.Stock;
import com.analiseAcoes.stocks.repository.AnaliseInvestidoresRepository;
import com.analiseAcoes.stocks.repository.StockRepository;
import com.analiseAcoes.stocks.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/acoes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StockController {

    private final AnaliseInvestidoresRepository analiseRepository;
    private final StockRepository stockRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Endpoint para o Screnner / Ranking Buffett 
     * Exemplo: GET /api/acoes/buffett?usuarioId=1
    */
   @GetMapping("/buffett")
   public ResponseEntity<List<StockResponseDTO>> getRankingBuffett(@RequestParam(required = false) Long usuarioId) {
       boolean isPro = usuarioRepository.verificarSeEPro(usuarioId);
       int limite = isPro ? 50 : 5;  // Free: limitado ao top 5; Pro: até 50 ações

       List<AnaliseInvestidores> ranking = analiseRepository.findTopBuffett(PageRequest.of(0, limite));
       List<StockResponseDTO> response = ranking.stream()
                .map(item -> converteParaDTO(item, isPro))
                .collect(Collectors.toList());

       return ResponseEntity.ok(response);
   }
   
   /**
    * Endpoint para buscar o Raio-X de uma ação específica
    * Exemplo: GET /api/acoes/WEGE3?usuarioId=1
    */
   @GetMapping("/{ticker}")
   public ResponseEntity<StockResponseDTO> getAcaoPorTicket(
              @PathVariable String ticker,
              @RequestParam (required = false) Long usuarioId) {
                Optional<Stock> acaoOpt = stockRepository.findByTickerIgnoreCase(ticker);

                if(acaoOpt.isEmpty()){
                    return ResponseEntity.notFound().build();
                }

                Stock acao = acaoOpt.get();
                boolean isPro = usuarioRepository.verificarSeEPro(usuarioId);
                AnaliseInvestidores analise = acao.getAnalise();

                if(analise == null){
                    return ResponseEntity.notFound().build();
                }

                return ResponseEntity.ok(converteParaDTO(analise, isPro));
              }

    private StockResponseDTO converteParaDTO(AnaliseInvestidores analise, boolean isPro) {
        Stock acao = analise.getAcao();

        return StockResponseDTO.builder()
                .ticker(acao.getTicker())
                .nomeEmpresa(acao.getNomeEmpresa())
                .cotacao(acao.getCotacao())
                .scoreBuffett(analise.getScoreBuffett())
                .aprovadoBuffett(analise.getAprovadoBuffett())
                .roic(acao.getRoic())
                .roe(acao.getRoe())
                .dividaLiquidaEbitda(acao.getDividaLiquidaEbtida())
                // Campos PRO (mascarados como null para planos Free)
                .precoJustoGraham(isPro ? analise.getPrecoJustoGraham() : null)
                .margemSegurancaGraham(isPro ? analise.getMargemSegurancaGraham() : null)
                .aprovadoGraham(isPro ? analise.getAprovadoGraham() : null)
                .precoTetoBazin(isPro ? analise.getPrecoTetoBazin() : null)
                .aprovadoBazin(isPro ? analise.getAprovadoBazin() : null)
                .rankingGreenblatt(isPro ? analise.getRankingGreenblatt() : null)
                .proOnly(!isPro)
                .build();
    }
}
