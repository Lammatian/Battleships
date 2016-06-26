package Grid;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.border.LineBorder;

import Ships.*;
import Board.*;

public class Grid{
	
	/*
	 * coordinates will be booleans
	 * true means that there is a ship in a particular cell
	 * false means that there is no ship in a particular cell
	 */
	private boolean[][] coords;
	private JButton[][] buttons;
	private MouseAdapter[][] hover;
	private Ship[] ships = {new AircraftCarrier(), new Battleship(), new Destroyer(), new PatrolBoat(), new Submarine()};
	private int placedShips;
	
	/**
	 * initializing a 10x10 grid (normal size)
	 * at first there are no ships so all cells are 'false'
	 */
	public Grid(){
		
		coords = new boolean[10][];
		buttons = new JButton[10][];
		hover = new MouseAdapter[10][];
		for(int i=0; i<10; i++){
			coords[i] = new boolean[10];
			buttons[i] = new JButton[10];
			hover[i] = new MouseAdapter[10];
		}
		
		for(int i=0; i<10; i++){
			for(int j=0; j<10; j++){
				JButton b = new JButton();
				b.setFocusPainted(false);
				b.setBackground(Color.WHITE);
				b.setBorder(new LineBorder(Color.BLACK, 1));
				buttons[i][j] = b;
				hover[i][j] = mouseEvent(i, j, new AircraftCarrier(), 'H');
			}
		}
		
		addHover();
	}
	
