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

        // Coloca o rei branco em posição centralizada
        whiteKing = new King('W', 7, 4);
        board.setPiece(7, 4, whiteKing);
    }

    /**
     * Objetivo: Verificar se o rei pode se mover para todas as casas adjacentes válidas.
     * Retorno: movimentos do rei estão limitados a 1 casa em qualquer direção.
     * Significado: o rei obedece às regras de movimentação básicas do xadrez.
     */
    @Test(timeout = 2000)
    public void test_kingMovesOneSquareInAnyDirection() {
        List<int[]> moves = whiteKing.pieceMovement(board);

        assertFalse("King should have possible moves", moves.isEmpty());

        for (int[] move : moves) {
            int dRow = Math.abs(move[0] - whiteKing.getRow());
            int dCol = Math.abs(move[1] - whiteKing.getCol());
            assertTrue("King moves only one square", dRow <= 1 && dCol <= 1);
        }
    }

    /**
     * Objetivo: Verificar que o rei não pode mover mais que uma casa em qualquer direção.
     * Retorno: a lista de movimentos não inclui casas a duas ou mais casas de distância.
     * Significado: o rei está restrito ao movimento básico de uma casa por vez.
     */
    @Test(timeout = 2000)
    public void test_kingCannotMoveMoreThanOneSquare() {
        List<int[]> moves = whiteKing.pieceMovement(board);
        assertFalse(containsMove(moves, 5, 4)); // movimento a 2 casas para cima
        assertFalse(containsMove(moves, 7, 6)); // movimento a 2 casas para a direita
    }

    
    /**
     * Objetivo: Verificar se o rei é impedido de se mover para uma casa que está sob ataque inimigo.
     * Retorno: movimento para (6,4) é bloqueado por estar em xeque.
     * Significado: o sistema protege o rei contra movimentos suicidas.
     */
    @Test(timeout = 2000)
    public void test_kingCannotMoveIntoCheck() {
        Rook blackRook = new Rook('B', 5, 4);
        board.setPiece(5, 4, blackRook);

        List<int[]> moves = whiteKing.pieceMovement(board);

        assertFalse("King must not move into check at (6,4)", containsMove(moves, 6, 4));
    }

    /**
     * Objetivo: Verificar se o rei não pode capturar uma peça da própria cor.
     * Retorno: movimento para (6,4) não deve estar na lista.
     * Significado: o rei respeita as regras de não-captura de aliados.
     */
    @Test(timeout = 2000)
    public void test_kingCannotCaptureOwnPiece() {
        Pawn whitePawn = new Pawn('W', 6, 4);
        board.setPiece(6, 4, whitePawn);

        List<int[]> moves = whiteKing.pieceMovement(board);

        assertFalse("King must not capture own piece at (6,4)", containsMove(moves, 6, 4));
    }

    // Função auxiliar para verificar se (row, col) está na lista de movimentos
    private boolean containsMove(List<int[]> moves, int row, int col) {
        for (int[] move : moves) {
            if (move[0] == row && move[1] == col) return true;
        }
        return false;
    }
}
