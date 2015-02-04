package src;

import java.util.ArrayList;
import src.Board.Operator;

/*
 * OperatorTile is a subclass of Tile which has an Operator field.  Operator is an enum implemented
 * in Board.
 */

public class OperatorTile extends Tile{
	private Operator op;
	
	//OperatorTile must be passed an Operator in the constructor
	public OperatorTile(Operator o){
		op = o;
		updateASCIIimage();
		cleared = false;
	}
	
	//getter for op field
	public Operator getOperator(){
		return op;
	}
	
	//setter for op field
	public void setOperator(Operator o){
		op = o;
		updateASCIIimage();
	}
	
	//make sure ASCIIimage represents must up-to-date nature of OperatorTile
	public void updateASCIIimage(){
		if (cleared){
			ASCIIimage = "#";
		}
		else{
			switch (op){
			case PLUS:
				ASCIIimage = "+";
				break;
			case MINUS:
				ASCIIimage = "-";
				break;
			}
		}
	}

	//swaps ops if two OperatorTiles picked up consecutively, else adds self to pickup list
	public void getPickedUp(ArrayList<Tile> pickups) {
		Tile recentPickup = pickups.get(pickups.size()-1);
		if (recentPickup instanceof OperatorTile){
			Operator temp = this.op;
			this.op = ((OperatorTile)recentPickup).getOperator();
			((OperatorTile)recentPickup).setOperator(temp);
			updateASCIIimage();
		}
		else if (recentPickup instanceof NumberTile){
			pickups.add(new OperatorTile(this.op));
			clear();
		}
	}

}
