package model;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {
    public Knight(char color, int row, int col) {
        super(color, row, col);
    }

    // Movimentos possíveis
    @Override
    public List<int[]> pieceMovement(Board board) {
        List<int[]> moves = new ArrayList<>();
        // Movimentos do Cavalo
        int[][] movements = {
            {-2, 1}, {-2, -1}, {2, 1}, {2, -1},
            {-1, 2}, {-1, -2}, {1, 2}, {1, -2}
        };

        // Verifica os movimentos possíveis da posição atual
        for (int[] movement : movements) {
            int targetRow = this.row + movement[0];
            int targetCol = this.col + movement[1];

            // Verifica se o destino do movimento está dentro do tabuleiro
            if (targetRow >= 0 && targetRow < 8 && targetCol >= 0 && targetCol < 8) {
                Piece targetPiece = board.getPiece(targetRow, targetCol);
                /*
                 Verifica se o destino do movimento não possui uma peça
                 da mesma cor
                 */
                if (targetPiece == null || targetPiece.getColor() != this.color) {
                    // Verifica se o movimento é seguro (não deixa o rei em xeque)
                    if (testMoveSafety(board, targetRow, targetCol)) {
                        moves.add(new int[]{targetRow, targetCol});
                    }
                }
            }
        }
        return moves;
    }

    // Validação do movimento
    @Override
    public boolean canMove(int fromRow, int fromCol, int toRow, int toCol, Board board) {
        // Diferença absoluta
        int rowDiff = Math.abs(toRow - fromRow);
        int colDiff = Math.abs(toCol - fromCol);
        // Verifica o movimento do Cavalo
        if ((rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2)) {
            // Verifica se o destino do movimento está dentro do tabuleiro
            if (toRow >= 0 && toRow < 8 && toCol >= 0 && toCol < 8) {
                /*
                 Verifica se o destino do movimento não possui uma peça
                 da mesma cor
                 */
                Piece targetPiece = board.getPiece(toRow, toCol);
                return targetPiece == null || targetPiece.getColor() != this.color;
            }
        }
        return false;
    }

    // Verifica se o movimento não deixa o rei em xeque
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