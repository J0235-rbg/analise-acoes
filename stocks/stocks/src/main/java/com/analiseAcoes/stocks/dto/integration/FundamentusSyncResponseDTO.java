package com.analiseAcoes.stocks.dto.integration;

import java.util.List;

import lombok.Data;

@Data 
public class FundamentusSyncResponseDTO {

    private String status;
    private Integer totalAcoes;
    private List<FundamentusItemDTO> dados;
}
