package br.icev.vendas;

import br.icev.vendas.excecoes.QuantidadeInvalidaException;
import br.icev.vendas.excecoes.SemEstoqueException;

public class Estoque {
    public void adicionarEstoque(String codigo, int quantidade) throws QuantidadeInvalidaException {
        throw new UnsupportedOperationException("TODO");
    }

    public int getDisponivel(String codigo) {
        throw new UnsupportedOperationException("TODO");
    }

    public void reservar(String codigo, int quantidade)
            throws SemEstoqueException, QuantidadeInvalidaException {
        throw new UnsupportedOperationException("TODO");
    }
}
