package br.icev.vendas;

import br.icev.vendas.excecoes.ErroPagamentoException;
import br.icev.vendas.excecoes.QuantidadeInvalidaException;
import br.icev.vendas.excecoes.SemEstoqueException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/** NÃO ALTERE ESTES TESTES. Implemente as classes de produção para fazê-los passar. */
class CheckoutTeste {

    private static Produto p(String codigo, String nome, String preco) {
        return new Produto(codigo, nome, new BigDecimal(preco));
    }

    /** Fluxo: validar estoque -> cobrar -> reservar -> criar pedido pago */
    @Test
    void checkoutComSucesso() throws Exception {
        Estoque est = new Estoque();
        est.adicionarEstoque("A", 5);

        Carrinho carrinho = new Carrinho();
        carrinho.adicionar(p("A","Item A","20.00"), 2); // total 40.00

        PoliticaDesconto semDesconto = subtotal -> subtotal;

        final BigDecimal[] cobrado = {null};
        GatewayPagamento gateway = valor -> {
            cobrado[0] = valor;
            return "AUTH-123";
        };

        Map<String, Integer> itensPorCodigo = new HashMap<>();
        itensPorCodigo.put("A", 2);

        Pedido pedido = checkout(est, carrinho, gateway, semDesconto, itensPorCodigo);

        assertEquals(new BigDecimal("40.00"), cobrado[0]);
        assertEquals(new BigDecimal("40.00"), pedido.getTotalPago());
        assertEquals("AUTH-123", pedido.getCodigoAutorizacao());
        assertEquals(Pedido.Status.PAGO, pedido.getStatus());
        assertEquals(3, est.getDisponivel("A")); // 5 - 2
        assertEquals(2, pedido.getQuantidadeItem("A"));
    }

    @Test
    void falhaSeSemEstoqueSuficiente() throws QuantidadeInvalidaException {
        Estoque est = new Estoque();
        est.adicionarEstoque("A", 1);

        Carrinho carrinho = new Carrinho();
        carrinho.adicionar(p("A","Item A","10.00"), 2);

        PoliticaDesconto sem = x -> x;

        final boolean[] cobrou = { false };
        GatewayPagamento gateway = valor -> { cobrou[0] = true; return "NAO-USAR"; };

        Map<String, Integer> itens = new HashMap<>();
        itens.put("A", 2);

        assertThrows(SemEstoqueException.class, () -> checkout(est, carrinho, gateway, sem, itens));
        assertFalse(cobrou[0], "Gateway não deve ser chamado quando não há estoque");
    }

    @Test
    void repropagaFalhaDePagamentoSemAlterarEstoque() throws Exception {
        Estoque est = new Estoque();
        est.adicionarEstoque("A", 2);

        Carrinho carrinho = new Carrinho();
        carrinho.adicionar(p("A","Item A","10.00"), 2);

        PoliticaDesconto sem = x -> x;

        GatewayPagamento gateway = valor -> { throw new ErroPagamentoException("Cartão recusado"); };

        Map<String, Integer> itens = new HashMap<>();
        itens.put("A", 2);

        assertThrows(ErroPagamentoException.class, () -> checkout(est, carrinho, gateway, sem, itens));
        // estoque deve permanecer o mesmo, já que a reserva só acontece após o pagamento
        assertEquals(2, est.getDisponivel("A"));
    }

    // ---- Função utilitária usada pelos testes ----
    private Pedido checkout(Estoque est, Carrinho carrinho, GatewayPagamento gateway,
                            PoliticaDesconto politica, Map<String, Integer> itensPorCodigo)
            throws SemEstoqueException, QuantidadeInvalidaException, ErroPagamentoException {

        for (Map.Entry<String, Integer> e : itensPorCodigo.entrySet()) {
            int disponivel = est.getDisponivel(e.getKey());
            if (e.getValue() <= 0) {
                throw new QuantidadeInvalidaException("Quantidade inválida");
            }
            if (disponivel < e.getValue()) {
                throw new SemEstoqueException("Sem estoque para " + e.getKey());
            }
        }

        BigDecimal total = carrinho.getTotalCom(politica);
        if (total.compareTo(BigDecimal.ZERO) < 0) total = BigDecimal.ZERO;

        String autorizacao = gateway.cobrar(total);

        for (Map.Entry<String, Integer> e : itensPorCodigo.entrySet()) {
            est.reservar(e.getKey(), e.getValue());
        }

        return new Pedido(itensPorCodigo, total, autorizacao, Pedido.Status.PAGO);
    }
}
