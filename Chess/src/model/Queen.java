package model;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {

    public Queen(char color, int row, int col) {
        super(color, row, col);
    }

    @Override
    public List<int[]> pieceMovement(Board board) {
        List<int[]> moves = new ArrayList<>();

        // Direções: verticais, horizontais, diagonais
        int[][] directions = {
            {-1, 0}, {1, 0},   // vertical
            {0, -1}, {0, 1},   // horizontal
            {-1, -1}, {-1, 1}, // diagonal
            {1, -1},  {1, 1}
        };

        for (int[] dir : directions) {
            int dRow = dir[0];
            int dCol = dir[1];
            int newRow = this.row + dRow;
            int newCol = this.col + dCol;

            while (board.isValidPosition(newRow, newCol)) {
                Piece targetPiece = board.getPiece(newRow, newCol);

                if (targetPiece == null) {
                    if (testMoveSafety(board, newRow, newCol)) {
                        moves.add(new int[]{newRow, newCol});
                    }
                } else {
                    if (targetPiece.getColor() != this.color && !(targetPiece instanceof King)) {
                        if (testMoveSafety(board, newRow, newCol)) {
                            moves.add(new int[]{newRow, newCol});
                        }
                    }
                    break;
                }

                newRow += dRow;
                newCol += dCol;
            }
        }

        return moves;
    }

    @Override
    public boolean canMove(int fromRow, int fromCol, int toRow, int toCol, Board board) {
        if (board.isValidPosition(toRow, toCol)) {
            int rowStep = Integer.compare(toRow, fromRow);
            int colStep = Integer.compare(toCol, fromCol);

            // Movimentos válidos: vertical, horizontal ou diagonal
            if (rowStep == 0 || colStep == 0 || Math.abs(rowStep) == Math.abs(colStep)) {
                int currentRow = fromRow + rowStep;
                int currentCol = fromCol + colStep;

                while (currentRow != toRow || currentCol != toCol) {
                    if (board.getPiece(currentRow, currentCol) != null) {
                        return false; // caminho bloqueado
                    }
                    currentRow += rowStep;
                    currentCol += colStep;
                }

                Piece targetPiece = board.getPiece(toRow, toCol);
                return targetPiece == null || targetPiece.getColor() != this.color;
            }
        }

        return false;
    }

    // Verifica se o movimento não expõe o rei ao xeque
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
