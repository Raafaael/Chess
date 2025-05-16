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

        whitePawn = new Pawn('W', 6, 4);
        blackPawn = new Pawn('B', 1, 3);

        board.setPiece(6, 4, whitePawn);
        board.setPiece(1, 3, blackPawn);

        // Garante que as casas para movimento duplo estejam vazias
        board.setPiece(2, 3, null);
        board.setPiece(3, 3, null);

        // Debug: imprime o estado do tabuleiro ao redor do peão preto
        System.out.println("Board state around black pawn:");
        for (int r = 1; r <= 3; r++) {
            Piece p = board.getPiece(r, 3);
            System.out.println("Position (" + r + ",3): " + (p == null ? "empty" : p.getClass().getSimpleName()));
        }
    }


    @Test
    public void testWhitePawnInitialMoves() {
    	System.out.println("Is empty at next row (5,4): " + board.isEmpty(5, 4));
    	System.out.println("Is empty at two steps row (4,4): " + board.isEmpty(4, 4));
        List<int[]> moves = whitePawn.pieceMovement(board);
        System.out.println("Peca branca:");
        for (int[] move : moves) {
            System.out.println(move[0] + ", " + move[1]);
        }
        assertTrue(containsMove(moves, 5, 4)); // move 1 frente
        assertTrue(containsMove(moves, 4, 4)); // move 2 frente
    }

    @Test
    public void testBlackPawnInitialMoves() {
        List<int[]> moves = blackPawn.pieceMovement(board);
        System.out.println("Peca preta:");
        for (int[] move : moves) {
            System.out.println(move[0] + ", " + move[1]);
        }
        assertTrue(containsMove(moves, 2, 3)); // move 1 frente
        assertTrue(containsMove(moves, 3, 3)); // move 2 frente
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
        Set<String> seen = new HashSet<>();
        for (int[] move : moves) {
            String key = move[0] + "," + move[1];
            if (!seen.contains(key)) {
                seen.add(key);
                if (move[0] == row && move[1] == col) {
                    return true;
                }
            }
        }
        return false;
    }
}
