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

        // Limpa o tabuleiro
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

        assertFalse("King should have at least one move", moves.isEmpty());

        for (int[] move : moves) {
            int dRow = Math.abs(move[0] - whiteKing.getRow());
            int dCol = Math.abs(move[1] - whiteKing.getCol());
            assertTrue("King moves max one square in any direction", dRow <= 1 && dCol <= 1);
            assertTrue("Move must be inside board", board.isValidPosition(move[0], move[1]));
        }
    }

    @Test
    public void testKingDoesNotMoveIntoCheck() {
        Rook blackRook = new Rook('B', 5, 4);
        board.setPiece(5, 4, blackRook);

        List<int[]> moves = whiteKing.pieceMovement(board);

        for (int[] move : moves) {
            assertFalse("King must not move into check at (6,4)", move[0] == 6 && move[1] == 4);
        }
    }

    @Test
    public void testKingCannotCaptureOwnPiece() {
        Pawn whitePawn = new Pawn('W', 6, 4);
        board.setPiece(6, 4, whitePawn);

        List<int[]> moves = whiteKing.pieceMovement(board);

        assertFalse("King cannot capture own piece", containsMove(moves, 6, 4));
    }

    @Test
    public void testKingCanCaptureEnemyPiece() {
        Pawn blackPawn = new Pawn('B', 6, 5);
        board.setPiece(6, 5, blackPawn);

        List<int[]> moves = whiteKing.pieceMovement(board);

        assertTrue("King should be able to capture enemy piece", containsMove(moves, 6, 5));
    }

    @Test
    public void testKingMovesDiagonallyAndOrthogonally() {
        List<int[]> moves = whiteKing.pieceMovement(board);

        // O rei está em (7,4), deve poder mover para casas adjacentes
        int[][] expectedMoves = {
            {6, 3}, {6, 4}, {6, 5},
            {7, 3},         {7, 5}
        };

        for (int[] expected : expectedMoves) {
            assertTrue("King should move to (" + expected[0] + "," + expected[1] + ")",
                containsMove(moves, expected[0], expected[1]));
        }
    }

    @Test
    public void testKingDoesNotMoveOutsideBoard() {
        List<int[]> moves = whiteKing.pieceMovement(board);

        for (int[] move : moves) {
            assertTrue("Move must be inside board", board.isValidPosition(move[0], move[1]));
        }
    }


    // Método auxiliar para verificar se lista contém o movimento
    private boolean containsMove(List<int[]> moves, int row, int col) {
        for (int[] move : moves) {
            if (move[0] == row && move[1] == col) return true;
        }
        return false;
    }
}
