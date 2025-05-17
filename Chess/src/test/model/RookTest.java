package model;

import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class RookTest {
    private Board board;
    private Rook whiteRook;

    @Before
    public void setUp() {
        board = Board.getInstance();
        // Limpa o tabuleiro para controle total
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board.setPiece(i, j, null);
            }
        }
        whiteRook = new Rook('W', 3, 3);
        board.setPiece(3, 3, whiteRook);
    }

    @Test
    public void testRookBasicMoves() {
        List<int[]> moves = whiteRook.pieceMovement(board);

        // Torre deve ser capaz de realizar esses movimentos
        assertEquals(14, moves.size());
        int[][] expected = {
            {1, 3}, {2, 3}, {4, 3}, {5, 3}, {6, 3}, {7, 3},
            {3, 0}, {3, 1}, {3, 2}, {3, 4}, {3, 5}, {3, 6}, {3, 7}
        };
        for (int[] exp : expected) {
            assertTrue(containsMove(moves, exp[0], exp[1]));
        }
    }

    @Test
    public void testRookCannotCaptureOwnPiece() {
        // Coloca um peão aliado
        Pawn whitePawn = new Pawn('W', 1, 3);
        board.setPiece(1, 3, whitePawn);

        List<int[]> moves = whiteRook.pieceMovement(board);

        // Torre deve ser incapaz de se mover para esse ponto
        assertFalse(containsMove(moves, 1, 3));
    }

    @Test
    public void testRookCanCaptureOpponentPiece() {
        // Coloca um peão inimigo
        Pawn blackPawn = new Pawn('B', 1, 3);
        board.setPiece(1, 3, blackPawn);

        List<int[]> moves = whiteRook.pieceMovement(board);

        // Torre consegue capturar o peão inimigo
        assertTrue(containsMove(moves, 1, 3));
    }

    // Auxiliar para verificar movimento na lista
    private boolean containsMove(List<int[]> moves, int row, int col) {
        for (int[] move : moves) {
            if (move[0] == row && move[1] == col) return true;
        }
        return false;
    }
}