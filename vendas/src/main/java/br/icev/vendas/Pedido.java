package br.icev.vendas;

import java.math.BigDecimal;
import java.util.Map;

public class Pedido {
    public enum Status { PAGO }

    public Pedido(Map<String, Integer> itensPorCodigo, BigDecimal totalPago,
                  String codigoAutorizacao, Status status) {
        throw new UnsupportedOperationException("TODO");
    }

    public BigDecimal getTotalPago() { throw new UnsupportedOperationException("TODO"); }
    public String getCodigoAutorizacao() { throw new UnsupportedOperationException("TODO"); }
    public Status getStatus() { throw new UnsupportedOperationException("TODO"); }
    public int getQuantidadeItem(String codigo) { throw new UnsupportedOperationException("TODO"); }
}
