package Grid;

import Ships.Ship;

public class Grid{
	
	/*
	 * coordinates will be booleans
	 * true means that there is a ship in a particular cell
	 * false means that there is no ship in a particular cell
	 */
	private boolean[][] coords;
	
	public Grid(){
		/*
		 * initializing a 10x10 grid (normal size)
		 * at first there are no ships so all cells are 'false'
		 */
		coords = new boolean[10][];
		for(int i=0; i<10; i++){
			coords[i] = new boolean[10];
		}
	}
	
	public Grid(int rows, int columns){
		/*
		 * initializing a grid with user's parameters for width and height
		 * again, all cells are 'false' since there are no ships
		 * 
		 * add exceptions later
		 */
		coords = new boolean[rows][];
		for(int i=0; i<rows; i++){
			coords[i] = new boolean[columns];
		}
	}
	
	public void setValue(int row, int column, boolean value){
		/*
		 * set value (true/false) of a particular cell
		 * 
		 * add exceptions later
		 */
		coords[row][column] = value;
	}
	
	public boolean getValue(int row, int column){
		/*
		 * returns a value (true/false) of the particular cell
		 * 
		 * add exceptions later
		 */
		return coords[row][column];
	}
	
	public void placeShip(int x, int y, Ship ship, char placement){
		/*
		 * ship 'starts' at (x, y) and goes either down (placement = 'V' (i.e. vertical)) or to the right (placement = 'H' (i.e. horizontal))
		 * 
		 * if (x,y) is too low for the ship to go down/right, then it will go up/left
		 * 
		 * add exceptions later
		 */
		
	}
}