	/**
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
			for(int j=0; j<10; j++){
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
	 * 'enemy' grid, with ones ships that can't be seen
	 */
	public Grid(Grid grid){
		
		coords = new boolean[10][];
		buttons = new JButton[10][];
		hover = new MouseAdapter[10][];
		for(int i=0; i<10; i++){
			coords[i] = new boolean[10];
			for(int j=0; j<10; j++){
				coords[i][j] = grid.getValue(i, j);
			}
			buttons[i] = new JButton[10];
			hover[i] = new MouseAdapter[10];
		}
		
		for(int i=0; i<10; i++){
			for(int j=0; j<10; j++){
				JButton b = new JButton();
				b.setFocusPainted(false);
				b.setBackground(Color.WHITE);
				b.setBorder(new LineBorder(Color.BLACK, 1));
				buttons[i][j] = b;
				hover[i][j] = hover(i,j);
			}
		}
		
		addHover();
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
	 * adds a listener to enable adding a certain ship on the grid
	 */
	public void addPlacingShip(Ship ship, char position){
		for(int i=0; i<buttons.length; i++){
			int tempI = i;
			for(int j=0; j<buttons[0].length; j++){
				int tempJ = j;
				if(buttons[i][j].getActionListeners().length == 3) buttons[i][j].removeActionListener(buttons[i][j].getActionListeners()[2]);
				if(buttons[i][j].getActionListeners().length == 2) buttons[i][j].removeActionListener(buttons[i][j].getActionListeners()[1]);
				buttons[i][j].addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						placeShip(tempI, tempJ, ship, position);
					}
				});
				updateGrid();
				updateHover(ship, position);
			}
		}
	}
	
	/**
	 * adds an action listener to every button
	 * @param a
	 */
	public void addAction(ActionListener a){
		for(int i=0; i<buttons.length; i++){
			for(int j=0; j<buttons[0].length; j++){
				buttons[i][j].addActionListener(a);
			}
		}
	}
	
	/**
	 * removes all action listeners
	 */
	public void removeActions(){
		for(int i=0; i<buttons.length; i++){
			for(int j=0; j<buttons[0].length; j++){
				for(ActionListener a : buttons[i][j].getActionListeners()){
					buttons[i][j].removeActionListener(a);
				}
			}
		}
	}
	
	/**
	 * removes action listeners
	 */
	public void removeActionListeners(JButton b){
		for(ActionListener a : b.getActionListeners()){
			b.removeActionListener(a);
		}
	}
	
	/**
	 * removes mouse listeners
	 * @param b
	 */
	public void removeMouseListeners(JButton b){
		for(MouseListener a : b.getMouseListeners()){
			b.removeMouseListener(a);
		}
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
	 * creates mouse adapter for the hover mouse listener
	 * @param i
	 * @param j
	 * @param ship
	 * @return
	 */
	public MouseAdapter mouseEvent(int x, int y, Ship ship, char position){
		MouseAdapter ma = new MouseAdapter(){
			public void mouseEntered(MouseEvent evt){
				boolean canPlace = true;
				if(position == 'H'){
					/*
					 * checking if the ship is too long to go right
					 */
					if(coords[0].length - y < ship.getLength()){ //i.e. ship is too long
						for(int i=0; i<ship.getLength(); i++){ //checking if any of the ships 'blocks the way'
							if(coords[x][y-i]){ //if a ship is placed to the left
								canPlace = false;
								break;
							}
						}
						if(canPlace){
							for(int i=ship.getLength()-1; i>=0; i--){
								if(!coords[x][y-i]){
									buttons[x][y-i].setBackground(Color.GREEN);
								}
							}
						}
					}
					else if(coords[0].length - y >= ship.getLength()){ //i.e. placing from right to left
						for(int i=0; i<ship.getLength(); i++){ //checking if any of the ships 'blocks the way'
							if(coords[x][y+i]){ //if a ship is placed to the left
								canPlace = false;
								break;
							}
						}
						if(canPlace){
							for(int i=ship.getLength()-1; i>=0; i--){
								if(!coords[x][y+i]){
									buttons[x][y+i].setBackground(Color.GREEN);
								}
							}
						}
					}
				}
				else if(position == 'V'){
					/*
					 * checking if the ship is too long to go down
					 */
					if(coords.length - x < ship.getLength()){ //i.e. ship is too long
						for(int i=0; i<ship.getLength(); i++){ //checking if any of the ships 'blocks the way'
							if(coords[x-i][y]){ //if a ship is placed to the left
								canPlace = false;
								break;
							}
						}
						if(canPlace){
							for(int i=ship.getLength()-1; i>=0; i--){
								if(!coords[x-i][y]){
									buttons[x-i][y].setBackground(Color.GREEN);
								}
							}
						}
					}
					else if(coords[0].length - x >= ship.getLength()){ //i.e. placing from right to left
						for(int i=0; i<ship.getLength(); i++){ //checking if any of the ships 'blocks the way'
							if(coords[x+i][y]){ //if a ship is placed to the left
								canPlace = false;
								break;
							}
						}
						if(canPlace){
							for(int i=ship.getLength()-1; i>=0; i--){
								if(!coords[x+i][y]){
									buttons[x+i][y].setBackground(Color.GREEN);
								}
							}
						}
					}
				}
			}
			public void mouseExited(MouseEvent evt){
				if(position == 'H'){
					/*
					 * checking if the ship is too long to go right
					 */
					if(coords[0].length - y < ship.getLength()){ //i.e. ship is too long
						for(int i=ship.getLength()-1; i>=0; i--){
							if(!coords[x][y-i]){
								buttons[x][y-i].setBackground(Color.WHITE);
							}
						}
					}
					else if(coords[0].length - y >= ship.getLength()){ //i.e. placing from right to left
						for(int i=ship.getLength()-1; i>=0; i--){
							if(!coords[x][y+i]){
								buttons[x][y+i].setBackground(Color.WHITE);
							}
						}
					}
				}
				else if(position == 'V'){
					/*
					 * checking if the ship is too long to go down
					 */
					if(coords.length - x < ship.getLength()){ //i.e. ship is too long
						for(int i=ship.getLength()-1; i>=0; i--){
							if(!coords[x-i][y]){
								buttons[x-i][y].setBackground(Color.WHITE);
							}
						}
					}
					else if(coords[0].length - x >= ship.getLength()){ //i.e. placing from right to left
						for(int i=ship.getLength()-1; i>=0; i--){
							if(!coords[x+i][y]){
								buttons[x+i][y].setBackground(Color.WHITE);
							}
						}
					}
				}
			}
		};
		
		return ma;
	}
	
	/**
	 * hover on every button
	 * @param x
	 * @param y
	 * @return
	 */
	public MouseAdapter hover(int x, int y){
		MouseAdapter ma = new MouseAdapter(){
			public void mouseEntered(MouseEvent evt){
				buttons[x][y].setBackground(new Color(0, 191, 255));
			}
			public void mouseExited(MouseEvent evt){
				buttons[x][y].setBackground(Color.WHITE);
			}
		};
		
		return ma;
	}
	
	/**
	 * updates mouse listener for the hover action
	 */
	public void updateHover(Ship ship, char position){
		for(int i=0; i<hover.length; i++){
			for(int j=0; j<hover[0].length; j++){
				buttons[i][j].removeMouseListener(hover[i][j]);
				hover[i][j] = mouseEvent(i, j, ship, position);
				buttons[i][j].addMouseListener(hover[i][j]);
			}
		}
	}
	
	/**
	 * adds hover to the buttons
	 */
	public void addHover(){
		for(int i=0; i<buttons.length; i++){
			for(int j=0; j<buttons[0].length; j++){
				buttons[i][j].addMouseListener(hover[i][j]);
			}
		}
	}
	
	/**
	 * adds action listeners to enable attacking the enemy
	 */
	public void addAttack(){
		for(int i=0; i<buttons.length; i++){
			int tempI = i;
			for(int j=0; j<buttons[0].length; j++){
				int tempJ = j;
				ActionListener attack = new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						if(coords[tempI][tempJ]){ //there is a ship
							buttons[tempI][tempJ].setBackground(Color.RED);
							Board.hit();
							removeActionListeners(buttons[tempI][tempJ]);
							removeMouseListeners(buttons[tempI][tempJ]);
						}
						else{
							buttons[tempI][tempJ].setBackground(Color.GRAY);
							Board.noHit();
							removeActionListeners(buttons[tempI][tempJ]);
							removeMouseListeners(buttons[tempI][tempJ]);
						}
					}
				};
				buttons[i][j].addActionListener(attack);
			}
		}
	}
	
	/**
	 * ship 'starts' at (x, y) and goes either down (placement = 'V' (i.e. vertical)) or to the right (placement = 'H' (i.e. horizontal))
	 * 
	 * if (x,y) is too low for the ship to go down/right, then it will go up/left
	 * 
	 * add exceptions later (check if the ship is not too long in general)
	 */
	public void placeShip(int x, int y, Ship ship, char position){
		
		boolean canPlace = true;
		boolean placed = false;
		
		if(position == 'H'){
			/*
			 * checking if the ship is too long to go right
			 */
			if(coords[0].length - y < ship.getLength()){ //i.e. ship is too long
				for(int i=0; i<ship.getLength(); i++){ //checking if any of the ships 'blocks the way'
					if(coords[x][y-i]){ //if a ship is placed to the left
						canPlace = false;
						break;
					}
				}
				if(canPlace){
					for(int i=ship.getLength()-1; i>=0; i--){
						coords[x][y-i] = true;
						buttons[x][y-i].setBackground(Color.GREEN);
						removeMouseListeners(buttons[x][y-i]);
					}
					placed = true;
				}
			}
			else if(coords[0].length - y >= ship.getLength()){ //i.e. placing from right to left
				for(int i=0; i<ship.getLength(); i++){ //checking if any of the ships 'blocks the way'
					if(coords[x][y+i]){ //if a ship is placed to the left
						canPlace = false;
						break;
					}
				}
				if(canPlace){
					for(int i=ship.getLength()-1; i>=0; i--){
						coords[x][y+i] = true;
						buttons[x][y+i].setBackground(Color.GREEN);
						removeMouseListeners(buttons[x][y+i]);
					}
					placed = true;
				}
			}
		}
		else if(position == 'V'){
			/*
			 * checking if the ship is too long to go down
			 */
			if(coords.length - x < ship.getLength()){ //i.e. ship is too long
				for(int i=0; i<ship.getLength(); i++){ //checking if any of the ships 'blocks the way'
					if(coords[x-i][y]){ //if a ship is placed to the left
						canPlace = false;
						break;
					}
				}
				if(canPlace){
					for(int i=ship.getLength()-1; i>=0; i--){
						coords[x-i][y] = true;
						buttons[x-i][y].setBackground(Color.GREEN);
						removeMouseListeners(buttons[x-i][y]);
					}
					placed = true;
				}
			}
			else if(coords[0].length - x >= ship.getLength()){ //i.e. placing from right to left
				for(int i=0; i<ship.getLength(); i++){ //checking if any of the ships 'blocks the way'
					if(coords[x+i][y]){ //if a ship is placed to the left
						canPlace = false;
						break;
					}
				}
				if(canPlace){
					for(int i=ship.getLength()-1; i>=0; i--){
						coords[x+i][y] = true;
						buttons[x+i][y].setBackground(Color.GREEN);
						removeMouseListeners(buttons[x+i][y]);
					}
					placed = true;
				}
			}
		}
		if(placed){
			placedShips++;
			System.out.println("Ship placed");
		}
		else{
			System.out.println("Couldn't place the ship");
		}
	}
	
	/**
	 * updated the grid to show actual state
	 */
	public void updateGrid(){
		for(int i=0; i<coords.length; i++){
			for(int j=0; j<coords[0].length; j++){
				if(coords[i][j]){
					buttons[i][j].setBackground(Color.GREEN);
				}
				else{
					buttons[i][j].setBackground(Color.WHITE);
				}
			}
		}
	}
	
	/**
	 * returns how many ships are placed
	 * @return
	 */
	public int howManyShipsPlaced(){
		return placedShips;
	}
	
	/**
	 * checks if all ships are placed
	 */
	public boolean placedAll(){ //TODO - should be in board?
		if(placedShips == ships.length){
			return true;
		}
		else{
			return false;
		}
	}
}