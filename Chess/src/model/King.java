package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Rei – move-se uma casa em qualquer direção e
 * nunca pode ocupar (nem atravessar) casas atacadas
 */
public class King extends Piece {

    public King(char color, int row, int col) {
        super(color, row, col);
    }

    @Override
    public char getTypeChar() { return 'K'; }

    /* Casas que o Rei pode realmente jogar */
    @Override
    public List<int[]> pieceMovement(Board board) {
        List<int[]> moves = new ArrayList<>();

        /* deslocamentos possíveis ao redor do Rei */
        int[] dRow = {-1,-1,-1, 0, 0, 1, 1, 1};
        int[] dCol = {-1, 0, 1,-1, 1,-1, 0, 1};

        for (int i = 0; i < dRow.length; i++) {
            int newRow = row + dRow[i];
            int newCol = col + dCol[i];

            if (!board.isValidPosition(newRow, newCol)) continue;

            Piece target = board.getPiece(newRow, newCol);

            if (target instanceof King) continue;

            boolean casaLivreOuInimiga =
                    target == null || target.getColor() != color;

            /* Casa também não pode estar atacada */
            if (casaLivreOuInimiga &&
                !isSquareUnderAttack(board, newRow, newCol)) {

                moves.add(new int[]{ newRow, newCol });
            }
        }
        return moves;
    }

    /* true se a casa (toRow,toCol) estiver sob ataque inimigo */
    private boolean isSquareUnderAttack(Board board, int toRow, int toCol) {
        char enemy = (color == 'W') ? 'B' : 'W';

        /* 1. Cavalo inimigo */
        int[][] knightJump = {
            {-2,-1},{-2, 1},{-1,-2},{-1, 2},
            { 1,-2},{ 1, 2},{ 2,-1},{ 2, 1}};
        for (int[] d : knightJump) {
            Piece p = board.getPiece(toRow + d[0], toCol + d[1]);
            if (p instanceof Knight && p.getColor() == enemy) return true;
        }

        /* 2. Peões inimigos (capturam na diagonal) */
        int dir   = (enemy == 'W') ? -1 : 1;      // direção de CAPTURA
        int[] dc  = {-1, 1};
        for (int d : dc) {
            Piece p = board.getPiece(toRow - dir, toCol + d);
            if (p instanceof Pawn && p.getColor() == enemy) return true;
        }

        /* 3. Raios ortogonais (torre ou dama inimigas) */
        int[][] straight = {{ 1,0},{-1,0},{0, 1},{0,-1}};
        if (rayHits(board, toRow, toCol, straight, enemy, true)) return true;

        /* 4. Raios diagonais (bispo ou dama inimigas) */
        int[][] diag = {{ 1, 1},{ 1,-1},{-1, 1},{-1,-1}};
        if (rayHits(board, toRow, toCol, diag, enemy, false)) return true;

        /* 5. Rei inimigo em casa adjacente */
        for (int dr = -1; dr <= 1; dr++)
            for (int dc2 = -1; dc2 <= 1; dc2++) {
                if (dr == 0 && dc2 == 0) continue;
                Piece p = board.getPiece(toRow + dr, toCol + dc2);
                if (p instanceof King && p.getColor() == enemy) return true;
            }

        return false;
    }

    /*
     * Percorre cada direção até encontrar a primeira peça.
     * Se essa peça for inimiga e compatível (torre/dama ou bispo/dama),
     * então a casa está atacada.
     */
    private boolean rayHits(Board board, int r, int c, int[][] dirs, char enemyColor, boolean rookLike) {
        for (int[] d : dirs) {
            int nr = r + d[0], nc = c + d[1];
            while (board.isValidPosition(nr, nc)) {
                Piece p = board.getPiece(nr, nc);
                if (p != null) {
                    if (nr == this.row && nc == this.col && p instanceof King && p.getColor() == this.color) {
                        nr += d[0];
                        nc += d[1];
                        continue;
                    }
                    if (p.getColor() == enemyColor) {
                        if (rookLike) {
                            if (p instanceof Rook || p instanceof Queen)
                                return true;
                        } else {
                            if (p instanceof Bishop || p instanceof Queen)
                                return true;
                        }
                    }
                    break;
                }
                nr += d[0];
                nc += d[1];
            }
        }
        return false;
    }

    /* canMove delega à lista já filtrada por pieceMovement */
    @Override
    public boolean canMove(int fr,int fc,int tr,int tc,Board board) {
        for (int[] mv : pieceMovement(board))
            if (mv[0]==tr && mv[1]==tc) return true;
        return false;
    }
}