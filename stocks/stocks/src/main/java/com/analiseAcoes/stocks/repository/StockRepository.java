package com.analiseAcoes.stocks.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.analiseAcoes.stocks.model.Stock;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, String>{
    Optional<Stock> findByTickerIgnoreCase(String ticker);
}
