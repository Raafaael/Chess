package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ChessGameTest {

    private ChessGame game;

    @Before
    public void setUp() {
        game = new ChessGame();
        game.resetBoard();
    }

    /**
     * Objetivo: Verificar se o jogador pode selecionar uma peça da sua própria cor.
     * Retorno: selectPiece retorna true.
     * Significado: o sistema permite selecionar uma peça válida conforme o turno.
     */
    @Test(timeout = 2000)
    public void test_selectOwnPieceReturnsTrue() {
        Pawn whitePawn = new Pawn('W', 6, 4);
        Board.getInstance().setPiece(6, 4, whitePawn);

        assertTrue("Should be able to select own piece", game.selectPiece(6, 4));
        assertEquals(whitePawn, game.getSelectedPiece());
    }

    /**
     * Objetivo: Verificar se o jogador é impedido de selecionar uma peça do adversário.
     * Retorno: selectPiece retorna false.
     * Significado: o sistema protege contra seleção de peças erradas no turno atual.
     */
    @Test(timeout = 2000)
    public void test_cannotSelectOpponentPiece() {
        Pawn blackPawn = new Pawn('B', 1, 4);
        Board.getInstance().setPiece(1, 4, blackPawn);

        assertFalse("Should not be able to select opponent piece", game.selectPiece(1, 4));
    }

    /**
     * Objetivo: Verificar se uma jogada legal é executada corretamente e o turno é trocado.
     * Retorno: selectTarget retorna true, e o turno passa para o adversário.
     * Significado: o movimento foi validado e executado corretamente.
     */
    @Test(timeout = 2000)
    public void test_legalMoveSucceedsAndTurnChanges() {
        Pawn whitePawn = new Pawn('W', 6, 4);
        Board.getInstance().setPiece(6, 4, whitePawn);

        assertTrue(game.selectPiece(6, 4));
        assertTrue(game.selectTarget(5, 4));

        assertEquals("Turn should change to Black", 'B', game.getCurrentTurn());
    }

    /**
     * Objetivo: Verificar se um movimento ilegal é rejeitado e o turno não é alterado.
     * Retorno: selectTarget retorna false, e o turno permanece o mesmo.
     * Significado: o sistema protege contra movimentos inválidos.
     */
    @Test(timeout = 2000)
    public void test_illegalMoveFailsAndTurnStays() {
        Pawn whitePawn = new Pawn('W', 6, 4);
        Board.getInstance().setPiece(6, 4, whitePawn);

        assertTrue(game.selectPiece(6, 4));
        assertFalse("Move is illegal (not a valid direction)", game.selectTarget(4, 3));

        assertEquals("Turn should remain White", 'W', game.getCurrentTurn());
    }

    /**
     * Objetivo: Verificar se um movimento que deixaria o rei em xeque é rejeitado.
     * Retorno: selectTarget retorna false.
     * Significado: o sistema detecta xeque descoberto e impede a jogada.
     */
    @Test(timeout = 2000)
    public void test_moveThatLeavesKingInCheckIsRejected() {
        King whiteKing = new King('W', 7, 4);
        Board.getInstance().setPiece(7, 4, whiteKing);

        Rook blackRook = new Rook('B', 0, 4); // torre que ameaça o rei
        Board.getInstance().setPiece(0, 4, blackRook);

        Pawn whitePawn = new Pawn('W', 5, 4); // está protegendo o rei
        Board.getInstance().setPiece(5, 4, whitePawn);

        game.selectPiece(5, 4);
        assertFalse("Move would expose king to discovered check", game.selectTarget(4, 4));
    }
}
