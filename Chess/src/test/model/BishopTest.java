package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

public class BishopTest {

    private Board board;
    private Bishop bishop;

    @Before
    public void setUp() {
        board = Board.getInstance();

        // Limpa o tabuleiro
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                board.setPiece(r, c, null);
            }
        }

        // Rei necessário para testMoveSafety
        King whiteKing = new King('W', 7, 4);
        board.setPiece(7, 4, whiteKing);

        bishop = new Bishop('W', 4, 4);
        board.setPiece(4, 4, bishop);
    }

    /**
     * Objetivo: Verificar se o bispo pode se mover para todas as diagonais livres.
     * Retorno: a lista de movimentos contém casas nas quatro diagonais.
     * Significado: o bispo está obedecendo corretamente sua regra de movimentação.
     */
    @Test(timeout = 2000)
    public void test_bishopMovesDiagonallyWhenUnblocked() {
        List<int[]> moves = bishop.pieceMovement(board);

        assertTrue(containsMove(moves, 3, 3));
        assertTrue(containsMove(moves, 3, 5));
        assertTrue(containsMove(moves, 5, 3));
        assertTrue(containsMove(moves, 5, 5));
    }

    /**
     * Objetivo: Verificar que o bispo não pode se mover em linha reta.
     * Retorno: a lista de movimentos não inclui casas adjacentes retas.
     * Significado: o bispo respeita seu movimento diagonal exclusivo.
     */
    @Test(timeout = 2000)
    public void test_bishopCannotMoveInvalidly() {
        List<int[]> moves = bishop.pieceMovement(board);
        assertFalse(containsMove(moves, 4, 5)); // movimento reto para a direita
        assertFalse(containsMove(moves, 5, 4)); // movimento reto para baixo
    }

    
    /**
     * Objetivo: Verificar se o bispo não ultrapassa peças da mesma cor.
     * Retorno: a casa ocupada pela peça aliada não está nos movimentos, nem as que vêm depois.
     * Significado: o sistema bloqueia corretamente a movimentação ao encontrar aliados.
     */
    @Test(timeout = 2000)
    public void test_bishopStopsAtOwnPiece() {
        Pawn whitePawn = new Pawn('W', 3, 3);
        board.setPiece(3, 3, whitePawn);

        List<int[]> moves = bishop.pieceMovement(board);

        assertFalse(containsMove(moves, 3, 3));
        assertFalse(containsMove(moves, 2, 2));
    }

    /**
     * Objetivo: Verificar se o bispo pode capturar uma peça inimiga.
     * Retorno: a casa do inimigo está presente, mas as posteriores não.
     * Significado: o bispo pode capturar, mas não atravessa.
     */
    @Test(timeout = 2000)
    public void test_bishopCanCaptureEnemyButNotGoPast() {
        Pawn blackPawn = new Pawn('B', 3, 3);
        board.setPiece(3, 3, blackPawn);

        List<int[]> moves = bishop.pieceMovement(board);

        assertTrue(containsMove(moves, 3, 3));
        assertFalse(containsMove(moves, 2, 2));
    }

    /**
     * Objetivo: Verificar se o bispo é impedido de capturar diretamente o rei inimigo.
     * Retorno: o movimento até a casa do rei não está presente.
     * Significado: o sistema respeita a regra que impede a captura direta do rei.
     */
    @Test(timeout = 2000)
    public void test_bishopCannotCaptureEnemyKing() {
        King blackKing = new King('B', 3, 3);
        board.setPiece(3, 3, blackKing);

        List<int[]> moves = bishop.pieceMovement(board);

        assertFalse(containsMove(moves, 3, 3));
    }

    /**
     * Objetivo: Verificar se o bispo é impedido de se mover ao revelar um xeque descoberto.
     * Retorno: o movimento que expõe o rei (ex: para 4,3) não aparece na lista.
     * Significado: o bispo está sendo corretamente impedido de expor o rei.
     */
    @Test(timeout = 2000)
    public void test_bishopCannotExposeKingToDiscoveredCheck() {
        // Rei branco na mesma coluna
        King whiteKing = new King('W', 5, 1);
        board.setPiece(5, 1, whiteKing);

        // Torre preta alinhada com o rei, mas bloqueada
        Rook blackRook = new Rook('B', 5, 6);
        board.setPiece(5, 6, blackRook);

        // Bispo branco bloqueando o xeque
        bishop = new Bishop('W', 5, 4);
        board.setPiece(5, 4, bishop);

        // Movimento qualquer do bispo que libere a coluna (como ir para 4,3)
        List<int[]> moves = bishop.pieceMovement(board);

        assertFalse("Bishop must not move if it exposes king to discovered check", containsMove(moves, 4, 3));
    }


    // Auxiliar para verificar se um movimento existe na lista
    private boolean containsMove(List<int[]> moves, int row, int col) {
        for (int[] move : moves) {
            if (move[0] == row && move[1] == col) return true;
        }
        return false;
    }
}
