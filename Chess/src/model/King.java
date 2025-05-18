package model;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {

    public King(char color, int row, int col) {
        super(color, row, col);
    }

    @Override
    public List<int[]> pieceMovement(Board board) {
        List<int[]> moves = new ArrayList<>();

        int[] rowOffsets = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] colOffsets = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < rowOffsets.length; i++) {
            int newRow = row + rowOffsets[i];
            int newCol = col + colOffsets[i];

            if (board.isValidPosition(newRow, newCol)) {
                Piece p = board.getPiece(newRow, newCol);
                if (p == null || p.getColor() != this.color) {
                    // Check if the move would put the king in check
                    if (!testMoveSafety(board, newRow, newCol)) {
                        moves.add(new int[] {newRow, newCol});
                    }
                }
            }
        }

        return moves;
    }

    @Override
    public boolean canMove(int fromRow, int fromCol, int toRow, int toCol, Board board) {
        List<int[]> validMoves = pieceMovement(board);
        for (int[] move : validMoves) {
            if (move[0] == toRow && move[1] == toCol) {
                return true;
            }
        }
        return false;
    }

    // Checks if a square is under attack by enemy pieces
    public boolean testMoveSafety(Board board, int toRow, int toCol) {
        char enemyColor = (this.color == 'W') ? 'B' : 'W';

        // Directions for straight lines (rook/queen)
        int[][] straightDirections = {
            {1, 0}, {-1, 0}, {0, 1}, {0, -1}
        };

        // Directions for diagonals (bishop/queen)
        int[][] diagonalDirections = {
            {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
        };

        // Check straight lines (rook + queen)
        for (int[] dir : straightDirections) {
            int r = toRow + dir[0];
            int c = toCol + dir[1];
            while (board.isValidPosition(r, c)) {
                Piece p = board.getPiece(r, c);
                if (p != null) {
                    if (p.getColor() == enemyColor && (p instanceof Rook || p instanceof Queen)) {
                        return true;
                    } else {
                        break; // blocked by other piece
                    }
                }
                r += dir[0];
                c += dir[1];
            }
        }

        // Check diagonals (bishop + queen)
        for (int[] dir : diagonalDirections) {
            int r = toRow + dir[0];
            int c = toCol + dir[1];
            while (board.isValidPosition(r, c)) {
                Piece p = board.getPiece(r, c);
                if (p != null) {
                    if (p.getColor() == enemyColor && (p instanceof Bishop || p instanceof Queen)) {
                        return true;
                    } else {
                        break;
                    }
                }
                r += dir[0];
                c += dir[1];
            }
        }

        // Check knight attacks
        int[][] knightMoves = {
            { -2, -1 }, { -2, 1 }, { -1, -2 }, { -1, 2 },
            { 1, -2 }, { 1, 2 }, { 2, -1 }, { 2, 1 }
        };
        for (int[] move : knightMoves) {
            int r = toRow + move[0];
            int c = toCol + move[1];
            if (board.isValidPosition(r, c)) {
                Piece p = board.getPiece(r, c);
                if (p != null && p.getColor() == enemyColor && p instanceof Knight) {
                    return true;
                }
            }
        }

        // Check pawns attacks
        int pawnDir = (enemyColor == 'W') ? -1 : 1;
        int[] pawnCols = { -1, 1 };
        for (int dc : pawnCols) {
            int r = toRow + pawnDir;
            int c = toCol + dc;
            if (board.isValidPosition(r, c)) {
                Piece p = board.getPiece(r, c);
                if (p != null && p.getColor() == enemyColor && p instanceof Pawn) {
                    return true;
                }
            }
        }

        // Check enemy king nearby (one square around)
        int[] kingRowOffsets = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] kingColOffsets = {-1, 0, 1, -1, 1, -1, 0, 1};
        for (int i = 0; i < kingRowOffsets.length; i++) {
            int r = toRow + kingRowOffsets[i];
            int c = toCol + kingColOffsets[i];
            if (board.isValidPosition(r, c)) {
                Piece p = board.getPiece(r, c);
                if (p != null && p.getColor() == enemyColor && p instanceof King) {
                    return true;
                }
            }
        }

        return false;
    }
}
