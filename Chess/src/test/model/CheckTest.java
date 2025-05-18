package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

/**
 * Esta classe testa as situações de xeque direto e xeque descoberto (indireto) contra o rei.
 * Cada teste está documentado com objetivo, retorno e significado, seguindo o Capítulo 18.
 */
public class CheckTest {

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

        // Rei branco posicionado
        whiteKing = new King('W', 5, 2);
        board.setPiece(5, 2, whiteKing);
    }

    /**
     * Objetivo: Verificar se o método isInCheck retorna true quando o rei está sob ataque direto.
     * Retorno: true de board.isInCheck('W')
     * Significado: o sistema reconhece corretamente o xeque direto.
     */
    @Test(timeout = 2000)
    public void test_isInCheckReturnsTrue_whenKingIsUnderDirectCheck() {
        Rook blackRook = new Rook('B', 5, 4);
        board.setPiece(5, 4, blackRook);

        assertTrue("King should be in direct check", board.isInCheck('W'));
    }

    /**
     * Objetivo: Verificar se o método isInCheck retorna false quando não há ameaça ao rei.
     * Retorno: false de board.isInCheck('W')
     * Significado: o rei está seguro; o sistema detecta ausência de xeque.
     */
    @Test(timeout = 2000)
    public void test_isInCheckReturnsFalse_whenThreatIsRemoved() {
        Rook blackRook = new Rook('B', 5, 4);
        board.setPiece(5, 4, blackRook);

        board.setPiece(5, 4, null); // Remove ameaça

        assertFalse("Rei não deve estar em cheque", board.isInCheck('W'));
    }

    /**
     * Objetivo: Verificar se uma peça é impedida de se mover quando isso causaria xeque descoberto.
     * Retorno: movimento ausente na lista retornada por pieceMovement
     * Significado: o sistema bloqueia corretamente jogadas que revelariam uma ameaça ao rei.
     */
    @Test(timeout = 2000)
    public void test_discoveredCheckPreventsPieceFromMoving() {
        Rook blackRook = new Rook('B', 5, 4);
        board.setPiece(5, 4, blackRook);

        Pawn whitePawn = new Pawn('W', 5, 3);
        board.setPiece(5, 3, whitePawn);

        List<int[]> moves = whitePawn.pieceMovement(board);

        assertFalse("Movimento que expõe o Rei para cheque não é permitido", containsMove(moves, 4, 3));
    }

    // Função auxiliar para verificar se um movimento está presente na lista
    private boolean containsMove(List<int[]> moves, int row, int col) {
        for (int[] move : moves) {
            if (move[0] == row && move[1] == col) return true;
        }
        return false;
    }
}
