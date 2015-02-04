package src;

import java.util.ArrayList;

/*
 * NumberTile is a subclass of Tile which has a number field. number is of Integer class to make
 * casting to String easier for ASCIIimage.
 */

public class NumberTile extends Tile{
	private Integer number;
	
	//NumberTile must be passed an integer in the constructor
	public NumberTile(int num){
		number = num;
		updateASCIIimage();
		cleared = false;
	}
	
	//getter for number
	public Integer getNumber(){
		return number;
	}
	
	//setter for number
	public void setNumber(int num){
		number = num;
		updateASCIIimage();
	}
	
	//make sure ASCIIimage represents must up-to-date nature of NumberTile
	public void updateASCIIimage(){
		if (cleared){
			ASCIIimage = "#";
		}
		else{
			ASCIIimage = number.toString();
		}
	}

	//swaps numbers if two NumberTiles picked up consecutively, else adds self to pickup list
	public void getPickedUp(ArrayList<Tile> pickups) {
		Tile recentPickup = pickups.get(pickups.size()-1);
		if (recentPickup instanceof NumberTile){
			Integer temp = this.number;
			this.number = ((NumberTile)recentPickup).getNumber();
			((NumberTile)recentPickup).setNumber(temp);
			updateASCIIimage();
		}
		else if (recentPickup instanceof OperatorTile){
			pickups.add(new NumberTile(this.number));
			clear();
		}
	}
	
}
