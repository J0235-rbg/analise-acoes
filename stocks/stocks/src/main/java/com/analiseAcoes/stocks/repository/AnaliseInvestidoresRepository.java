package com.analiseAcoes.stocks.repository;

import java.util.List;

import org.springframework.boot.data.autoconfigure.web.DataWebProperties.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.analiseAcoes.stocks.model.AnaliseInvestidores;

@Repository 
public interface AnaliseInvestidoresRepository extends JpaRepository<AnaliseInvestidores, Long> {

    @Query("SELECT a FROM AnaliseInvestidores a ORDER BY a.scoreBuffett DESC")
    List<AnaliseInvestidores> findTopBuffett(Pageable pageable);

    List<AnaliseInvestidores> findByAprovadoBazinTrue(); 

    @Query("SELECT a FROM AnaliseInvestidores a WHERE a.rankingGreenblatt IS NOT NULL ORDER BY a.rankingGreenblatt ASC")
    List<AnaliseInvestidores> findTopGreenblatt(Pageable pageable);
}
