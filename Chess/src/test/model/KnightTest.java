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
        // Limpa o tabuleiro para controle total
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board.setPiece(i, j, null);
            }
        }
        whiteKnight = new Knight('W', 3, 3); // d4
        board.setPiece(3, 3, whiteKnight);
    }

    @Test
    public void testKnightBasicMoves() {
        List<int[]> moves = whiteKnight.pieceMovement(board);

        // Cavaleiro deve ser capaz de realizar esses movimentos
        assertEquals(8, moves.size());
        int[][] expected = {
            {1, 2}, {1, 4}, {2, 1}, {2, 5}, {4, 1}, {4, 5}, {5, 2}, {5, 4}
        };
        for (int[] exp : expected) {
            assertTrue(containsMove(moves, exp[0], exp[1]));
        }
    }

    @Test
    public void testKnightCannotCaptureOwnPiece() {
        // Coloca um peão aliado
        Pawn whitePawn = new Pawn('W', 2, 5);
        board.setPiece(2, 5, whitePawn);

        List<int[]> moves = whiteKnight.pieceMovement(board);

        // Cavaleiro deve ser incapaz de se mover para esse ponto
        assertFalse(containsMove(moves, 2, 5));
    }

    @Test
    public void testKnightCanCaptureOpponentPiece() {
    	// Coloca um peão inimigo
        Pawn blackPawn = new Pawn('B', 2, 5);
        board.setPiece(2, 5, blackPawn);

        List<int[]> moves = whiteKnight.pieceMovement(board);

        // Cavaleiro consegue capturar o peão inimigo
        assertTrue(containsMove(moves, 2, 5));
    }

 // Auxiliar para verificar movimento na lista
    private boolean containsMove(List<int[]> moves, int row, int col) {
        for (int[] move : moves) {
            if (move[0] == row && move[1] == col) return true;
        }
        return false;
    }
}