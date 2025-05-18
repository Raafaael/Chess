package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

public class RookTest {

    private Board board;
    private Rook rook;

    @Before
    public void setUp() {
        board = Board.getInstance();

        // Limpa o tabuleiro
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                board.setPiece(r, c, null);
            }
        }

        rook = new Rook('W', 5, 4);
        board.setPiece(5, 4, rook);
    }

    /**
     * Objetivo: Verificar se o rook pode se mover corretamente em linhas e colunas desobstruídas.
     * Retorno: lista de movimentos inclui casas horizontais e verticais válidas.
     * Significado: o rook está obedecendo à regra básica de movimentação.
     */
    @Test(timeout = 2000)
    public void test_rookMovesAlongRowsAndColumns() {
        List<int[]> moves = rook.pieceMovement(board);

        assertTrue(containsMove(moves, 4, 4));
        assertTrue(containsMove(moves, 6, 4));
        assertTrue(containsMove(moves, 5, 3));
        assertTrue(containsMove(moves, 5, 5));
    }

    /**
     * Objetivo: Verificar se o rook não pode capturar peça da mesma cor.
     * Retorno: casa ocupada por aliado não está na lista.
     * Significado: o rook respeita as restrições de captura.
     */
    @Test(timeout = 2000)
    public void test_rookCannotCaptureOwnPiece() {
        Pawn whitePawn = new Pawn('W', 5, 3);
        board.setPiece(5, 3, whitePawn);

        List<int[]> moves = rook.pieceMovement(board);

        assertFalse(containsMove(moves, 5, 3));
    }

    /**
     * Objetivo: Verificar se o rook pode capturar peça inimiga.
     * Retorno: casa ocupada pelo inimigo está na lista.
     * Significado: o rook pode capturar adversários corretamente.
     */
    @Test(timeout = 2000)
    public void test_rookCanCaptureEnemyPiece() {
        Pawn blackPawn = new Pawn('B', 5, 3);
        board.setPiece(5, 3, blackPawn);

        List<int[]> moves = rook.pieceMovement(board);

        assertTrue(containsMove(moves, 5, 3));
    }

    /**
     * Objetivo: Verificar se o rook é impedido de mover para casa que deixaria o rei em xeque descoberto.
     * Retorno: movimentos que expõem o rei não aparecem na lista.
     * Significado: o rook não pode fazer movimentos que desprotejam o rei.
     */
    @Test(timeout = 2000)
    public void test_rookCannotExposeKingToDiscoveredCheck() {
        King whiteKing = new King('W', 5, 1); // Rei na mesma linha, fora do caminho
        board.setPiece(5, 1, whiteKing);

        // Torre preta ameaçando na mesma linha, bloqueada pelo rook
        Rook blackRook = new Rook('B', 5, 7);
        board.setPiece(5, 7, blackRook);

        List<int[]> moves = rook.pieceMovement(board);

        assertFalse("Rook must not move if it exposes king to discovered check", containsMove(moves, 4, 4));
    }

    /**
     * Objetivo: Verificar se o rook não pode realizar movimentos inválidos (fora das linhas e colunas).
     * Retorno: a lista de movimentos não inclui movimentos diagonais ou fora do tabuleiro.
     * Significado: o rook está limitado à movimentação correta.
     */
    @Test(timeout = 2000)
    public void test_rookCannotMoveInvalidly() {
        List<int[]> moves = rook.pieceMovement(board);

        assertFalse("Rook cannot move diagonally", containsMove(moves, 6, 5));
        assertFalse("Rook cannot move diagonally", containsMove(moves, 4, 5));
        assertFalse("Rook cannot move outside board", containsMove(moves, 8, 4));
        assertFalse("Rook cannot move outside board", containsMove(moves, -1, 4));
    }

    // Auxiliar para verificar se um movimento existe na lista
    private boolean containsMove(List<int[]> moves, int row, int col) {
        for (int[] move : moves) {
            if (move[0] == row && move[1] == col) return true;
        }
        return false;
    }
}