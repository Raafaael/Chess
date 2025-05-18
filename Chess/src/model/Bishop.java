package model;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {

    public Bishop(char color, int row, int col) {
        super(color, row, col);
    }

    @Override
    public List<int[]> pieceMovement(Board board) {
        List<int[]> moves = new ArrayList<>();

        int[][] directions = {
            {-1, -1}, {-1, 1},
            {1, -1},  {1, 1}
        };

        for (int[] dir : directions) {
            int r = row + dir[0];
            int c = col + dir[1];

            while (board.isValidPosition(r, c)) {
                Piece target = board.getPiece(r, c);

                if (target == null) {
                    if (testMoveSafety(board, r, c)) {
                        moves.add(new int[]{r, c});
                    }
                } else {
                    if (target.getColor() != this.color && !(target instanceof King)) {
                        if (testMoveSafety(board, r, c)) {
                            moves.add(new int[]{r, c});
                        }
                    }
                    break; // encontrou obstáculo, para aqui
                }

                r += dir[0];
                c += dir[1];
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
    
    // Método auxiliar para impedir que a peça deixe o próprio Rei em cheque
    @Override
    public boolean testMoveSafety(Board board, int toRow, int toCol) {
        Piece capturedPiece = board.getPiece(toRow, toCol);
        int originalRow = this.row;
        int originalCol = this.col;

        board.makeMove(row, col, toRow, toCol);
        boolean inCheck = board.isInCheck(color);
        board.undoMove(row, col, toRow, toCol, capturedPiece);
        this.setPosition(originalRow, originalCol);

        return !inCheck;
    }

}
