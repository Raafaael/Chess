package model;

import java.util.List;

public abstract class Piece {
	protected char color;
	protected int row;
	protected int col;
	protected boolean hasMoved;
	
	public Piece(char color, int row, int col) {
		this.color = color;
	    this.row = row;
	    this.col = col;
	}
	
	public char getColor() {
		return color;
	}
	
    public int getRow() {
    	return row; 
    }
    
    public int getCol() {
    	return col;
    }
	
    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }
    
    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }
    
    public abstract List<int[]> pieceMovement(Board board);
    
    public abstract boolean canMove(int fromRow, int fromCol, int toRow, int toCol, Board board);
    
    public abstract boolean testMoveSafety(Board board, int toRow, int toCol);
}
