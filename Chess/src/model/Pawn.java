package model;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {

    public Pawn(char color, int row, int col) {
        super(color, row, col);
    }

    @Override
    public List<int[]> pieceMovement(Board board) {
        List<int[]> candidateMoves = new ArrayList<>();

        int direction = (color == 'W') ? -1 : 1;
        int startRow = (color == 'W') ? 6 : 1;

        int nextRow = row + direction;
        int twoStepsRow = row + 2 * direction;

        // Movimento 1 casa à frente
        if (board.isValidPosition(nextRow, col) && board.isEmpty(nextRow, col)) {
            candidateMoves.add(new int[] {nextRow, col});

            // Movimento 2 casas à frente
            if (row == startRow && board.isValidPosition(twoStepsRow, col) && board.isEmpty(twoStepsRow, col)) {
                candidateMoves.add(new int[] {twoStepsRow, col});
            }
        }

        // Captura diagonal esquerda
        int diagLeftCol = col - 1;
        if (board.isValidPosition(nextRow, diagLeftCol)) {
            Piece target = board.getPiece(nextRow, diagLeftCol);
            if (target != null && target.getColor() != color && !(target instanceof King)) {
                candidateMoves.add(new int[] {nextRow, diagLeftCol});
            }
        }

        // Captura diagonal direita
        int diagRightCol = col + 1;
        if (board.isValidPosition(nextRow, diagRightCol)) {
            Piece target = board.getPiece(nextRow, diagRightCol);
            if (target != null && target.getColor() != color && !(target instanceof King)) {
                candidateMoves.add(new int[] {nextRow, diagRightCol});
            }
        }

        // Agora filtramos pela segurança dos movimentos
        List<int[]> safeMoves = new ArrayList<>();
        for (int[] move : candidateMoves) {
            if (testMoveSafety(board, move[0], move[1])) {
                safeMoves.add(move);
            }
        }

        return safeMoves;
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

        // Salvar posição original
        int originalRow = this.row;
        int originalCol = this.col;

        board.makeMove(row, col, toRow, toCol);
        boolean inCheck = board.isInCheck(color);
        board.undoMove(row, col, toRow, toCol, capturedPiece);

        // Restaurar posição do Pawn no objeto
        this.setPosition(originalRow, originalCol);

        return !inCheck;
    }
}
