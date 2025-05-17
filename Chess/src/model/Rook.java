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
        
        // Direções que pode ir
        int[][] directions = {
            {-1, 0},
            {1, 0},
            {0, -1},
            {0, 1} 
        };

        for (int[] dir : directions) {
            int dRow = dir[0];
            int dCol = dir[1];
            int newRow = this.row;
            int newCol = this.col;

            // Move-se até atingir uma peça ou borda
            while (true) {
                newRow += dRow;
                newCol += dCol;

                // Verifica se ainda está no tabuleiro
                if (newRow < 0 || newRow >= 8 || newCol < 0 || newCol >= 8) {
                    break;
                }

                Piece targetPiece = board.getPiece(newRow, newCol);
                // Adiciona movimento se estiver vazio ou com peça de oponente
                if (targetPiece == null || targetPiece.getColor() != this.color) {
                    moves.add(new int[]{newRow, newCol});
                }
                // Para se atingir uma qualquer peça
                if (targetPiece != null) {
                    break;
                }
            }
        }

        return moves;
    }

    @Override
    public boolean canMove(int fromRow, int fromCol, int toRow, int toCol, Board board) {
        // Verifica movimento vertical ou horizontal
        if (fromRow == toRow || fromCol == toCol) {
            // Garante que esteja dentro do tabuleiro
            if (toRow >= 0 && toRow < 8 && toCol >= 0 && toCol < 8) {
                // Verifica se o caminho está livre e se o destino é válido
                int rowStep = Integer.compare(toRow, fromRow);
                int colStep = Integer.compare(toCol, fromCol);
                int currentRow = fromRow + rowStep;
                int currentCol = fromCol + colStep;

                while (currentRow != toRow || currentCol != toCol) {
                    if (board.getPiece(currentRow, currentCol) != null) {
                        return false; //Caminho bloqueado
                    }
                    currentRow += rowStep;
                    currentCol += colStep;
                }

                // Verifica destino
                Piece targetPiece = board.getPiece(toRow, toCol);
                if (targetPiece == null || targetPiece.getColor() != this.color) {
                    return true;
                }
            }
        }
        return false;
    }
}