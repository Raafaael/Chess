package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

public class BishopTest {

    private Board  board;
    private Bishop bishop;

    @Before
    public void setUp() {
        board = Board.getInstance();

        /* limpa o tabuleiro */
        for (int r = 0; r < 8; r++)
            for (int c = 0; c < 8; c++)
                board.setPiece(r, c, null);

        /* rei branco necessário – brancas embaixo */
        board.setPiece(7, 4, new King('W', 7, 4));

        bishop = new Bishop('W', 4, 4);
        board.setPiece(4, 4, bishop);
    }

    /**
     * Objetivo: Verificar se o bispo pode se mover para todas as diagonais livres.
     * Retorno: a lista de movimentos contém casas nas quatro diagonais.
     * Significado: o bispo está obedecendo corretamente sua regra de movimentação.
     */
    @Test(timeout = 2000)
    public void test_bishopMovesDiagonallyWhenUnblocked() {
        List<int[]> moves = bishop.pieceMovement(board);

        assertTrue(containsMove(moves, 3, 3));
        assertTrue(containsMove(moves, 3, 5));
        assertTrue(containsMove(moves, 5, 3));
        assertTrue(containsMove(moves, 5, 5));
    }

    /**
     * Objetivo: Verificar que o bispo não pode se mover em linha reta.
     * Retorno: a lista de movimentos não inclui casas adjacentes retas.
     * Significado: o bispo respeita seu movimento diagonal exclusivo.
     */
    @Test(timeout = 2000)
    public void test_bishopCannotMoveInvalidly() {
        List<int[]> moves = bishop.pieceMovement(board);
        assertFalse(containsMove(moves, 4, 5));
        assertFalse(containsMove(moves, 5, 4));
    }

    /**
     * Objetivo: Verificar se o bispo não ultrapassa peças da mesma cor.
     * Retorno: a casa ocupada pelo aliado não está nos movimentos, nem as posteriores.
     * Significado: o sistema bloqueia a movimentação ao encontrar aliados.
     */
    @Test(timeout = 2000)
    public void test_bishopStopsAtOwnPiece() {
        board.setPiece(3, 3, new Pawn('W', 3, 3));

        List<int[]> moves = bishop.pieceMovement(board);
        assertFalse(containsMove(moves, 3, 3));
        assertFalse(containsMove(moves, 2, 2));
    }

    /**
     * Objetivo: Verificar se o bispo pode capturar uma peça inimiga.
     * Retorno: a casa do inimigo está presente, mas as posteriores não.
     * Significado: o bispo pode capturar, mas não atravessa peças.
     */
    @Test(timeout = 2000)
    public void test_bishopCanCaptureEnemyButNotGoPast() {
        board.setPiece(3, 3, new Pawn('B', 3, 3));

        List<int[]> moves = bishop.pieceMovement(board);
        assertTrue (containsMove(moves, 3, 3));
        assertFalse(containsMove(moves, 2, 2));
    }

    /**
     * Objetivo: Verificar se o bispo é impedido de capturar diretamente o rei inimigo.
     * Retorno: o movimento até a casa do rei não está presente.
     * Significado: o sistema respeita a regra que impede a captura direta do rei.
     */
    @Test(timeout = 2000)
    public void test_bishopCannotCaptureEnemyKing() {
        board.setPiece(3, 3, new King('B', 3, 3));

        List<int[]> moves = bishop.pieceMovement(board);
        assertFalse(containsMove(moves, 3, 3));
    }

    /**
     * Objetivo: Verificar se o bispo é impedido de se mover ao revelar um xeque descoberto.
     * Retorno: o movimento que expõe o rei (para 3,3) não aparece na lista.
     * Significado: o bispo está sendo corretamente impedido de expor o rei.
     */
    @Test(timeout = 2000)
    public void test_bishopCannotExposeKingToDiscoveredCheck() {

        board.setPiece(2, 1, new King('W', 2, 1));     // rei branco
        board.setPiece(2, 6, new Rook('B', 2, 6));     // torre preta

        bishop = new Bishop('W', 2, 4);                // bloqueia
        board.setPiece(2, 4, bishop);

        List<int[]> moves = bishop.pieceMovement(board);
        assertFalse(containsMove(moves, 3, 3));        // libera a coluna
    }

    /* auxiliar */
    private boolean containsMove(List<int[]> moves, int r, int c) {
        for (int[] mv : moves)
            if (mv[0] == r && mv[1] == c) return true;
        return false;
    }
}
