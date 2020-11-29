import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import javax.swing.ImageIcon;



public class Game implements ActionListener{
	//This class belongs to nick now
	
	
	final int ROWS = 10;
	final int COLS = 15;
	final int NUMSPACES = ROWS*COLS;
	
	private boolean isBlueTurn;
	private Board board;
	private String userInput;
	
	String action;
	int activePiece;
	int secondPoint;
	int moveCounter;
	
	Scanner in;
	
	Gui gui;
	
	/**
	 * Constructor for a new game instance
	 */
	public Game() {
		//Initialize board
		board = new Board(ROWS, COLS);
		board.initializeGame();
		isBlueTurn = true;
		
		//Initialize the gui
		gui = new Gui(this);
		gui.placePieces(board.getPositionsAndPieces(), isBlueTurn);
		gui.updateTeam(isBlueTurn);
		System.out.println(board.getPositionsAndPieces());
		
		//Initialize the variables for the game play.
		action = "click";
		moveCounter = 0;
		
		//Make the playable pieces active.
		this.makePiecesPlayable();
		
	}
	
	/**
	 * The action listener for the game. runs every time the jvm detects an action as outlined
	 * in the gui class. This class decodes the action command string to find the button pressed
	 * and send out its location to the gamrerun method, or change teams if that button is pressed.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		//check to see if it is a gameboard button.
		for (int i = 0; i <= NUMSPACES-1; i++) {
			if (e.getActionCommand().equals("MATRIX_BUTTON_" + i)) {
				gameRun(i);
				System.out.println(i);
			}
			
		}
		//check to see if it is the turn switch button.
		if (e.getActionCommand().equals("NEXT_TURN")) {
			if (action.equals("click")) {
				isBlueTurn = !isBlueTurn;
				gui.pieceButtons(board.getPositionsAndPieces(), isBlueTurn);
				gui.updateTeam(isBlueTurn);
				this.makePiecesPlayable();
			}
		}
	}
	
	/**
	 * Runs in reaction to a button press on the board. Uses data set by the action listener to run
	 * an iteration of a game move.
	 * 
	 */
	public void gameRun(int userInput) {
		//react based if the current action and change to the next action.
		switch(action) {
		//the unit select action.
		case "click":
			activePiece = userInput;
			//use different colors depending on the current active team.
			if (isBlueTurn) {
				gui.setButtonColor(userInput, "blueSelect");
			}
			else {
				gui.setButtonColor(userInput, "redSelect");
			}
			//update gui
			gui.moveButtons(board.availableActions(userInput), activePiece);
			gui.setStats(board.getPieceAt(userInput));
			//only allow the unit to be selected if they have a turn available.
			if (board.getPieceAt(activePiece).isMoveable()) {
				action = "action";
			}
			else {
				gui.placePieces(board.getPositionsAndPieces(), isBlueTurn);
			}

			break;
		//The unit performs action.
		case "action":
			secondPoint = userInput;
			if (userInput == activePiece) {
				gui.placePieces(board.getPositionsAndPieces(), isBlueTurn);
				
			}
			//attack
			else if (board.availableActions(activePiece).get((Integer) secondPoint).charAt(0)=='a') {
				board.fight((Integer) activePiece, (Integer) secondPoint);
				
				board.getPieceAt(activePiece).canMove(false);
			}
			//heal
			else if (board.availableActions(activePiece).get((Integer) secondPoint).charAt(0)=='h') {
				board.castHeal((Integer) activePiece, (Integer) secondPoint);
				board.getPieceAt(activePiece).canMove(false);
			}
			//move
			else if (board.availableActions(activePiece).get((Integer) secondPoint).charAt(0)=='m') {
				
				board.movePiece((Integer) activePiece, (Integer) secondPoint);
				System.out.println("Piece moved");
				System.out.println(board.getPieceAt(secondPoint).getPieceType().substring(0));
				if ((board.getPieceAt(secondPoint).getPieceType().substring(0) == "Knight")) {
					if (moveCounter == 1) {
						board.getPieceAt(secondPoint).canMove(false);
						moveCounter = 0;
					}
					else {
						moveCounter++;
					}
					System.out.println(moveCounter);
				}
				else {
					board.getPieceAt(secondPoint).canMove(false);
				}

			}
			//update gui
			gui.placePieces(board.getPositionsAndPieces(), isBlueTurn);
			gui.clearStats();
			action = "click";
			break;
		
		}
		
		
	}
	
	/**
	 * @return true if red is out of pieces and blue has won the game
	 */
	private boolean blueHasWon() {
		return false;
	}
	
	/**
	 * @return true if blue is out of pieces and red has won the game
	 */
	private boolean redHasWon() {
		return false;
	}
	
	/**
	 * @author Nicholas Sparks\
	 * resets the moves of the players on each team.
	 */
	private void makePiecesPlayable() {
		for(int i = 0; i < NUMSPACES; i++) {
			GamePiece unit = board.getPieceAt(i);
			if (!(unit == null)) {
				if (unit.isBlueTeam == this.isBlueTurn) {
					unit.canMove(true);
				}
				else {
					unit.canMove(false);
				}
			}
		}
	}
	
}
