package model;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {

    public Pawn(char color, int row, int col) {
        super(color, row, col);
    }

    @Override
    public List<int[]> pieceMovement(Board board) {
        List<int[]> moves = new ArrayList<>();

        int direction = (color == 'W') ? -1 : 1; // Branco sobe (-1), preto desce (+1)
        int startRow = (color == 'W') ? 6 : 1;

        int nextRow = row + direction;

        // Avança 1 casa se estiver vazia
        if (board.isValidPosition(nextRow, col) && board.isEmpty(nextRow, col)) {
            if (testMoveSafety(board, nextRow, col)) {
                moves.add(new int[] {nextRow, col});
            }

            // Avança 2 casas na primeira jogada, se as duas estiverem vazias
            int twoStepsRow = row + 2 * direction;
            if (row == startRow && board.isEmpty(twoStepsRow, col)) {
                if (testMoveSafety(board, twoStepsRow, col)) {
                    moves.add(new int[] {twoStepsRow, col});
                }
            }
        }

        // Captura diagonal esquerda
        int diagLeftCol = col - 1;
        if (board.isValidPosition(nextRow, diagLeftCol) && board.hasEnemyPiece(nextRow, diagLeftCol, color)) {
            if (testMoveSafety(board, nextRow, diagLeftCol)) {
                moves.add(new int[] {nextRow, diagLeftCol});
            }
        }

        // Captura diagonal direita
        int diagRightCol = col + 1;
        if (board.isValidPosition(nextRow, diagRightCol) && board.hasEnemyPiece(nextRow, diagRightCol, color)) {
            if (testMoveSafety(board, nextRow, diagRightCol)) {
                moves.add(new int[] {nextRow, diagRightCol});
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

    // Método auxiliar para testar se o movimento não deixa o rei em xeque
    private boolean testMoveSafety(Board board, int toRow, int toCol) {
        Piece capturedPiece = board.getPiece(toRow, toCol);

        board.makeMove(row, col, toRow, toCol);
        boolean inCheck = board.isInCheck(color);
        board.undoMove(row, col, toRow, toCol, capturedPiece);

        return !inCheck;
    }
}
