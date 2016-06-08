package Grid;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.border.LineBorder;

import Ships.*;

public class Grid{
	
	/*
	 * coordinates will be booleans
	 * true means that there is a ship in a particular cell
	 * false means that there is no ship in a particular cell
	 */
	private boolean[][] coords;
	private JButton[][] buttons;
	/**
	 * initializing a 10x10 grid (normal size)
	 * at first there are no ships so all cells are 'false'
	 */
	public Grid(){
		
		coords = new boolean[10][];
		buttons = new JButton[10][];
		for(int i=0; i<10; i++){
			coords[i] = new boolean[10];
			buttons[i] = new JButton[10];
		}
		
		for(int i=0; i<10; i++){
			int tempI = i;
			for(int j=0; j<10; j++){
				int tempJ = j;
				JButton b = new JButton();
				b.setFocusPainted(false);
				b.setBackground(Color.WHITE);
				b.setBorder(new LineBorder(Color.BLACK, 1));
				b.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						placeShip(tempI, tempJ, new AircraftCarrier(), 'H');
					}
				});
				buttons[i][j] = b;
			}
		}
	}
	
	/*
	 * constructor for a grid with no actionlisteners
	 */
	public Grid(int x){
		
		coords = new boolean[10][];
		buttons = new JButton[10][];
		for(int i=0; i<10; i++){
			coords[i] = new boolean[10];
			buttons[i] = new JButton[10];
		}
		
		for(int i=0; i<10; i++){
			int tempI = i;
			for(int j=0; j<10; j++){
				int tempJ = j;
				JButton b = new JButton();
				b.setFocusPainted(false);
				b.setBackground(Color.WHITE);
				b.setBorder(new LineBorder(Color.BLACK, 1));
				buttons[i][j] = b;
			}
		}
	}
	
	/**
	 * initializing a grid with user's parameters for width and height
	 * again, all cells are 'false' since there are no ships
	 * 
	 * add exceptions later
	 */
	public Grid(int rows, int columns){
		
		coords = new boolean[rows][];
		for(int i=0; i<rows; i++){
			coords[i] = new boolean[columns];
		}
	}
	
	/**
	 * set value (true/false) of a particular cell
	 * 
	 * add exceptions later
	 */
	public void setValue(int row, int column, boolean value){
		coords[row][column] = value;
	}
	
	/**
	 * returns a value (true/false) of the particular cell
	 * 
	 * add exceptions later
	 */
	public boolean getValue(int row, int column){
		return coords[row][column];
	}
	
	/**
	 * returns all buttons for this grid
	 * @return
	 */
	public JButton[][] getButtons(){
		return buttons;
	}
	
	/**
	 * returns specific button from this grid
	 * @param row
	 * @param column
	 * @return
	 */
	public JButton getButton(int row, int column){
		return buttons[row][column];
	}
	
	/**
	 * ship 'starts' at (x, y) and goes either down (placement = 'V' (i.e. vertical)) or to the right (placement = 'H' (i.e. horizontal))
	 * 
	 * if (x,y) is too low for the ship to go down/right, then it will go up/left
	 * 
	 * add exceptions later (check if the ship is not too long in general)
	 */
	public void placeShip(int x, int y, Ship ship, char position){
		
		if(position == 'H'){
			/*
			 * checking if the ship is too long to go right
			 */
			if(coords.length - x < ship.getLength()){ //i.e. ship is too long
				for(int i=ship.getLength()-1; i>=0; i--){
					coords[x-i][y] = true;
					buttons[x-i][y].setBackground(Color.GREEN);
					buttons[x-i][y].removeActionListener(buttons[x+i][y].getActionListeners()[0]); //hope it works
				}
			}
			else{ //i.e. placing from right to left
				for(int i=ship.getLength()-1; i>=0; i--){
					coords[x+i][y] = true;
					buttons[x+i][y].setBackground(Color.GREEN);
					buttons[x+i][y].removeActionListener(buttons[x+i][y].getActionListeners()[0]); //hope it works
				}
			}
		}
		else if(position == 'V'){
			/*
			 * checking if the ship is too long to go down
			 */
			if(coords[0].length - y < ship.getLength()){ //i.e. ship is too long
				for(int i=ship.getLength()-1; i>=0; i--){
					coords[x][y-i] = true;
					buttons[x][y-i].setBackground(Color.GREEN);
					buttons[x][y-i].removeActionListener(buttons[x+i][y].getActionListeners()[0]); //hope it works
				}
			}
			else{ //i.e. placing from right to left
				for(int i=ship.getLength()-1; i>=0; i--){
					coords[x][y+i] = true;
					buttons[x][y+i].setBackground(Color.GREEN);
					buttons[x][y+i].removeActionListener(buttons[x+i][y].getActionListeners()[0]); //hope it works
				}
			}
		}
	}
}