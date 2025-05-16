package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

public class KingTest {

    private Board board;
    private King whiteKing;

    @Before
    public void setUp() {
        board = Board.getInstance();

        // Limpa o tabuleiro para controle total
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                board.setPiece(r, c, null);
            }
        }

        whiteKing = new King('W', 7, 4);
        board.setPiece(7, 4, whiteKing);
    }

    @Test
    public void testKingBasicMoves() {
        List<int[]> moves = whiteKing.pieceMovement(board);

        // O rei deve poder mover 1 casa em qualquer direção válida
        assertTrue(moves.size() > 0);
        for (int[] move : moves) {
            assertTrue(Math.abs(move[0] - whiteKing.getRow()) <= 1);
            assertTrue(Math.abs(move[1] - whiteKing.getCol()) <= 1);
        }
    }

    @Test
    public void testKingDoesNotMoveIntoCheck() {
        // Posicionar torre preta atacando a casa ao lado do rei
        Rook blackRook = new Rook('B', 5, 4);
        board.setPiece(5, 4, blackRook);

        List<int[]> moves = whiteKing.pieceMovement(board);

        // Rei não pode mover para (6,4) que está em xeque
        for (int[] move : moves) {
            assertFalse(move[0] == 6 && move[1] == 4);
        }
    }

    @Test
    public void testKingCannotCaptureOwnPiece() {
        Pawn whitePawn = new Pawn('W', 6, 4);
        board.setPiece(6, 4, whitePawn);

        List<int[]> moves = whiteKing.pieceMovement(board);

        // Rei não pode capturar peça própria
        assertFalse(containsMove(moves, 6, 4));
    }

    // Auxiliar para verificar movimento na lista
    private boolean containsMove(List<int[]> moves, int row, int col) {
        for (int[] move : moves) {
            if (move[0] == row && move[1] == col) return true;
        }
        return false;
    }
}
