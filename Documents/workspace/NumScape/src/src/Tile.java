package src;

import java.util.ArrayList;

/* 
 * Tile is an abstract class which describes shared behavior of all subclass tiles: NumberTile,
 * OperatorTile, and BlankTile.  Behavior of methods described below.
 */

public abstract class Tile {
	protected String ASCIIimage;  //String representation of the tile
	protected boolean cleared;  //boolean that tracks whether a tile is still usable
	
	//makes a tile unavailable to be moved to.  Used after a tile gets picked up, not when swapped
	public void clear(){
		cleared = true;
		updateASCIIimage();
	}
	
	//returns char which represents the tile's value or operator (or blank) in a text-only format
	public String getASCIIimage(){
		return ASCIIimage;
	}
	
	//getter for cleared
	public boolean isCleared(){
		return cleared;
	}
	
	//abstract method implemented in subclasses
	public abstract void getPickedUp(ArrayList<Tile> pickups);
	
	//ensures ASCIIimage reflects current state (after clearing/swapping)
	public abstract void updateASCIIimage();
	
}
