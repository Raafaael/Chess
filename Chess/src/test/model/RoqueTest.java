package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RoqueTest {

    private Board     board;
    private ChessGame game;

    @Before
    public void setUp() {
        board = Board.getInstance();
        board.setupInitialPosition();
        game  = ChessGame.getInstance();
    }

    /**
     * Objetivo  : Verificar se o 'roque curto' das brancas ocorre quando o caminho está livre e o rei não passa por xeque.
     * Retorno   : `moveTo` devolve 'true'; rei em (7,6) e torre em (7,5).
     * Significado: garante que a implementação movimenta as duas peças corretamente e respeita as regras do roque.
     */
    @Test(timeout = 2000)
    public void test_roqueCurtoBrancas() {

        /* libera bispo e cavalo brancos */
        board.setPiece(7, 5, null);
        board.setPiece(7, 6, null);

        assertTrue(game.selectPiece(7, 4));   // rei
        assertTrue(game.moveTo(7, 7));        // clica torre

        assertTrue(board.getPiece(7, 6) instanceof King);
        assertTrue(board.getPiece(7, 5) instanceof Rook);
    }

    /**
     * Objetivo  : Verificar se o 'roque longo' das pretas ocorre quando permitido.
     * Retorno   : rei termina em (0,2) e torre em (0,3).
     * Significado: comprova que o roque longo também é suportado.
     */
    @Test(timeout = 2000)
    public void test_roqueLongoPretas() {

        /* primeiro lance das brancas para entregar o turno */
        game.selectPiece(6, 4);
        game.moveTo(4, 4);

        /* libera cavalo, bispo e dama pretos no lado da dama */
        board.setPiece(0, 1, null);
        board.setPiece(0, 2, null);
        board.setPiece(0, 3, null);

        assertTrue(game.selectPiece(0, 4));   // rei preto
        assertTrue(game.moveTo(0, 0));        // torre A

        assertTrue(board.getPiece(0, 2) instanceof King);
        assertTrue(board.getPiece(0, 3) instanceof Rook);
    }
}
