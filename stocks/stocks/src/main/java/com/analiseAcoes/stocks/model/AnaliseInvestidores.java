package com.analiseAcoes.stocks.model;

import lombok.*;
import java.math.BigDecimal;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "analise_investidores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder 
public class AnaliseInvestidores {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "acao_ticker", nullable = false, unique = true)
    private Stock acao;

    //metodologia 1: Warren Buffet (nota de 0 a 100 baseado em Moat, ROIC aleto e divida baixa)
    private Integer scoreBuffett;
    private Boolean aprovadoBuffett;

    // Metodologia 2: Decio Bazin (preço teto e DY >= 6%)
    @Column(precision = 10, scale = 20)
    private BigDecimal precoTetoBazin;
    private Boolean aprovadoBazin;

    // Metodologia 3: Benjamin Graham ( Preço justo calculado pela fórmula da raiz) 
    @Column(precision = 10, scale = 2)
    private BigDecimal precoJustoGraham;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal margemSegurancaGraham; // margem de segurança = (preço justo - preço atual) / preço justo
    private Boolean aprovadoGraham;

    // Metodologia 4: Joel Greanblatt (Ranking da Formula magic)
    private Integer rankingGreenblatt;

    private LocalDateTime dataCalculo = LocalDateTime.now();

}
