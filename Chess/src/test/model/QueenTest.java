package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

public class QueenTest {

    private Board board;
    private Queen whiteQueen;
    private Queen blackQueen;

    @Before
    public void setUp() {
        board = Board.getInstance();

        // Limpa o tabuleiro
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                board.setPiece(r, c, null);
            }
        }

        // Posiciona os reis para testMoveSafety
        King whiteKing = new King('W', 7, 4);
        King blackKing = new King('B', 0, 4);
        board.setPiece(7, 4, whiteKing);
        board.setPiece(0, 4, blackKing);

        whiteQueen = new Queen('W', 4, 4);
        blackQueen = new Queen('B', 3, 3);

        board.setPiece(4, 4, whiteQueen);
        board.setPiece(3, 3, blackQueen);
    }

    /**
     * Objetivo: Verificar se a rainha pode se mover livremente em todas as direções.
     */
    @Test(timeout = 2000)
    public void test_queenCanMoveInAllDirections() {
        List<int[]> moves = whiteQueen.pieceMovement(board);

        assertTrue(containsMove(moves, 4, 0)); // horizontal esquerda
        assertTrue(containsMove(moves, 4, 7)); // horizontal direita
        assertTrue(containsMove(moves, 0, 4)); // vertical cima
        assertTrue(containsMove(moves, 7, 4)); // vertical baixo
        assertTrue(containsMove(moves, 7, 7)); // diagonal SE
        assertTrue(containsMove(moves, 1, 1)); // diagonal NO
    }

    /**
     * Objetivo: Verificar que a rainha não pode atravessar outras peças.
     */
    @Test(timeout = 2000)
    public void test_queenCannotMoveThroughOtherPieces() {
        Pawn blocker = new Pawn('W', 4, 6);
        board.setPiece(4, 6, blocker);

        List<int[]> moves = whiteQueen.pieceMovement(board);
        assertFalse(containsMove(moves, 4, 7)); // atrás do peão
        assertTrue(containsMove(moves, 4, 5));  // antes do peão
    }

    /**
     * Objetivo: Verificar se a rainha pode capturar uma peça inimiga.
     */
    @Test(timeout = 2000)
    public void test_queenCanCaptureEnemy() {
        Pawn enemy = new Pawn('B', 6, 6);
        board.setPiece(6, 6, enemy);

        List<int[]> moves = whiteQueen.pieceMovement(board);
        assertTrue(containsMove(moves, 6, 6));
    }

    /**
     * Objetivo: Verificar se a rainha não pode capturar o rei inimigo.
     */
    @Test(timeout = 2000)
    public void test_queenCannotCaptureEnemyKing() {
        King enemyKing = new King('B', 6, 6);
        board.setPiece(6, 6, enemyKing);

        List<int[]> moves = whiteQueen.pieceMovement(board);
        assertFalse(containsMove(moves, 6, 6));
    }

    /**
     * Objetivo: Verificar se a rainha não pode se mover para uma casa que deixaria o rei em xeque.
     */
    @Test(timeout = 2000)
    public void test_queenCannotMoveIntoCheck() {
        Rook blackRook = new Rook('B', 0, 4);
        board.setPiece(0, 4, blackRook);

        // Se a rainha sair da frente do rei, o rei ficará em xeque
        List<int[]> moves = whiteQueen.pieceMovement(board);
        assertFalse(containsMove(moves, 3, 4));
    }

    /**
     * Objetivo: Verificar se a rainha é impedida de se mover ao revelar um xeque descoberto.
     * Retorno: o movimento que expõe o rei (ex: para 4,3) não aparece na lista.
     * Significado: a rainha está sendo corretamente impedida de expor o rei.
     */
    @Test(timeout = 2000)
    public void test_queenCannotExposeKingToDiscoveredCheck() {
        // Rei branco posicionado atrás da rainha
        King whiteKing = new King('W', 5, 1);
        board.setPiece(5, 1, whiteKing);

        // Torre preta alinhada, pronta para dar xeque se a rainha sair
        Rook blackRook = new Rook('B', 5, 6);
        board.setPiece(5, 6, blackRook);

        // Rainha branca bloqueando o ataque da torre
        whiteQueen = new Queen('W', 5, 4);
        board.setPiece(5, 4, whiteQueen);

        // Movimento qualquer da rainha que libere a linha (como ir para 4,3)
        List<int[]> moves = whiteQueen.pieceMovement(board);

        assertFalse("Queen must not move if it exposes king to discovered check", containsMove(moves, 4, 3));
    }

    // Função auxiliar
    private boolean containsMove(List<int[]> moves, int row, int col) {
        for (int[] move : moves) {
            if (move[0] == row && move[1] == col) return true;
        }
        return false;
    }
}
