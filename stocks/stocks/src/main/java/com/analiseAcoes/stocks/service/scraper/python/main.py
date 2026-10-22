import fundamentus
from fastapi import FastAPI, HTTPException
import numpy as np

app = FastAPI(
    title="Fundamentus Scraper API",
    description="Microsserviço de extração fundamentalista de ações da B3",
    version="1.0.0"
)

def limpar_float(valor) -> float:
    """Converte com segurança valores para float, tratando padrões numéricos brasileiros."""
    if valor is None or (isinstance(valor, float) and np.isnan(valor)):
        return 0.0
    try:
        val_str = str(valor).replace('.', '').replace(',', '.').replace('%', '').strip()
        val_float = float(val_str)
        return 0.0 if np.isnan(val_float) or np.isinf(val_float) else val_float
    except Exception:
        return 0.0

@app.get("/api/v1/dados-acoes")
def obter_dados_acoes():
    try:
        # Usa get_resultado_raw() para ignorar o renomeamento interno que causava o KeyError
        try:
            df = fundamentus.get_resultado_raw()
        except Exception:
            # Fallback direto caso o pacote tenha alguma instabilidade adicional
            import pandas as pd
            import requests
            url = "https://www.fundamentus.com.br/resultado.php"
            headers = {"User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36"}
            req = requests.get(url, headers=headers, timeout=15)
            dfs = pd.read_html(req.text, decimal=",", thousands=".")
            df = dfs[0]

        # Garante que o ticker seja uma coluna utilizável
        df = df.reset_index()

        resultado = []
        for _, row in df.iterrows():
            # Acessa os campos pela ordem fixa das colunas da tabela do Fundamentus
            # 0: Papel | 1: Cotação | 2: P/L | 3: P/VP | 4: PSR | 5: Div.Yield
            # 8: Mrg. Líq | 9: Liq. Corr | 15: ROIC | 16: ROE | 19: Dív.Brut/Patrim
            ticker = str(row.iloc[0]).strip()
            if not ticker or ticker.lower() == 'papel':
                continue

            cotacao = limpar_float(row.iloc[1])
            pl = limpar_float(row.iloc[2])
            pvp = limpar_float(row.iloc[3])
            psr = limpar_float(row.iloc[4])
            dy = limpar_float(row.iloc[5])
            mrg_liq = limpar_float(row.iloc[8])
            liq_corr = limpar_float(row.iloc[9])
            roic = limpar_float(row.iloc[15])
            roe = limpar_float(row.iloc[16])
            divida = limpar_float(row.iloc[19]) if len(row) > 19 else 0.0

            # Normaliza valores percentuais caso já venham como fração decimal
            def ajustar_pct(val):
                return val * 100 if (0.0 < abs(val) < 1.0) else val

            item = {
                "ticker": ticker,
                "cotacao": cotacao,
                "pl": pl,
                "pvp": pvp,
                "psr": psr,
                "dividendYield": ajustar_pct(dy),
                "roic": ajustar_pct(roic),
                "roe": ajustar_pct(roe),
                "margemLiquida": ajustar_pct(mrg_liq),
                "liquidezCorrente": liq_corr,
                "dividaLiquidaEbitda": divida
            }
            resultado.append(item)

        return {
            "status": "sucesso",
            "total_acoes": len(resultado),
            "dados": resultado
        }

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Erro ao obter dados das ações: {str(e)}")