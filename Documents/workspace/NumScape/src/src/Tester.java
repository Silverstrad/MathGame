package src;

import java.io.IOException;
import java.util.Scanner;
import src.Board.Direction;

public class Tester {

	public static void main(String[] args) throws IOException {
		
		//init board
		Board b = new Board();
		
		//char inputs are interpreted as directional commands
		char command= ' ';
		
		//allows for quitting, erroring out
		boolean runBool = true;
		
		//Scanner used for input
		Scanner s = new Scanner(System.in);
		
		//play while you have turns left or until quitting
		while (b.getTurnsRemaining()>0  && runBool){
			
			b.display();
			System.out.println("");
			
			//try to read in a new direction command
			try{
				command = s.nextLine().charAt(0);
			} catch (Exception e){
				//command gets useless char, effectively nothing happens
				command = ' ';
			}
			
			//interpret char into direction, pass the direction to the board
			switch (command){
			case 's':
				b.attemptMove(Direction.DOWN);
				break;
			case 'a':
				b.attemptMove(Direction.LEFT);
				break;
			case 'w':
				b.attemptMove(Direction.UP);
				break;
			case 'd':
				b.attemptMove(Direction.RIGHT);
				break;
			//hitting 'q' causes quit
			case 'q':
				runBool = false;
				break;
			}
			
			//if player is stuck, quit out
			if (!b.canMove(Direction.UP)&&!b.canMove(Direction.DOWN)&&!b.canMove(Direction.LEFT)&&!b.canMove(Direction.RIGHT)){
				b.display();
				System.out.println("Oh no, you're stuck!\nBetter luck next time!");
				break;
			}
			
		}
		
		//if player used all turns, check for victory
		if (b.getTurnsRemaining() == 0){
			b.display();
			Integer result = b.iterativeCalculate();
			if (result == null){
				System.out.println("Ending with an operator results in an invalid sequence!");
			}
			else{
				System.out.println("Your number result: "+b.iterativeCalculate());
			}
			if (b.onTarget()){
				System.out.println("Good job!");
			}
			else{
				System.out.println("Better luck next time!");
			}
		}

	}

}
