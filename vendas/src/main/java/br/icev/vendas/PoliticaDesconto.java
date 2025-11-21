package br.icev.vendas;

import java.math.BigDecimal;

@FunctionalInterface
public interface PoliticaDesconto {
    BigDecimal aplicar(BigDecimal subtotal);
}
