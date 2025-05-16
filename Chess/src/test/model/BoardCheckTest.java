package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BoardCheckTest {

    private Board board;
    private King whiteKing;
    private Rook blackRook;

    @Before
    public void setUp() {
        board = Board.getInstance();

        // Limpar tabuleiro
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                board.setPiece(r, c, null);
            }
        }

        whiteKing = new King('W', 7, 4);
        board.setPiece(7, 4, whiteKing);

        blackRook = new Rook('B', 5, 4);
        board.setPiece(5, 4, blackRook);
    }

    @Test
    public void testIsInCheckTrue() {
        assertTrue(board.isInCheck('W'));
    }

    @Test
    public void testIsInCheckFalse() {
        board.setPiece(5, 4, null); // Remove torre ameaçadora
        assertFalse(board.isInCheck('W'));
    }
}
