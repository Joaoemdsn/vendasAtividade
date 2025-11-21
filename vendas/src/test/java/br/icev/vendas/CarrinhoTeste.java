package br.icev.vendas;

import br.icev.vendas.excecoes.QuantidadeInvalidaException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/** NÃO ALTERE ESTES TESTES. Implemente as classes de produção para fazê-los passar. */
class CarrinhoTeste {

    private static Produto p(String codigo, String nome, String preco) {
        return new Produto(codigo, nome, new BigDecimal(preco));
    }

    @Test
    void somaItensComArredondamento() throws QuantidadeInvalidaException {
        Carrinho c = new Carrinho();
        c.adicionar(p("A","Item A","1.115"), 2);
        c.adicionar(p("B","Item B","17.67"), 1);
        assertEquals(new BigDecimal("19.90"), c.getSubtotal());
    }

    @Test
    void naoPermiteQuantidadeInvalida() {
        Carrinho c = new Carrinho();
        assertThrows(QuantidadeInvalidaException.class, () -> c.adicionar(p("A","Item A","10.00"), 0));
        assertThrows(QuantidadeInvalidaException.class, () -> c.adicionar(p("A","Item A","10.00"), -1));
    }

    @Test
    void acumulaQuantidadeMesmoCodigo() throws QuantidadeInvalidaException {
        Carrinho c = new Carrinho();
        c.adicionar(p("A","Item A","5.00"), 1);
        c.adicionar(p("A","Item A","5.00"), 3);
        assertEquals(4, c.getTotalItens());
        assertEquals(new BigDecimal("20.00"), c.getSubtotal());
    }

    @Test
    void aplicaDescontoUmaUnicaVezENuncaNegativo() throws QuantidadeInvalidaException {
        Carrinho c = new Carrinho();
        c.adicionar(p("A","Item A","50.00"), 1);

        PoliticaDesconto dezPorCento = subtotal -> subtotal.multiply(new BigDecimal("0.90"));
        assertEquals(new BigDecimal("45.00"), c.getTotalCom(dezPorCento));

        PoliticaDesconto louco = subtotal -> new BigDecimal("-9999");
        assertEquals(new BigDecimal("0.00"), c.getTotalCom(louco));
    }
}
