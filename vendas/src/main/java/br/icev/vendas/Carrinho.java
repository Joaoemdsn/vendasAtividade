package br.icev.vendas;

import br.icev.vendas.excecoes.QuantidadeInvalidaException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Carrinho {
    private final Map<String, ItemCarrinho> itens = new HashMap<>();

    public void adicionar(Produto produto, int quantidade) throws QuantidadeInvalidaException {
        if (quantidade <= 0) {
            throw new QuantidadeInvalidaException("Quantidade deve ser maior que zero");
        }
        String codigo = produto.getCodigo();
        int novaQuantidade = itens.containsKey(codigo) ? itens.get(codigo).quantidade : 0;
        itens.put(codigo, new ItemCarrinho(produto, novaQuantidade + quantidade));
    }

    public BigDecimal getSubtotal() {
        BigDecimal subtotal = BigDecimal.ZERO;
        for (ItemCarrinho item : itens.values()) {
            BigDecimal preco = item.produto.getPrecoUnitario();
            BigDecimal totalItem = preco.multiply(new BigDecimal(item.quantidade));
            subtotal = subtotal.add(totalItem);
        }
        return UtilDinheiro.arredondar2(subtotal);
    }

    public BigDecimal getTotalCom(PoliticaDesconto politica) {
        BigDecimal subtotal = getSubtotal();
        BigDecimal total = politica.aplicar(subtotal);
        if (total == null || total.compareTo(BigDecimal.ZERO) < 0) {
            return UtilDinheiro.arredondar2(BigDecimal.ZERO);
        }
        return UtilDinheiro.arredondar2(total);
    }

    public int getTotalItens() {
        int total = 0;
        for (ItemCarrinho item : itens.values()) {
            total += item.quantidade;
        }
        return total;
    }

    private static class ItemCarrinho {
        Produto produto;
        int quantidade;

        ItemCarrinho(Produto produto, int quantidade) {
            this.produto = produto;
            this.quantidade = quantidade;
        }
    }
}
