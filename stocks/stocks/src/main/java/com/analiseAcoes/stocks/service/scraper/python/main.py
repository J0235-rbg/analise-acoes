import fundamentus
from fastapi import FastAPI, HTTPException
import numpy as np

app = FastAPI(
    title="Fundamentus Scraper API.",
    description="Microserviço de extração fundamentalista de açoões da B3 usando a biblioteca fundamentus.",
    version="1.0.0"
)

@app.get("/api/v1/dados-acoes")
def obter_dados_acoes():
    try:
        # puxa o DataFrame com os dados das ações negociadas na B3
        df = fundamentus.get_resultado()

        # Reseta o index para transformar a coluna do ticker('papel') em uma coluna normal
        df.reset_index()

        # Trata de valores nulos / infinitos para não quebrar a conversão para JSON 
        df.replace([np.inf, -np.inf], np.nan)
        df = df.fillna(0.0)

        # Mapeia as categorias para o padrão aceito pelo backend Spring Boot
        resultado = []
        for _, row in df.iterrows():
            item = {
                "ticker": str(row.get("papel", "")).strip(),
                "cotacao": float(row.get("cotacao", 0.0)),
                "pl": float(row.get("pl", 0.0)),
                "pvp": float(row.get("pvp", 0.0)),
                "psr": float(row.get("psr", 0.0)),
                "dividendYield": float(row.get("dy", 0.0)) * 100, # Converte para porcentagem
                "roic": float(row.get("roic", 0.0)) * 100, # Converte para porcentagem
                "roe": float(row.get("roe", 0.0)) * 100, #converte para porcentagem
                "margemLiquida": float(row.get("mrgLiq", 0.0)) * 100, # Converte para porcentagem
                "LiquidezCorrente": float(row.get("liq_corr", 0.0)),
                "dividaLiquidaEbitda": float(row.get("divb_patr", 0.0)) # ou alavancagem equivalente
            }
            resultado.append(item)

            return {
                "status": "sucesso",
                "total_acoes": len(resultado),
                "dados": resultado
            }
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Erro ao obter dados das ações: {str(e)}")

# Execução local: uvicorn main: app --reload --port 8080