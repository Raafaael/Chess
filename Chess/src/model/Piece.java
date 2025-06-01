package model;

import java.util.List;

public abstract class Piece {
    protected char color;
    protected int  row, col;
    protected boolean hasMoved;

    public Piece(char color,int row,int col){
        this.color=color; this.row=row; this.col=col;
    }
    
    public char getColor(){
    	return color; 
    	}
    
    public int  getRow(){
    	return row;
    	}
    
    public int  getCol(){ 
    	return col;
    	}
    
    public void setPosition(int r,int c){ 
    	row=r; col=c; 
    	}
    
    public void setHasMoved(boolean b){
    	hasMoved=b; 
    	}
    
    public boolean hasMoved(){
    	return hasMoved; 
    	}

    public abstract char getTypeChar();
    public abstract List<int[]> pieceMovement(Board board);
    public abstract boolean canMove(int fr,int fc,int tr,int tc,Board board);
}
