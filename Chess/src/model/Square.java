package model;

/** Casa simples: apenas armazena a peça atual. */
class Square {
    private Piece piece;

    Square(Piece p) {
    	this.piece = p; 
    	}
    
    Piece getPiece() { 
    	return piece;   
    	}
    
    void setPiece(Piece p) { 
    	this.piece = p; 
    	}
}
