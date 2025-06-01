package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

public class QueenTest {

    private Board board;
    private Queen whiteQueen;

    @Before
    public void setUp() {
        board = Board.getInstance();

        for (int r = 0; r < 8; r++)
            for (int c = 0; c < 8; c++)
                board.setPiece(r, c, null);

        board.setPiece(7, 4, new King('W', 7, 4));
        board.setPiece(0, 4, new King('B', 0, 4));

        whiteQueen = new Queen('W', 4, 4);
        board.setPiece(4, 4, whiteQueen);
    }
    
    /**
     * Objetivo: Verificar se a rainha pode se mover livremente em todas as direções.
     * Retorno: lista contém casas verticais, horizontais e diagonais válidas.
     * Significado: a rainha obedece suas regras de movimentação.
     */
    @Test(timeout = 2000)
    public void test_queenCanMoveInAllDirections() {
        List<int[]> moves = whiteQueen.pieceMovement(board);

        assertTrue(containsMove(moves, 4, 0));
        assertTrue(containsMove(moves, 4, 7));
        assertTrue(containsMove(moves, 1, 4));
        assertTrue(containsMove(moves, 6, 4));
        assertTrue(containsMove(moves, 6, 6));
        assertTrue(containsMove(moves, 0, 0));
        assertTrue(containsMove(moves, 7, 1));
        assertTrue(containsMove(moves, 1, 7));
    }

    /**
     * Objetivo: Verificar que a rainha não atravessa peças aliadas.
     * Retorno: lista não inclui casas após uma peça aliada.
     * Significado: a rainha respeita bloqueios em seu caminho.
     */
    @Test(timeout = 2000)
    public void test_queenCannotMoveThroughOtherPieces() {
        board.setPiece(4, 6, new Pawn('W', 4, 6));

        List<int[]> moves = whiteQueen.pieceMovement(board);

        assertTrue (containsMove(moves, 4, 5));
        assertFalse(containsMove(moves, 4, 6));
        assertFalse(containsMove(moves, 4, 7));
    }

    /**
     * Objetivo: Verificar se a rainha pode capturar uma peça inimiga.
     * Retorno: a casa inimiga aparece na lista de movimentos.
     * Significado: a rainha captura corretamente inimigos em sua trajetória.
     */
    @Test(timeout = 2000)
    public void test_queenCanCaptureEnemy() {
        whiteQueen = new Queen('W', 5, 5);
        board.setPiece(5, 5, whiteQueen);

        board.setPiece(6, 6, new Pawn('B', 6, 6));

        List<int[]> moves = whiteQueen.pieceMovement(board);
        assertTrue(containsMove(moves, 6, 6));
    }

    /**
     * Objetivo: Verificar se a rainha é impedida de se mover para casa que deixaria seu rei em xeque.
     * Retorno: lista não inclui movimentação ilegal.
     * Significado: o sistema impede movimentos que colocam o próprio rei em risco.
     */
    @Test(timeout = 2000)
    public void test_queenCannotMoveIntoCheck() {
        board.setPiece(0, 4, new Rook('B', 0, 4));

        List<int[]> moves = whiteQueen.pieceMovement(board);
        assertFalse(containsMove(moves, 3, 3));
    }

    /**
     * Objetivo: Verificar se a rainha é impedida de se mover ao revelar um xeque descoberto.
     * Retorno: o movimento que expõe o rei (para 2,3) não aparece na lista.
     * Significado: a rainha está sendo corretamente impedida de expor o rei.
     */
    @Test(timeout = 2000)
    public void test_queenCannotExposeKingToDiscoveredCheck() {
        board.setPiece(2, 1, new King('W', 2, 1));   // rei branco
        board.setPiece(2, 6, new Rook('B', 2, 6));   // torre preta

        whiteQueen = new Queen('W', 2, 4);
        board.setPiece(2, 4, whiteQueen);

        List<int[]> moves = whiteQueen.pieceMovement(board);
        assertTrue(containsMove(moves, 2, 3));
    }

    /* auxiliar */
    private boolean containsMove(List<int[]> moves, int r, int c) {
        for (int[] mv : moves)
            if (mv[0] == r && mv[1] == c) return true;
        return false;
    }
}
