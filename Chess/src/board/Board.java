package board;

import pieces.*;

public class Board {
	private Piece[][] squares = new Piece[8][8];
	
	public Piece getPiece(int row, int col) {
		return squares[row][col];
	}
	
	public void movePiece(int fromRow, int fromCol, int toRow, int toCol) {

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
}
