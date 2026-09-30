package com.analiseAcoes.stocks.model;

import java.time.LocalDateTime;

import com.analiseAcoes.stocks.model.enums.Plano;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder     
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Plano plano = Plano.Free;

    private LocalDateTime dataExpiracaoPlano;
    
    private LocalDateTime dataCriacao = LocalDateTime.now();

    public boolean isProAtivo() {
        if(this.plano == Plano.Pro && this.dataExpiracaoPlano != null){
            return dataExpiracaoPlano == null || dataExpiracaoPlano.isAfter(LocalDateTime.now());
        }
        return false;
    }
}
