package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

public class PawnTest {

    private Board board;
    private Pawn  whitePawn;
    private Pawn  blackPawn;

    @Before
    public void setUp() {
        board = Board.getInstance();

        /* limpa tabuleiro */
        for (int r = 0; r < 8; r++)
            for (int c = 0; c < 8; c++)
                board.setPiece(r, c, null);

        board.setPiece(7, 4, new King('W', 7, 4));
        board.setPiece(0, 4, new King('B', 0, 4));

        whitePawn = new Pawn('W', 6, 4);   // brancas embaixo
        blackPawn = new Pawn('B', 1, 3);   // pretas em cima

        board.setPiece(6, 4, whitePawn);
        board.setPiece(1, 3, blackPawn);
    }

    /**
     * Objetivo: Verificar se o peão branco pode realizar seus dois movimentos iniciais.
     * Retorno: lista de movimentos inclui (5,4) e (4,4).
     * Significado: o peão branco está livre para seus primeiros movimentos válidos.
     */
    @Test(timeout = 2000)
    public void test_whitePawnInitialMovesAreAvailable() {
        List<int[]> moves = whitePawn.pieceMovement(board);
        assertTrue(containsMove(moves, 5, 4));
        assertTrue(containsMove(moves, 4, 4));
    }

    /**
     * Objetivo: Verificar que o peão não pode mover para trás.
     * Retorno: lista de movimentos não inclui a casa atrás do peão.
     * Significado: o peão respeita a regra de não retroceder.
     */
    @Test(timeout = 2000)
    public void test_pawnCannotMoveBackward() {
        List<int[]> moves = whitePawn.pieceMovement(board);
        assertFalse(containsMove(moves, 7, 4));
    }

    /**
     * Objetivo: Verificar se o peão preto pode realizar seus dois movimentos iniciais.
     * Retorno: lista inclui (2,3) e (3,3).
     * Significado: o peão preto tem caminho livre na linha inicial.
     */
    @Test(timeout = 2000)
    public void test_blackPawnInitialMovesAreAvailable() {
        List<int[]> moves = blackPawn.pieceMovement(board);
        assertTrue(containsMove(moves, 2, 3));
        assertTrue(containsMove(moves, 3, 3));
    }

    /**
     * Objetivo: Verificar se o peão é impedido de avançar quando há peça bloqueando.
     * Retorno: lista não contém casas à frente.
     * Significado: o sistema bloqueia o avanço em caso de obstrução.
     */
    @Test(timeout = 2000)
    public void test_pawnCannotMoveThroughOtherPieces() {
        board.setPiece(5, 4, new Pawn('W', 5, 4));

        List<int[]> moves = whitePawn.pieceMovement(board);
        assertFalse(containsMove(moves, 5, 4));
        assertFalse(containsMove(moves, 4, 4));
    }

    /**
     * Objetivo: Verificar se o peão pode capturar peças inimigas nas diagonais.
     * Retorno: lista inclui (5,3) e (5,5).
     * Significado: o sistema permite capturas diagonais válidas.
     */
    @Test(timeout = 2000)
    public void test_pawnCanCaptureDiagonally() {
        board.setPiece(5, 3, new Pawn('B', 5, 3));
        board.setPiece(5, 5, new Pawn('B', 5, 5));

        List<int[]> moves = whitePawn.pieceMovement(board);
        assertTrue(containsMove(moves, 5, 3));
        assertTrue(containsMove(moves, 5, 5));
    }

    /**
     * Objetivo: Verificar se o peão é impedido de capturar o rei inimigo diretamente.
     * Retorno: lista não contém a casa do rei inimigo.
     * Significado: o sistema proíbe a captura direta do rei.
     */
    @Test(timeout = 2000)
    public void test_pawnCannotCaptureEnemyKing() {
        board.setPiece(5, 3, new King('B', 5, 3));

        List<int[]> moves = whitePawn.pieceMovement(board);
        assertFalse(containsMove(moves, 5, 3));
    }

    /**
     * Objetivo: Verificar se o peão é impedido de realizar um movimento que deixaria seu rei em xeque.
     * Retorno: lista não contém (5,4).
     * Significado: o sistema impede movimentos que violam a segurança do rei.
     */
    @Test(timeout = 2000)
    public void test_pawnCannotMoveIntoCheck() {
        /* torre preta duas casas acima do peão */
        board.setPiece(4, 4, new Rook('B', 4, 4));

        List<int[]> moves = whitePawn.pieceMovement(board);
        assertTrue(containsMove(moves, 5, 4));
    }

    /* auxiliar */
    private boolean containsMove(List<int[]> moves, int r, int c) {
        for (int[] mv : moves)
            if (mv[0] == r && mv[1] == c) return true;
        return false;
    }
}
