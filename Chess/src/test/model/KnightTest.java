package model;

import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class KnightTest {

    private Board board;
    private Knight whiteKnight;

    @Before
    public void setUp() {
        board = Board.getInstance();

        // Limpa o tabuleiro
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board.setPiece(i, j, null);
            }
        }

        // Rei necessário para testMoveSafety
        King whiteKing = new King('W', 7, 4);
        board.setPiece(7, 4, whiteKing);

        whiteKnight = new Knight('W', 3, 3); // d4
        board.setPiece(3, 3, whiteKnight);
    }

    /**
     * Objetivo: Verificar se o cavalo pode se mover para as 8 casas em "L" disponíveis.
     * Retorno: a lista de movimentos contém exatamente as 8 casas padrão do cavalo.
     * Significado: o cavalo obedece à sua movimentação característica.
     */
    @Test(timeout = 2000)
    public void test_knightHasEightValidMovesFromCenter() {
        List<int[]> moves = whiteKnight.pieceMovement(board);

        int[][] expected = {
            {1, 2}, {1, 4}, {2, 1}, {2, 5},
            {4, 1}, {4, 5}, {5, 2}, {5, 4}
        };

        assertEquals("Knight should have 8 possible moves", 8, moves.size());

        for (int[] pos : expected) {
            assertTrue("Knight should be able to move to (" + pos[0] + "," + pos[1] + ")",
                    containsMove(moves, pos[0], pos[1]));
        }
    }
    
    /**
     * Objetivo: Verificar que o cavalo não pode se mover de forma diferente do padrão “L”.
     * Retorno: a lista de movimentos não inclui movimentos retos ou diagonais simples.
     * Significado: o cavalo está limitado ao movimento em “L” característico.
     */
    @Test(timeout = 2000)
    public void test_knightCannotMoveInvalidly() {
        List<int[]> moves = whiteKnight.pieceMovement(board);
        assertFalse(containsMove(moves, 2, 3)); // movimento reto para cima (inválido)
        assertFalse(containsMove(moves, 3, 4)); // movimento reto para a direita (inválido)
    }


    /**
     * Objetivo: Verificar que o cavalo não pode capturar uma peça da mesma cor.
     * Retorno: a casa ocupada por aliado não aparece na lista de movimentos.
     * Significado: o sistema bloqueia corretamente capturas inválidas.
     */
    @Test(timeout = 2000)
    public void test_knightCannotCaptureOwnPiece() {
        Pawn whitePawn = new Pawn('W', 2, 5);
        board.setPiece(2, 5, whitePawn);

        List<int[]> moves = whiteKnight.pieceMovement(board);

        assertFalse("Knight should not be able to capture own piece", containsMove(moves, 2, 5));
    }

    /**
     * Objetivo: Verificar se o cavalo pode capturar uma peça inimiga.
     * Retorno: a casa do inimigo está presente na lista de movimentos.
     * Significado: o cavalo consegue capturar corretamente peças adversárias.
     */
    @Test(timeout = 2000)
    public void test_knightCanCaptureEnemyPiece() {
        Pawn blackPawn = new Pawn('B', 2, 5);
        board.setPiece(2, 5, blackPawn);

        List<int[]> moves = whiteKnight.pieceMovement(board);

        assertTrue("Knight should be able to capture enemy piece", containsMove(moves, 2, 5));
    }

    // Auxiliar para verificar se um movimento está na lista
    private boolean containsMove(List<int[]> moves, int row, int col) {
        for (int[] move : moves) {
            if (move[0] == row && move[1] == col) return true;
        }
        return false;
    }
}
