package src;

import java.util.ArrayList;

/*
 * BlankTile is a subclass of Tile which has no special fields.
 */

public class BlankTile extends Tile{
	
	//BlankTile takes nothing in the constructor
	public BlankTile(){
		ASCIIimage = " ";
		cleared = false;
	}

	//make sure ASCIIimage represents must up-to-date nature of NumberTile
	public void updateASCIIimage() {
		if (cleared){
			ASCIIimage = "#";
		}
		else{
			ASCIIimage = " ";
		}
	}

	//BlankTiles aren't picked up, merely cleared
	public void getPickedUp(ArrayList<Tile> pickups) {
		clear();
	}
}
