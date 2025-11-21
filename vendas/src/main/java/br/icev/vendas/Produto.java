package br.icev.vendas;

import java.math.BigDecimal;

public class Produto {
    private final String codigo;
    private final String nome;
    private final BigDecimal precoUnitario;

    public Produto(String codigo, String nome, BigDecimal precoUnitario) {
        throw new UnsupportedOperationException("TODO implementar");
    }

    public String getCodigo() { throw new UnsupportedOperationException("TODO"); }
    public String getNome() { throw new UnsupportedOperationException("TODO"); }
    public BigDecimal getPrecoUnitario() { throw new UnsupportedOperationException("TODO"); }

    @Override
    public boolean equals(Object o) { throw new UnsupportedOperationException("TODO"); }
    @Override
    public int hashCode() { throw new UnsupportedOperationException("TODO"); }
}
