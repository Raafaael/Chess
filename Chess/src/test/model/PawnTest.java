package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class PawnTest {

    private Board board;
    private Pawn whitePawn;
    private Pawn blackPawn;

    @Before
    public void setUp() {
        board = Board.getInstance();

        // Limpa o tabuleiro para controle total
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                board.setPiece(r, c, null);
            }
        }

        King whiteKing = new King('W', 7, 4);
        board.setPiece(7, 4, whiteKing);

        King blackKing = new King('B', 0, 4);
        board.setPiece(0, 4, blackKing);
        
        // Verifique se o rei branco está no tabuleiro
        Piece king = board.getPiece(7, 4);
        System.out.println("White king at (7,4): " + (king != null && king instanceof King));
        
        whitePawn = new Pawn('W', 6, 4);
        blackPawn = new Pawn('B', 1, 3);

        board.setPiece(6, 4, whitePawn);
        board.setPiece(5, 4, null);
        board.setPiece(4, 4, null);
        board.setPiece(1, 3, blackPawn);
        board.setPiece(2, 3, null);
        board.setPiece(3, 3, null);
        
    }


    @Test
    public void testWhitePawnInitialMoves() {
        List<int[]> moves = whitePawn.pieceMovement(board);
    	for (int[] move : moves) {
    	    System.out.println("White pawn move: " + move[0] + ", " + move[1]);
    	}
        assertTrue(containsMove(moves, 5, 4));
        assertTrue(containsMove(moves, 4, 4));
    }

    @Test
    public void testBlackPawnInitialMoves() {
    	List<int[]> moves = blackPawn.pieceMovement(board);
    	for (int[] move : moves) {
    	    System.out.println("Black pawn move: " + move[0] + ", " + move[1]);
    	}
        assertTrue(containsMove(moves, 2, 3));
        assertTrue(containsMove(moves, 3, 3));
    }

    @Test
    public void testPawnCannotMoveThroughPieces() {
        Pawn blockingPawn = new Pawn('W', 5, 4);
        board.setPiece(5, 4, blockingPawn);

        List<int[]> moves = whitePawn.pieceMovement(board);
        assertFalse(containsMove(moves, 5, 4));
        assertFalse(containsMove(moves, 4, 4));
    }

    @Test
    public void testPawnCaptureMoves() {
        Pawn enemyLeft = new Pawn('B', 5, 3);
        Pawn enemyRight = new Pawn('B', 5, 5);

        board.setPiece(5, 3, enemyLeft);
        board.setPiece(5, 5, enemyRight);
        System.out.println("Enemy Left piece at (5,3): " + board.getPiece(5, 3));
        System.out.println("Enemy Right piece at (5,5): " + board.getPiece(5, 5));

        List<int[]> moves = whitePawn.pieceMovement(board);
        assertTrue(containsMove(moves, 5, 3));
        assertTrue(containsMove(moves, 5, 5));
    }

    @Test
    public void testPawnDoesNotCaptureKing() {
        King enemyKing = new King('B', 5, 3);
        board.setPiece(5, 3, enemyKing);

        List<int[]> moves = whitePawn.pieceMovement(board);
        assertFalse(containsMove(moves, 5, 3));
    }

    @Test
    public void testPawnDoesNotMoveIntoCheck() {
        // Torre preta ameaçando a casa (5,4)
        Rook blackRook = new Rook('B', 3, 4);
        board.setPiece(3, 4, blackRook);

        List<int[]> moves = whitePawn.pieceMovement(board);
        // O peão não deve poder mover para a casa 5,4 pois isso deixaria o rei em xeque
        assertFalse(containsMove(moves, 5, 4));
    }

    // Método auxiliar para verificar se uma lista de movimentos contém a posição (row,col)
    private boolean containsMove(List<int[]> moves, int row, int col) {
        for (int[] move : moves) {
            if (move[0] == row && move[1] == col) {
                return true;
            }
        }
        return false;
    }

}
