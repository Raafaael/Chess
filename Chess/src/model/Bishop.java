package model;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {

    public Bishop(char color, int row, int col) {
        super(color, row, col);
    }

    @Override
    public char getTypeChar() {
        return 'B';
    }

    @Override
    public List<int[]> pieceMovement(Board board) {

        List<int[]> rawMoves = new ArrayList<>();
        int[][] dir = { { -1, -1 }, { -1, 1 }, { 1, -1 }, { 1, 1 } };

        for (int[] d : dir) {
            int r = getRow() + d[0];
            int c = getCol() + d[1];

            while (board.isValidPosition(r, c)) {
                if (board.isEmpty(r, c)) {
                    rawMoves.add(new int[]{ r, c });
                } else {
                    if (board.hasEnemyPiece(r, c, color)) {
                        rawMoves.add(new int[]{ r, c });
                    }
                    break;                      // bloqueado
                }
                r += d[0];
                c += d[1];
            }
        }

        List<int[]> safeMoves = new ArrayList<>();

        for (int[] mv : rawMoves) {
            int toRow = mv[0];
            int toCol = mv[1];

            Piece target = board.getPiece(toRow, toCol);
            if (target instanceof King) {
                continue;
            }

            int     fromRow = getRow();
            int     fromCol = getCol();
            boolean moved   = hasMoved;

            board.makeMove(fromRow, fromCol, toRow, toCol);
            boolean inCheck = board.isInCheck(color);
            board.undoMove(fromRow, fromCol, toRow, toCol, target);
            setHasMoved(moved);

            if (!inCheck) {
                safeMoves.add(mv);
            }
        }
        return safeMoves;
    }

    @Override
    public boolean canMove(int fromRow, int fromCol, int toRow, int toCol,
                           Board board) {

        if (Math.abs(toRow - fromRow) != Math.abs(toCol - fromCol)) {
            return false;
        }

        int dr = Integer.compare(toRow, fromRow);
        int dc = Integer.compare(toCol, fromCol);
        int r  = fromRow + dr;
        int c  = fromCol + dc;
        while (r != toRow || c != toCol) {
            if (!board.isEmpty(r, c)) {
                return false;
            }
            r += dr;
            c += dc;
        }

        Piece target = board.getPiece(toRow, toCol);
        if (target instanceof King) {
            return false;
        }
        if (target != null && target.getColor() == color) {
            return false;
        }

        int     savedRow = getRow();
        int     savedCol = getCol();
        boolean moved    = hasMoved;

        board.makeMove(savedRow, savedCol, toRow, toCol);
        boolean inCheck = board.isInCheck(color);
        board.undoMove(savedRow, savedCol, toRow, toCol, target);
        setHasMoved(moved);

        return !inCheck;
    }
}
