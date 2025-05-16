package controller;

import model.Board;

public class ChessController {
	Board board = new Board();
	char turn;
	
    public ChessController() {
        this.board = new Board();
        this.turn = 'W';
        board.setupInitialPosition();
    }
    
    public void startGame() {
    	
    }
    
    public void finishGame() {
    	
    }
    
    public void save() {
    	
    }
    
}
