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

        int direction = (color == 'W') ? -1 : 1; // White moves up (-1), Black moves down (+1)
        int startRow = (color == 'W') ? 6 : 1;

        int nextRow = row + direction;
        int twoStepsRow = row + 2 * direction;

        if (board.isValidPosition(nextRow, col) && board.isEmpty(nextRow, col)) {
            if (testMoveSafety(board, nextRow, col)) {
                moves.add(new int[] {nextRow, col});
            }

            if (row == startRow && board.isValidPosition(twoStepsRow, col) && board.isEmpty(twoStepsRow, col)) {
                // Importante: já sabemos que nextRow está vazia pelo if externo
                if (testMoveSafety(board, twoStepsRow, col)) {
                    moves.add(new int[] {twoStepsRow, col});
                }
            }
        }


        // Capture diagonally left
        int diagLeftCol = col - 1;
        if (board.isValidPosition(nextRow, diagLeftCol) && board.hasEnemyPiece(nextRow, diagLeftCol, color)) {
            Piece target = board.getPiece(nextRow, diagLeftCol);
            // Avoid capturing the enemy King
            if (!(target instanceof King)) {
                if (testMoveSafety(board, nextRow, diagLeftCol)) {
                    moves.add(new int[] {nextRow, diagLeftCol});
                }
            }
        }

        // Capture diagonally right
        int diagRightCol = col + 1;
        if (board.isValidPosition(nextRow, diagRightCol) && board.hasEnemyPiece(nextRow, diagRightCol, color)) {
            Piece target = board.getPiece(nextRow, diagRightCol);
            // Avoid capturing the enemy King
            if (!(target instanceof King)) {
                if (testMoveSafety(board, nextRow, diagRightCol)) {
                    moves.add(new int[] {nextRow, diagRightCol});
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

    // Helper method to check if a move doesn't leave own king in check
    private boolean testMoveSafety(Board board, int toRow, int toCol) {
        Piece capturedPiece = board.getPiece(toRow, toCol);

        board.makeMove(row, col, toRow, toCol);
        boolean inCheck = board.isInCheck(color);
        board.undoMove(row, col, toRow, toCol, capturedPiece);

        System.out.println("Testing move to " + toRow + "," + toCol + " inCheck: " + inCheck);
        return !inCheck;
    }

}
