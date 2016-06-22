package Board;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import Grid.Grid;
import Ships.*;

public class Board extends JPanel implements ActionListener{

	private final String COLS = "ABCDEFGHIJ";
	private boolean ingame;
	private boolean placing;
	private int turnCounter;
	private int whichPlayer;
	private char position = 'H';
	private JLabel turnLabel;
	private JLabel lastMoveLabel;
	private JLabel announcementsLabel;
	private JLabel whichPlayerLabel;
	private JPanel mainView;
	private JPanel secondView;
	private Grid[] grids = {new Grid(0), new Grid(), new Grid()};
	private final Ship[] standardShips = {new AircraftCarrier(), new Battleship(), new Destroyer(), new Submarine(), new PatrolBoat()};
	
	/**
	 * Create the panel.
	 */
	public Board() {
		initBoard();
	}
	
	public void initBoard(){
		requestFocusInWindow();
		getInputMap(WHEN_IN_FOCUSED_WINDOW);
		keyBinding();
		setLayout(null);
		
		ingame = false;
		
		Action newGameAction = new AbstractAction("New game"){
            @Override
            public void actionPerformed(ActionEvent e) {
                setupNewGame();
            }
		};
		
		//creating menu
		JToolBar menu = new JToolBar();
		menu.setFloatable(false);
		menu.setBounds(0, 0, 620, 23);
		menu.add(newGameAction);
		menu.addSeparator();
		menu.add(new JButton("Swap views")); //TODO - add functionality
		menu.addSeparator();
		menu.add(new JButton("Undo")); //TODO - add functionality
		add(menu);
		
		//the big grid on the screen
		mainView = new JPanel(new GridLayout(11, 11));
		mainView.setBounds(204, 224, 385, 385);
		mainView.setBorder(new LineBorder(Color.BLACK, 2));
		add(mainView);
		
		//the small grid on the screen
		secondView = new JPanel(new GridLayout(11, 11));
		secondView.setBounds(10, 27, 186, 186);
		secondView.setBorder(new LineBorder(Color.BLACK, 2));
		add(secondView);
		
		updateView(secondView, new Grid(0)); 
		updateView(mainView, new Grid(0));
		
		turnLabel = new JLabel("Turn");
		turnLabel.setHorizontalAlignment(SwingConstants.CENTER);
		turnLabel.setFont(new Font("Tahoma", Font.PLAIN, 50));
		turnLabel.setBounds(247, 79, 304, 54);
		add(turnLabel);
		
		lastMoveLabel = new JLabel("Last move");
		lastMoveLabel.setFont(new Font("Tahoma", Font.PLAIN, 50));
		lastMoveLabel.setBounds(230, 144, 338, 54);
		add(lastMoveLabel);
		
		announcementsLabel = new JLabel("Announcements");
		announcementsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		announcementsLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		announcementsLabel.setBounds(312, 34, 256, 34);
		add(announcementsLabel);
		
		whichPlayerLabel = new JLabel("Player");
		whichPlayerLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		whichPlayerLabel.setBounds(230, 34, 72, 34);
		add(whichPlayerLabel);
	}
	
	public void setupNewGame(){
		ingame = true;
		
		grids[1] = new Grid();
		grids[2] = new Grid();
		
		addBinding(mainView);
		addBinding(secondView);
		
		turnCounter = 0;
		whichPlayer = 1;
		
		whichPlayerLabel.setText("Player: " + whichPlayer);
		
		setTurn();
		
		setupShips(grids[whichPlayer], mainView, standardShips);
		
		mainView.requestFocus();
	}
	
	public void play(){
		nextTurn();
		updateView(mainView, grids[0]);
		JOptionPane.showMessageDialog(null, "Player " + whichPlayer);
		updateView(mainView, grids[whichPlayer]);
		
	}
	
	/**
	 * starts a ship placement operation
	 * @param player
	 * @param view
	 * @param ships
	 */
	public void setupShips(Grid player, JPanel view, Ship[] ships){ //add mode later, for now just standard mode
		placing = true;
		view.requestFocus();
		announcementsLabel.setText("Place your ships");
		JOptionPane.showMessageDialog(null, "Place your ships");
		updateShipPlacementAnnouncement(player, ships);
		nextShip(player, view, ships);
	}
	
