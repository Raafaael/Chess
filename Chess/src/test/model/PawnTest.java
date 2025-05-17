package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

public class PawnTest {

    private Board board;
    private Pawn whitePawn;
    private Pawn blackPawn;

    @Before
    public void setUp() {
        board = Board.getInstance();

        // Limpa o tabuleiro
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                board.setPiece(r, c, null);
            }
        }

        // Posiciona o rei para o testMoveSafety funcionar corretamente
        King whiteKing = new King('W', 7, 4);
        King blackKing = new King('B', 0, 4);
        board.setPiece(7, 4, whiteKing);
        board.setPiece(0, 4, blackKing);

        whitePawn = new Pawn('W', 6, 4);
        blackPawn = new Pawn('B', 1, 3);

        board.setPiece(6, 4, whitePawn);
        board.setPiece(1, 3, blackPawn);

        // Limpa o caminho para o movimento inicial
        board.setPiece(5, 4, null);
        board.setPiece(4, 4, null);
        board.setPiece(2, 3, null);
        board.setPiece(3, 3, null);
    }

    /**
     * Objetivo: Verificar se o peão branco pode realizar seus dois movimentos iniciais (1 ou 2 casas).
     * Retorno: lista de movimentos inclui (5,4) e (4,4)
     * Significado: o peão branco está livre para realizar seus primeiros movimentos válidos.
     */
    @Test(timeout = 2000)
    public void test_whitePawnInitialMovesAreAvailable() {
        List<int[]> moves = whitePawn.pieceMovement(board);
        assertTrue(containsMove(moves, 5, 4));
        assertTrue(containsMove(moves, 4, 4));
    }

    /**
     * Objetivo: Verificar que o peão não pode mover para trás.
     * Retorno: a lista de movimentos não inclui a casa atrás do peão.
     * Significado: o peão respeita a regra de não retroceder.
     */
    @Test(timeout = 2000)
    public void test_pawnCannotMoveBackward() {
        List<int[]> moves = whitePawn.pieceMovement(board);
        assertFalse(containsMove(moves, 7, 4)); // movimento inválido para trás
    }

    
    /**
     * Objetivo: Verificar se o peão preto pode realizar seus dois movimentos iniciais.
     * Retorno: lista inclui (2,3) e (3,3)
     * Significado: o peão preto tem caminho livre e está na linha inicial.
     */
    @Test(timeout = 2000)
    public void test_blackPawnInitialMovesAreAvailable() {
        List<int[]> moves = blackPawn.pieceMovement(board);
        assertTrue(containsMove(moves, 2, 3));
        assertTrue(containsMove(moves, 3, 3));
    }

    /**
     * Objetivo: Verificar se o peão é impedido de avançar quando há uma peça bloqueando seu caminho.
     * Retorno: lista de movimentos não contém as casas à frente
     * Significado: o sistema bloqueia corretamente o avanço em caso de obstrução.
     */
    @Test(timeout = 2000)
    public void test_pawnCannotMoveThroughOtherPieces() {
        Pawn blocker = new Pawn('W', 5, 4);
        board.setPiece(5, 4, blocker);

        List<int[]> moves = whitePawn.pieceMovement(board);
        assertFalse(containsMove(moves, 5, 4));
        assertFalse(containsMove(moves, 4, 4));
    }

    /**
     * Objetivo: Verificar se o peão pode capturar peças inimigas nas diagonais.
     * Retorno: lista de movimentos inclui (5,3) e (5,5)
     * Significado: o sistema permite capturas diagonais válidas.
     */
    @Test(timeout = 2000)
    public void test_pawnCanCaptureDiagonally() {
        Pawn enemyLeft = new Pawn('B', 5, 3);
        Pawn enemyRight = new Pawn('B', 5, 5);

        board.setPiece(5, 3, enemyLeft);
        board.setPiece(5, 5, enemyRight);

        List<int[]> moves = whitePawn.pieceMovement(board);
        assertTrue(containsMove(moves, 5, 3));
        assertTrue(containsMove(moves, 5, 5));
    }

    /**
     * Objetivo: Verificar se o peão não pode capturar o rei inimigo diretamente.
     * Retorno: lista de movimentos não contém a casa do rei inimigo
     * Significado: o sistema proíbe a captura direta do rei, como exigido pelas regras do xadrez.
     */
    @Test(timeout = 2000)
    public void test_pawnCannotCaptureEnemyKing() {
        King enemyKing = new King('B', 5, 3);
        board.setPiece(5, 3, enemyKing);

        List<int[]> moves = whitePawn.pieceMovement(board);
        assertFalse(containsMove(moves, 5, 3));
    }

    /**
     * Objetivo: Verificar se o peão é impedido de realizar um movimento que deixaria seu rei em xeque.
     * Retorno: lista de movimentos não contém (5,4)
     * Significado: o sistema impede movimentos que violam a segurança do rei.
     */
    @Test(timeout = 2000)
    public void test_pawnCannotMoveIntoCheck() {
        Rook blackRook = new Rook('B', 3, 4);
        board.setPiece(3, 4, blackRook);

        List<int[]> moves = whitePawn.pieceMovement(board);
        assertFalse(containsMove(moves, 5, 4));
    }

    // Função auxiliar para verificar se (row, col) está na lista de movimentos
    private boolean containsMove(List<int[]> moves, int row, int col) {
        for (int[] move : moves) {
            if (move[0] == row && move[1] == col) return true;
        }
        return false;
    }
}
