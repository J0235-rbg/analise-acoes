package com.analiseAcoes.stocks.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.analiseAcoes.stocks.model.AnaliseInvestidores;
import com.analiseAcoes.stocks.model.Usuario;
import com.analiseAcoes.stocks.repository.AnaliseInvestidoresRepository;

import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

import com.analiseAcoes.stocks.dto.response.StockResponseDTO;

@Service 
@RequiredArgsConstructor 
public class ConsultaAcaoService {

    private final AnaliseInvestidoresRepository analiseRepository;

    public List<StockResponseDTO> listarRankingBuffett(Usuario usuario){
        boolean isPro = usuario != null && usuario.isProAtivo();

        // Free: limitado ao top 5; Pro: até 50 ações
        int limite = isPro ? 50 : 5;

        List<AnaliseInvestidores> lista = analiseRepository.findTopBuffett(PageRequest.of(0, limite));

        return lista.stream()
            .map(item -> StockResponseDTO.builder()
                .ticker(item.getAcao().getTicker())
                .cotacao(item.getAcao().getCotacao())
                .scoreBuffett(item.getScoreBuffett())
                // Dados exclusivos liberados apenas para usuários Pro
                .precoJustoGraham(isPro ? item.getPrecoJustoGraham() : null)
                .precoTetoBazin(isPro ? item.getPrecoTetoBazin() : null)
                .proOnly(!isPro)
                .build())
            .collect(Collectors.toList());
    }

}
