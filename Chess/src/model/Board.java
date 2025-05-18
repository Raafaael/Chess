package model;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private static Board instance;
    private Square[][] squares = new Square[8][8];
    private static Piece selectedPiece = null;

    public Board() {
        initializeEmptyBoard();
    }

    public static Board getInstance() {
        if (instance == null) {
            instance = new Board();
        }
        return instance;
    }

    public static void resetBoard() {
        instance = null;
    }

    private void initializeEmptyBoard() {
        for (int col = 0; col < 8; col++) {
            for (int row = 0; row < 8; row++) {
                squares[col][row] = new Square(col, row, null);
            }
        }
    }

    public void setupInitialPosition() {
        for (int col = 0; col < 8; col++) {
            squares[col][1].setPiece(new Pawn('W', 1, col));
            squares[col][6].setPiece(new Pawn('B', 6, col));
        }

        squares[0][0].setPiece(new Rook('W', 0, 0));
        squares[1][0].setPiece(new Knight('W', 0, 1));
        squares[2][0].setPiece(new Bishop('W', 0, 2));
        squares[3][0].setPiece(new Queen('W', 0, 3));
        squares[4][0].setPiece(new King('W', 0, 4));
        squares[5][0].setPiece(new Bishop('W', 0, 5));
        squares[6][0].setPiece(new Knight('W', 0, 6));
        squares[7][0].setPiece(new Rook('W', 0, 7));

        squares[0][7].setPiece(new Rook('B', 7, 0));
        squares[1][7].setPiece(new Knight('B', 7, 1));
        squares[2][7].setPiece(new Bishop('B', 7, 2));
        squares[3][7].setPiece(new Queen('B', 7, 3));
        squares[4][7].setPiece(new King('B', 7, 4));
        squares[5][7].setPiece(new Bishop('B', 7, 5));
        squares[6][7].setPiece(new Knight('B', 7, 6));
        squares[7][7].setPiece(new Rook('B', 7, 7));
    }

    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    public boolean isEmpty(int row, int col) {
        return getPiece(row, col) == null;
    }

    public boolean hasEnemyPiece(int row, int col, char color) {
        Piece piece = getPiece(row, col);
        return piece != null && piece.getColor() != color;
    }

    public Piece getPiece(int row, int col) {
        if (!isValidPosition(row, col)) return null;
        return squares[col][row].getPiece();
    }

    public void setPiece(int row, int col, Piece piece) {
        if (isValidPosition(row, col)) {
            squares[col][row].setPiece(piece);
        }
    }

    public void makeMove(int fromRow, int fromCol, int toRow, int toCol) {
        Piece moving = getPiece(fromRow, fromCol);
        Piece captured = getPiece(toRow, toCol);
        setPiece(toRow, toCol, moving);
        setPiece(fromRow, fromCol, null);
        if (moving != null) {
            moving.setPosition(toRow, toCol);
            moving.setHasMoved(true);
        }
    }

    public void undoMove(int fromRow, int fromCol, int toRow, int toCol, Piece captured) {
        Piece moving = getPiece(toRow, toCol);
        setPiece(fromRow, fromCol, moving);
        setPiece(toRow, toCol, captured);
        if (moving != null) {
            moving.setPosition(fromRow, fromCol);
        }
    }

    public boolean isInCheck(char color) {
        int kingRow = -1, kingCol = -1;

        // Busca pelo rei
        for (int col = 0; col < 8; col++) {
            for (int row = 0; row < 8; row++) {
                Piece p = getPiece(row, col);
                if (p instanceof King && p.getColor() == color) {
                    kingRow = row;
                    kingCol = col;
                    break;
                }
            }
            if (kingRow != -1) break;
        }

        if (kingRow == -1) return false; // rei não encontrado

        char enemyColor = (color == 'W') ? 'B' : 'W';

        // Verifica ameaças inimigas
        for (int col = 0; col < 8; col++) {
            for (int row = 0; row < 8; row++) {
                Piece p = getPiece(row, col);
                if (p != null && p.getColor() == enemyColor) {
                    if (p.canMove(row, col, kingRow, kingCol, this)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }


    public boolean isCheckmate(char color) {
        if (!isInCheck(color)) {
            return false; // Not in check, so no checkmate
        }

        for (Piece piece : getAllPieces()) {
            if (piece.getColor() != color) continue;

            List<int[]> moves = piece.pieceMovement(this);
            for (int[] move : moves) {
                int fromRow = piece.getRow();
                int fromCol = piece.getCol();
                int toRow = move[0];
                int toCol = move[1];
                Piece captured = getPiece(toRow, toCol);

                makeMove(fromRow, fromCol, toRow, toCol);
                boolean stillInCheck = isInCheck(color);
                undoMove(fromRow, fromCol, toRow, toCol, captured);

                if (!stillInCheck) {
                    return false; // Encontrou um movimento que escapa de cheque
                }
            }
        }
        return true; // Sem movimentos para escapar de cheque
    }

    public List<Piece> getAllPieces() {
        List<Piece> pieces = new ArrayList<>();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece p = getPiece(row, col);
                if (p != null) {
                    pieces.add(p);
                }
            }
        }
        return pieces;
    }

    public static Piece getSelectedPiece() {
        return selectedPiece;
    }

    public static void setSelectedPiece(Piece selected) {
        selectedPiece = selected;
    }
}
