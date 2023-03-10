package assignment2;

import java.util.List;

import assignment2.Piece.Type;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class BoardPanel extends GridPane implements EventHandler<ActionEvent> {

    private final View view;
    private final Board board;
    private boolean pickedPiece;
    private Cell cell;
    

    /**
     * Constructs a new GridPane that contains a Cell for each position in the board
     *
     * Contains default alignment and styles which can be modified
     * @param view
     * @param board
     */
    public BoardPanel(View view, Board board) {
        this.view = view;
        this.board = board;

        // Can modify styling
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-background-color: #181a1b;");
        int size = 550;
        this.setPrefSize(size, size);
        this.setMinSize(size, size);
        this.setMaxSize(size, size);

        setupBoard();
        updateCells();
    }


    /**
     * Setup the BoardPanel with Cells
     */
    private void setupBoard(){ // TODO
    	
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
            	this.add(this.board.board[row][col], row, col);
            	this.board.board[row][col].setOnAction(this);
            }
        }
        
    	

    }
    
    

    /**
     * Updates the BoardPanel to represent the board with the latest information
     *
     * If it's a computer move: disable all cells and disable all game controls in view
     *
     * If it's a human player turn and they are picking a piece to move:
     *      - disable all cells
     *      - enable cells containing valid pieces that the player can move
     * If it's a human player turn and they have picked a piece to move:
     *      - disable all cells
     *      - enable cells containing other valid pieces the player can move
     *      - enable cells containing the possible destinations for the currently selected piece
     *
     * If the game is over:
     *      - update view.messageLabel with the winner ('MUSKETEER' or 'GUARD')
     *      - disable all cells
     */
    protected void updateCells(){ // TODO
    	
        
        List<Cell> possiblePieces = this.board.getPossibleCells();
        
        
        if(this.board.isGameOver()) {
        	if (this.board.getWinner() == Type.MUSKETEER) {
        		
        		this.view.setMessageLabel("MUSKETEER");
        	}
        	else {
        		
        		this.view.setMessageLabel("GUARD");
        	}
        	
	        for (int row = 0; row < 5; row++) {
	            for (int col = 0; col < 5; col++) {;
	            	this.board.board[row][col].setDisable(true);
	            }
	        }	
        }
    	
        else if (view.model.isHumanTurn()) {
    		if (this.isPickedPiece()) {
    			List<Cell> destinations = this.board.getPossibleDestinations(this.cell);
    	        for (int row = 0; row < 5; row++) {
    	            for (int col = 0; col < 5; col++) {;
    	            	this.board.board[row][col].setDisable(true);
    	            }
    	        }
    	        for (int row = 0; row < 5; row++) {
    	            for (int col = 0; col < 5; col++) {;
    	            if(destinations.contains(this.board.board[row][col]) || possiblePieces.contains(this.board.board[row][col])){
    	            	this.board.board[row][col].setDisable(false);	
    	            	}
    	            }
    	        }	
    		}
    		else {
    	        for (int row = 0; row < 5; row++) {
    	            for (int col = 0; col < 5; col++) {;
    	            	this.board.board[row][col].setDisable(true);
    	            } 	
    	        }
    	        for (int row = 0; row < 5; row++) {
    	            for (int col = 0; col < 5; col++) {;
    	            if(possiblePieces.contains(this.board.board[row][col])){
    	            	this.board.board[row][col].setDisable(false);	
    	            	}
    	            }
    	        }
    	       
    		}
    	}
    	
    	else {
   	        for (int row = 0; row < 5; row++) {
	            for (int col = 0; col < 5; col++) {;
	            	this.board.board[row][col].setDisable(true);
	            }
	        }
   	        if(this.view.undoButton != null) {
   	        	this.view.undoButton.setDisable(true);
   	        }
   	        if (this.view.saveButton != null) {
   	        this.view.saveButton.setDisable(true);
   	        }
   	        if(this.view.restartButton != null) {
   	        	this.view.restartButton.setDisable(true);
   	        }
    	}
    	

    }

    /**
     * Handles Cell clicks and updates the board accordingly
     * When a Cell gets clicked the following must be handled:
     *  - If it's a valid piece that the player can move, select the piece and update the board
     *  - If it's a destination for a selected piece to move, perform the move and update the board
     * @param actionEvent
     */
    @Override
    public void handle(ActionEvent actionEvent) { // TODO
    	
    	
        List<Cell> possiblePieces = this.board.getPossibleCells();
 
    	Cell source = (Cell) actionEvent.getSource();
    	
    	if(possiblePieces.contains(source)) {
    		this.setPickedPiece(true);
    		this.cell = source;
    		this.updateCells();
    	}
    	
    	else if (this.isPickedPiece()) {
    	List<Cell> destinations = this.board.getPossibleDestinations(this.cell);
    	if(destinations.contains(source)) {
    		
    		Move move = new Move(this.cell, source);
    		this.view.model.move(move);
    		this.cell = source;
    		
    		this.view.runMove();
    		
    	}
    	}
  
    }


	public boolean isPickedPiece() {
		return pickedPiece;
	}


	public void setPickedPiece(boolean pickedPiece) {
		this.pickedPiece = pickedPiece;
	}
}
