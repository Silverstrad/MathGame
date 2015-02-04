package src;

import java.util.ArrayList;
import java.util.Random;


/*
 * Board class represents the collection of playable tiles. Tiles are maintained in a 2D array
 * along with the length and width of the board. Enum for Operator implemented here.
 * Currently the empty constructor should be used as the smart constructor has not been 
 * implemented. Empty constructor builds a 5x5 board with random tiles.  Methods for 
 * moving around the board implemented here.
 */
public class Board {
	public enum Operator{ 
		PLUS, MINUS
	}
	public enum Direction{
		UP, DOWN, LEFT, RIGHT
	}
	private Tile[][] tiles;  //2D array of tiles
	private int height;  
	private int width;
	private int[] activeTile; //x-y coords of tile the player is currently on 
	private int turnsRemaining; //number of turns player has left
	private int targetNumber; //number that the player must end with
	private ArrayList<Tile> pickups; //list of tiles picked up so far
	
	//dumb constructor, used for development purposes until smart generate is implemented
	public Board(){
		activeTile = new int[] {0, 0};  //player starts in top-left corner
		pickups = new ArrayList<Tile>();
		pickups.add(new NumberTile(0)); //start with 0
		width = 5;
		height = 5;
		targetNumber = 10;
		turnsRemaining = 15;
		tiles = new Tile[width][height];
		
		//fill in tiles randomly
		for (int i = 0; i < width; i ++){
			for (int j = 0; j < height; j++){
				Random rand1 = new Random();
				switch (rand1.nextInt(3)){
				case 0:
					//number tile
					tiles[i][j] = new NumberTile(rand1.nextInt(10));
					break;
				case 1:
					//operator tile
					switch (rand1.nextInt(2)){
					case 0:
						tiles[i][j] = new OperatorTile(Operator.PLUS);
						break;
					case 1:
						tiles[i][j] = new OperatorTile(Operator.MINUS);
						break;
					}
					break;
				case 2:
					//blank tile
					tiles[i][j] = new BlankTile();
					break;
				}
			}
		}
		tiles[activeTile[0]][activeTile[1]] = new BlankTile(); //ensure starting position is blank
		
	}
	
	//smart constructor, uses board generation algorithm to create board of size l x w
	public Board(int l, int w){
		//NOT IMPLEMENTED
	}
	
	//right now redirects to displayASCII, implemented with real graphics later
	public void display(){
		displayASCII();
	}
	
	//displays a text-only representation of the board
	public void displayASCII(){
		for (int i = 0; i < height; i++){
			System.out.print(" ");
			for (int j = 0; j < width; j++){
				if (j == activeTile[0] && i == activeTile[1] + 1){
					System.out.print(" ^  ");
				}
				else{
					System.out.print(" _  ");
				}
			}
			System.out.println();
			System.out.print("| ");
			for (int j = 0; j < width; j++){
				System.out.print(tiles[j][i].getASCIIimage());
				if (width - j > 1) System.out.print(" | ");
			}
			System.out.print(" |");
			System.out.println();


		}
		System.out.print(" ");
		for (int j = 0; j < width; j++){
			if (j == activeTile[0] && activeTile[1] == height - 1){
				System.out.print(" ^  ");
			}
			else{
				System.out.print(" _  ");
			}
		}
		System.out.println("\nTurns Remaining: "+this.turnsRemaining+"\nTarget Number: "+this.targetNumber+"\nProgress: "+getPickupString());
	}
	
	//tests if player can move in a given direction
	public boolean canMove(Direction dir){
		int deltaX = 0;
		int deltaY = 0;
		int[] tempActive = activeTile.clone();
		switch (dir){
		case UP: 
			deltaY = -1;
			break;
		case DOWN:
			deltaY = 1;
			break;
		case LEFT:
			deltaX = -1;
			break;
		case RIGHT:
			deltaX = 1;
			break;
		}
		tempActive[0] += deltaX;
		tempActive[1] += deltaY;
		
		
		if (tempActive[0] >= width || tempActive[0] <= -1 ){
			return false;
		}
		if (tempActive[1] >= height || tempActive[1] <= -1 ){
			return false;
		}
		if (tiles[tempActive[0]][tempActive[1]].isCleared()){
			return false;
		}
		return true;
		
	}
	
	//move in a given direction
	public void makeMove(Direction dir){
		int deltaX = 0;
		int deltaY = 0;
		switch (dir){
		case UP: 
			deltaY = -1;
			break;
		case DOWN:
			deltaY = 1;
			break;
		case LEFT:
			deltaX = -1;
			break;
		case RIGHT:
			deltaX = 1;
			break;
		}
		this.activeTile[0] += deltaX;
		this.activeTile[1] += deltaY;
		processTile(activeTile);
		this.turnsRemaining--;
		
	}
	
	//Converts an interpreted direction into an actual move, updates turn counter
	public void attemptMove(Direction dir){
		if (canMove(dir)){
			makeMove(dir);
		}
	}
	
	//takes tile coordinates for recent destination and attempts to pick up that tile
	private void processTile(int[] tileCoords){
		Tile currentTile = tiles[tileCoords[0]][tileCoords[1]];
		currentTile.getPickedUp(pickups);
	}
	
	//converts list of picked up tiles into a handy string
	public String getPickupString(){
		String temp = "";
		for (int i = 0; i < pickups.size(); i++){
			temp += pickups.get(i).getASCIIimage();
			temp +=" ";
		}
		return temp;
	}
	
	//Calculates result of pickups, returns null if ended with operator (must be handled)
	public Integer iterativeCalculate(){
		
		//if last tile was an operator
		if (pickups.get(pickups.size()-1) instanceof OperatorTile){
			return null;
		}
		
		Integer cumulative=0;
		for (int index = 0; index < pickups.size(); index++){
			if (index == 0){
				cumulative = ((NumberTile)pickups.get(0)).getNumber();
			}
			else{
				if (pickups.get(index) instanceof NumberTile){
					Operator op = ((OperatorTile)pickups.get(index-1)).getOperator();
					switch (op){
					case PLUS:
						cumulative += ((NumberTile)pickups.get(index)).getNumber();
						break;
					case MINUS:
						cumulative -= ((NumberTile)pickups.get(index)).getNumber();
						break;
					}
				}
			}
		}
		return cumulative;
		
	}
		
	//checks if pickup result hit the target, handles null returns from calculate
	public boolean onTarget(){
		//crunch the numbers 
		Integer result = iterativeCalculate();
		if (result == null){
			return false;
		}
		if (result == targetNumber){
			return true;
		}
		return false;
	}
	
	public int getTurnsRemaining(){
		return turnsRemaining;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public Tile[][] getTiles(){
		return tiles;
	}
	
	public int[] getActiveTile(){
		return activeTile;
	}
}
