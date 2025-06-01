package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

public class KnightTest {

    private Board board;
    private Knight whiteKnight;

    @Before
    public void setUp() {
        board = Board.getInstance();

        // Limpa o tabuleiro
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board.setPiece(i, j, null);
            }
        }

        whiteKnight = new Knight('W', 3, 3); // d4
        board.setPiece(3, 3, whiteKnight);
    }

    /**
     * Objetivo: Verificar se o cavalo pode se mover para as 8 casas em "L" disponíveis.
     * Retorno: a lista de movimentos contém exatamente as 8 casas padrão do cavalo.
     * Significado: o cavalo obedece à sua movimentação característica.
     */
    @Test(timeout = 2000)
    public void test_knightHasEightValidMovesFromCenter() {
        List<int[]> moves = whiteKnight.pieceMovement(board);

        int[][] expected = {
            {1, 2}, {1, 4}, {2, 1}, {2, 5},
            {4, 1}, {4, 5}, {5, 2}, {5, 4}
        };

        assertEquals("Cavaleiro deve ter 8 movimentos do centro", 8, moves.size());

        for (int[] pos : expected) {
            assertTrue(containsMove(moves, pos[0], pos[1]));
        }
    }
    
    /**
     * Objetivo: Verificar que o cavalo não pode se mover de forma diferente do padrão “L”.
     * Retorno: a lista de movimentos não inclui movimentos retos ou diagonais simples.
     * Significado: o cavalo está limitado ao movimento em “L” característico.
     */
    @Test(timeout = 2000)
    public void test_knightCannotMoveInvalidly() {
        List<int[]> moves = whiteKnight.pieceMovement(board);
        assertFalse(containsMove(moves, 2, 3)); // movimento reto para cima (inválido)
        assertFalse(containsMove(moves, 3, 4)); // movimento reto para a direita (inválido)
    }


    /**
     * Objetivo: Verificar que o cavalo não pode capturar uma peça da mesma cor.
     * Retorno: a casa ocupada por aliado não aparece na lista de movimentos.
     * Significado: o sistema bloqueia corretamente capturas inválidas.
     */
    @Test(timeout = 2000)
    public void test_knightCannotCaptureOwnPiece() {
        Pawn whitePawn = new Pawn('W', 2, 5);
        board.setPiece(2, 5, whitePawn);

        List<int[]> moves = whiteKnight.pieceMovement(board);

        assertFalse("Cavaleiro não pode capturar uma peça aliada", containsMove(moves, 2, 5));
    }

    /**
     * Objetivo: Verificar se o cavalo pode capturar uma peça inimiga.
     * Retorno: a casa do inimigo está presente na lista de movimentos.
     * Significado: o cavalo consegue capturar corretamente peças adversárias.
     */
    @Test(timeout = 2000)
    public void test_knightCanCaptureEnemyPiece() {
        Pawn blackPawn = new Pawn('B', 2, 5);
        board.setPiece(2, 5, blackPawn);

        List<int[]> moves = whiteKnight.pieceMovement(board);

        assertTrue("Cavaleiro consegue capturar uma peça inimiga", containsMove(moves, 2, 5));
    }
    
    /**
     * Objetivo: Verificar se o cavalo é impedido de mover para casa que deixaria o rei em xeque descoberto.
     * Retorno: movimentos que expõem o rei não aparecem na lista.
     * Significado: o cavalo não pode fazer movimentos que desprotejam o rei.
     */
    @Test(timeout = 2000)
    public void test_knightCannotExposeKingToDiscoveredCheck() {
        King whiteKing = new King('W', 5, 1);
        board.setPiece(5, 1, whiteKing);

        // Reposicionar o cavalo
        board.setPiece(3, 3, null); // Remover cavalo da posição original
        whiteKnight = new Knight('W', 5, 4);
        board.setPiece(5, 4, whiteKnight);

        // Torre preta ameaçando na mesma linha, bloqueada pelo knight
        Rook blackRook = new Rook('B', 5, 7);
        board.setPiece(5, 7, blackRook);

        List<int[]> moves = whiteKnight.pieceMovement(board);

        assertFalse("Cavaleiro não pode se mover caso coloque o seu Rei em cheque", containsMove(moves, 3, 5));
    }

    // Auxiliar para verificar se um movimento está na lista
    private boolean containsMove(List<int[]> moves, int row, int col) {
        for (int[] move : moves) {
            if (move[0] == row && move[1] == col) return true;
        }
        return false;
    }
}
