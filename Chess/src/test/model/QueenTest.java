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
        King blackKing = new King('B', 0, 5);
        board.setPiece(7, 4, whiteKing);
        board.setPiece(0, 4, blackKing);

        whiteQueen = new Queen('W', 4, 4);
        board.setPiece(4, 4, whiteQueen);
    }

    /**
     * Objetivo: Verificar se a rainha pode se mover livremente em todas as direções.
     * Retorno: a lista de movimentos contém casas nas direções vertical, horizontal e diagonais.
     * Significado: a rainha obedece suas regras de movimentação e pode percorrer o tabuleiro corretamente.
     */
    @Test(timeout = 2000)
    public void test_queenCanMoveInAllDirections() {
        List<int[]> moves = whiteQueen.pieceMovement(board);

        // Horizontais
        assertTrue("Movimento horizontal para a esquerda deve estar disponível", containsMove(moves, 4, 0));
        assertTrue("Movimento horizontal para a direita deve estar disponível", containsMove(moves, 4, 7));

        // Verticais (considerando os reis nas extremidades)
        assertTrue("Movimento vertical para cima deve estar disponível até antes do rei preto", containsMove(moves, 1, 4));
        assertTrue("Movimento vertical para baixo deve estar disponível até antes do rei branco", containsMove(moves, 6, 4));

        // Diagonais
        assertTrue("Movimento diagonal sudeste deve estar disponível até antes do rei branco", containsMove(moves, 6, 6));
        assertTrue("Movimento diagonal noroeste deve estar disponível até o canto", containsMove(moves, 0, 0));
        assertTrue("Movimento diagonal sudoeste deve estar disponível até o canto", containsMove(moves, 7, 1));
        assertTrue("Movimento diagonal nordeste deve estar disponível até o canto", containsMove(moves, 1, 7));
    }

    /**
     * Objetivo: Verificar que a rainha não pode atravessar outras peças.
     * Retorno: a lista de movimentos não inclui casas após uma peça aliada.
     * Significado: a rainha respeita bloqueios e para ao encontrar obstáculos.
     */
    @Test(timeout = 2000)
    public void test_queenCannotMoveThroughOtherPieces() {
        Pawn blocker = new Pawn('W', 4, 6);  // Aliado à direita
        board.setPiece(4, 6, blocker);

        List<int[]> moves = whiteQueen.pieceMovement(board);

        assertTrue("Rainha deve poder mover até antes da peça aliada", containsMove(moves, 4, 5));
        assertFalse("Rainha não deve poder capturar uma peça aliada", containsMove(moves, 4, 6));
        assertFalse("Rainha não deve atravessar uma peça aliada", containsMove(moves, 4, 7));
    }

    /**
     * Objetivo: Verificar se a rainha pode capturar uma peça inimiga.
     * Retorno: a casa com a peça inimiga aparece na lista de movimentos.
     * Significado: a rainha pode capturar corretamente inimigos em sua trajetória.
     */
    @Test(timeout = 2000)
    public void test_queenCanCaptureEnemy() {
        // Posiciona a rainha branca em (5,5) — você pode ajustar se necessário
        whiteQueen = new Queen('W', 5, 5);
        board.setPiece(5, 5, whiteQueen);

        // Peça inimiga na diagonal sudeste
        Pawn enemy = new Pawn('B', 6, 6);
        board.setPiece(6, 6, enemy);

        // Casa após o inimigo (não deve estar disponível)
        board.setPiece(7, 7, null);  // só para garantir

        List<int[]> moves = whiteQueen.pieceMovement(board);

        // A rainha deve poder capturar o inimigo
        assertTrue("Rainha deve poder capturar peça inimiga em (6,6)", containsMove(moves, 6, 6));

    }

    /**
     * Objetivo: Verificar se a rainha é impedida de se mover para uma casa que deixaria seu rei em xeque.
     * Retorno: a lista de movimentos não inclui casas que resultariam em xeque ao rei aliado.
     * Significado: o sistema impede movimentos que colocariam o próprio rei em risco.
     */
    @Test(timeout = 2000)
    public void test_queenCannotMoveIntoCheck() {
        Rook blackRook = new Rook('B', 0, 4); // Alvo alinhado com rei branco
        board.setPiece(0, 4, blackRook);

        List<int[]> moves = whiteQueen.pieceMovement(board);

        assertFalse("Rainha não deve se mover se isso deixar o rei em xeque", containsMove(moves, 3, 3));
    }

    /**
     * Objetivo: Verificar se a rainha é impedida de se mover ao revelar um xeque descoberto.
     * Retorno: o movimento que expõe o rei (ex: para 5,3) não aparece na lista.
     * Significado: a rainha está sendo corretamente impedida de expor o rei ao xeque indireto.
     */
    @Test(timeout = 2000)
    public void test_queenCannotExposeKingToDiscoveredCheck() {
        // Novo cenário
        King whiteKing = new King('W', 5, 1);
        board.setPiece(5, 1, whiteKing);

        Rook blackRook = new Rook('B', 5, 6);
        board.setPiece(5, 6, blackRook);

        whiteQueen = new Queen('W', 5, 4);
        board.setPiece(5, 4, whiteQueen);

        List<int[]> moves = whiteQueen.pieceMovement(board);

        assertFalse("Rainha não deve se mover se isso expuser o rei ao xeque descoberto", containsMove(moves, 5, 3));
    }

    // Função auxiliar
    private boolean containsMove(List<int[]> moves, int row, int col) {
        for (int[] move : moves) {
            if (move[0] == row && move[1] == col) return true;
        }
        return false;
    }
}