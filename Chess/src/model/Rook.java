package model;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {
    public Rook(char color, int row, int col) {
        super(color, row, col);
    }

    @Override
    public List<int[]> pieceMovement(Board board) {
        List<int[]> moves = new ArrayList<>();

        // Direções que o rook pode ir (linha e coluna)
        int[][] directions = {
            {-1, 0},  // cima
            {1, 0},   // baixo
            {0, -1},  // esquerda
            {0, 1}    // direita
        };

        for (int[] dir : directions) {
            int dRow = dir[0];
            int dCol = dir[1];
            int newRow = this.row;
            int newCol = this.col;

            // Move-se até encontrar uma peça ou borda
            while (true) {
                newRow += dRow;
                newCol += dCol;

                if (!board.isValidPosition(newRow, newCol)) {
                    break;
                }

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
                    break; // encontrou obstáculo, para nessa direção
                }
            }
        }

        return moves;
    }

    @Override
    public boolean canMove(int fromRow, int fromCol, int toRow, int toCol, Board board) {
        if (fromRow == toRow || fromCol == toCol) {
            if (board.isValidPosition(toRow, toCol)) {
                int rowStep = Integer.compare(toRow, fromRow);
                int colStep = Integer.compare(toCol, fromCol);
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

    // Verifica se o movimento não deixa o rei em xeque
    private boolean testMoveSafety(Board board, int toRow, int toCol) {
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
