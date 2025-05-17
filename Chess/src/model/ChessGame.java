package model;

public class ChessGame {

    private final Board board;
    private Piece selectedPiece;
    private char currentTurn;

    public ChessGame() {
        board = Board.getInstance();
        currentTurn = 'W';
        board.setupInitialPosition();
    }

    public void resetBoard() {
        Board.resetBoard();
        selectedPiece = null;
    }

    public boolean selectPiece(int row, int col) {
        Piece piece = board.getPiece(row, col);
        if (piece != null && piece.getColor() == currentTurn) {
            selectedPiece = piece;
            return true;
        }
        return false;
    }

    public boolean selectTarget(int row, int col) {
        if (selectedPiece == null) return false;

        int fromRow = selectedPiece.getRow();
        int fromCol = selectedPiece.getCol();

        if (selectedPiece.canMove(fromRow, fromCol, row, col, board)) {
            Piece captured = board.getPiece(row, col);
            board.makeMove(fromRow, fromCol, row, col);
            if (board.isInCheck(currentTurn)) {
                board.undoMove(fromRow, fromCol, row, col, captured);
                return false;
            }

            toggleTurn();
            selectedPiece = null;
            return true;
        }

        return false;
    }

    public Piece getSelectedPiece() {
        return selectedPiece;
    }

    public char getCurrentTurn() {
        return currentTurn;
    }

    private void toggleTurn() {
        currentTurn = (currentTurn == 'W') ? 'B' : 'W';
    }
}
