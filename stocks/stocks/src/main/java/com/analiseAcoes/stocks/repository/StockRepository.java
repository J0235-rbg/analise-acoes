package com.analiseAcoes.stocks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.analiseAcoes.stocks.model.Stock;
import java.util.Optional;

@Repository 
public interface StockRepository extends JpaRepository<Stock, String>{
    Optional<Stock> findByTickerIgnoreCase(String ticker);
}