	/**
	 * invokes placing next ships
	 */
	public void nextShip(Grid player, JPanel view, Ship[] ships){
		if(!player.placedAll()){
			player.addAction(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					nextShip(player, view, ships);
				}
			});
			player.addPlacingShip(ships[player.howManyShipsPlaced()], getPosition());
			updateShipPlacementAnnouncement(player, ships);
			updateView(view, player);
		}
		else{
			if(whichPlayer == 2){
				player.removeActions();
				JOptionPane.showMessageDialog(null, "Ships placed, game starts now");
				changePlayer();
				placing = false; //ended placing ships
				ingame = true;
				play();
			}
			else{
				player.removeActions();
				JOptionPane.showMessageDialog(null, "Second player");
				changePlayer();
				setupShips(grids[whichPlayer], mainView, ships);
			}
		}
	}
	
	/**
	 * updates the announcement label to say which ship will be placed now
	 * @param player
	 * @param ships
	 */
	public void updateShipPlacementAnnouncement(Grid player, Ship[] ships){
		announcementsLabel.setText("Place " + ships[player.howManyShipsPlaced()].toString() + ", size " + ships[player.howManyShipsPlaced()].getLength());
	}
	
	public void changePlayer(){
		whichPlayer = whichPlayer%2 + 1; //neat :>
		whichPlayerLabel.setText("Player: " + whichPlayer);
		updateView(mainView, grids[whichPlayer]);
	}
	
	public void nextTurn(){
		turnCounter++;
		setTurn();
	}
	
	public void setTurn(){
		turnLabel.setText("Turn: " + turnCounter);
	}
	
	/**
	 * updates the JPanel to show certain grid
	 */
	public void updateView(JPanel view, Grid grid){
		view.removeAll();
		view.add(new JLabel(""));
		for(int i=0; i<COLS.length(); i++){
			view.add(new JLabel(COLS.substring(i, i+1), SwingConstants.CENTER));
		}
		for(int i=0; i<COLS.length(); i++){
			for(int j=0; j<11; j++){
				if(j == 0){
					view.add(new JLabel(Integer.toString(i+1), SwingConstants.CENTER));
				}
				else{
					view.add(grid.getButton(i, j-1));
				}
			}
		}
		view.updateUI();
	}
	
	public char getPosition(){
		return position;
	}
	
	@Override
	public void actionPerformed(ActionEvent e){
		
	}
	
	public void keyBinding(){
		Action vertical = new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e){
				System.out.println("pos changed");
				position = 'V';
				if(placing){
					nextShip(grids[whichPlayer], mainView, standardShips);
				}
			}
		};
		Action horizontal = new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e){
				System.out.println("pos changed");
				position = 'H';
				if(placing){
					nextShip(grids[whichPlayer], mainView, standardShips);
				}
			}
		};
		getInputMap().put(KeyStroke.getKeyStroke("V"), "vertical");
		getInputMap().put(KeyStroke.getKeyStroke("H"), "horizontal");
		getActionMap().put("vertical", vertical);
		getActionMap().put("horizontal", horizontal);
	}
	
	public void addBinding(JPanel panel){
		Action vertical = new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e){
				System.out.println("pos changed");
				position = 'V';
				if(placing){
					nextShip(grids[whichPlayer], mainView, standardShips);
				}
			}
		};
		Action horizontal = new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e){
				System.out.println("pos changed");
				position = 'H';
				if(placing){
					nextShip(grids[whichPlayer], mainView, standardShips);
				}
			}
		};
		panel.getInputMap().put(KeyStroke.getKeyStroke("V"), "vertical");
		panel.getInputMap().put(KeyStroke.getKeyStroke("H"), "horizontal");
		panel.getActionMap().put("vertical", vertical);
		panel.getActionMap().put("horizontal", horizontal);
	}
}
