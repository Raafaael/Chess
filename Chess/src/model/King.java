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
                    // Testa se o movimento deixa o rei em xeque
                    if (testMoveSafety(board, newRow, newCol)) {
                        moves.add(new int[]{newRow, newCol});
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

    private boolean testMoveSafety(Board board, int toRow, int toCol) {
        Piece capturedPiece = board.getPiece(toRow, toCol);

        board.makeMove(row, col, toRow, toCol);
        boolean inCheck = board.isInCheck(color);
        board.undoMove(row, col, toRow, toCol, capturedPiece);

        return !inCheck;
    }
}
