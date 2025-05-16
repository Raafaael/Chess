package model;

public class Board {
    private Piece[][] squares = new Piece[8][8];

    public Board() {
        setupInitialPosition();
    }

    public Piece getPiece(int row, int col) {
        if (!isValidPosition(row, col)) {
            return null;
        }
        return squares[row][col];
    }

    public void setupInitialPosition() {
        squares[7][0] = new Rook('W', 7, 0);
        squares[7][1] = new Knight('W', 7, 1);
        squares[7][2] = new Bishop('W', 7, 2);
        squares[7][3] = new Queen('W', 7, 3);
        squares[7][4] = new King('W', 7, 4);
        squares[7][5] = new Bishop('W', 7, 5);
        squares[7][6] = new Knight('W', 7, 6);
        squares[7][7] = new Rook('W', 7, 7);
        for (int col = 0; col < 8; col++) {
            squares[6][col] = new Pawn('W', 6, col);
        }

        squares[0][0] = new Rook('B', 0, 0);
        squares[0][1] = new Knight('B', 0, 1);
        squares[0][2] = new Bishop('B', 0, 2);
        squares[0][3] = new Queen('B', 0, 3);
        squares[0][4] = new King('B', 0, 4);
        squares[0][5] = new Bishop('B', 0, 5);
        squares[0][6] = new Knight('B', 0, 6);
        squares[0][7] = new Rook('B', 0, 7);
        for (int col = 0; col < 8; col++) {
            squares[1][col] = new Pawn('B', 1, col);
        }
    }

    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    public boolean isEmpty(int row, int col) {
        if (!isValidPosition(row, col)) {
            return false;
        }
        return squares[row][col] == null;
    }

    public boolean hasEnemyPiece(int row, int col, char color) {
        if (!isValidPosition(row, col)) {
            return false;
        }
        Piece p = squares[row][col];
        if (p == null) {
            return false;
        }
        return p.getColor() != color;
    }

    public void makeMove(int fromRow, int fromCol, int toRow, int toCol) {
        Piece movingPiece = squares[fromRow][fromCol];
        squares[toRow][toCol] = movingPiece;
        squares[fromRow][fromCol] = null;
        if (movingPiece != null) {
            movingPiece.setPosition(toRow, toCol);
            movingPiece.setHasMoved(true);
        }
    }

    public void undoMove(int fromRow, int fromCol, int toRow, int toCol, Piece capturedPiece) {
        Piece movingPiece = squares[toRow][toCol];
        squares[fromRow][fromCol] = movingPiece;
        squares[toRow][toCol] = capturedPiece;
        if (movingPiece != null) {
            movingPiece.setPosition(fromRow, fromCol);
            // Aqui você pode ajustar o hasMoved se precisar controlar isso para undo
        }
    }

    public boolean isInCheck(char color) {
        int kingRow = -1, kingCol = -1;
        // Encontra o rei da cor
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = squares[r][c];
                if (p != null && p.getColor() == color && p instanceof King) {
                    kingRow = r;
                    kingCol = c;
                    break;
                }
            }
            if (kingRow != -1) break;
        }
        if (kingRow == -1) {
            // Rei não encontrado
            return false;
        }

        char enemyColor = (color == 'W') ? 'B' : 'W';

        // Verifica se alguma peça inimiga pode capturar o rei
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = squares[r][c];
                if (p != null && p.getColor() == enemyColor) {
                    if (p.canMove(r, c, kingRow, kingCol, this)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
