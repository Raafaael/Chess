package game;

import board.Board;

public class Game {
	Board board = new Board();
	char turn;
	
    public Game() {
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
