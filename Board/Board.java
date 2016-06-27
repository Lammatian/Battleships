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
	private boolean placing;
	private boolean ingame;
	private int turnCounter;
	private int whichPlayer;
	private char position = 'H';
	private JLabel turnLabel;
	private JLabel lastMoveLabel;
	private JLabel announcementsLabel;
	private JLabel whichPlayerLabel;
	private JPanel mainView;
	private JPanel secondView;
	private Grid[] grids = {new Grid(0), new Grid(), new Grid()}; //grids: neutral, for player 1 and for player 2
	private Grid[] enemyView = {new Grid(0), new Grid(), new Grid()}; //grids how enemy sees them
	private final Ship[] standardShips = {new AircraftCarrier(), new Battleship(), new Destroyer(), new Submarine(), new PatrolBoat()};
	private final Ship[] testShips = {new PatrolBoat()};
	private Ship[] usedShips;
	private String lastMove;
	
	/**
	 * Create the panel.
	 */
	public Board() {
		initBoard();
	}
	
	public void initBoard(){
		
		requestFocusInWindow();
		getInputMap(WHEN_IN_FOCUSED_WINDOW);
		//keyBinding();
		setLayout(null);
		
		Action newGameAction = new AbstractAction("New game"){

			@Override
            public void actionPerformed(ActionEvent e) {
                setupNewGame();
                usedShips = testShips;
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
		turnLabel.setFont(new Font("Tahoma", Font.PLAIN, 48));
		turnLabel.setBounds(247, 79, 304, 54);
		add(turnLabel);
		
		lastMoveLabel = new JLabel("Last move");
		lastMoveLabel.setFont(new Font("Tahoma", Font.PLAIN, 48));
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
	
	public void nextTurn(){
		turnCounter++;
		setTurn();
	}
	
	public void setTurn(){
		turnLabel.setText("Turn: " + turnCounter);
	}
	
	public void setPlayer(){
		whichPlayerLabel.setText("Player " + whichPlayer);
	}
	
	public void setAnnouncement(String message){
		announcementsLabel.setText(message);
	}
	
	public void updateLastMove(){
		lastMove = enemyView[whichPlayer].getLastPosition();
		setLastMove();
	}
	
	public void setLastMove(){
		lastMoveLabel.setText("Last move: " + lastMove);
	}
	
	public static void showInformation(String message){
		JOptionPane.showMessageDialog(null, message);
	}
	
	public void endGame(){
		showInformation("Player " + whichPlayer + " wins!");
		ingame = false;
		startView();
	}
	
	public char getPosition(){
		return position;
	}
	
	public void startView(){
		grids[1] = new Grid();
		grids[2] = new Grid();
		
		turnCounter = 0;
		setTurn();
		whichPlayer = 1;
		setPlayer();
		
		updateView(mainView, new Grid(0));
		updateView(secondView, new Grid(0));
		
		setAnnouncement("Press \"New game\" to start");
	}
	
	public void setupNewGame(){
		
		startView();
		
		grids[1] = new Grid();
		grids[2] = new Grid();
		
		addBinding(mainView);
		addBinding(secondView);
		
		turnCounter = 0;
		whichPlayer = 1;
		
		setPlayer();
		setTurn();
		
		//setupShips(grids[whichPlayer], mainView, standardShips);
		setupShips(grids[whichPlayer], mainView, testShips);
		
		mainView.requestFocus();
	}
	
	public void play(){
		ingame = true;
		setAnnouncement("");
		
		setupAttack();
		
		changePlayer();
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
		setAnnouncement("Place your ships");
		showInformation("Place your ships");
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
				showInformation("Ships placed, game starts now");
				placing = false; //ended placing ships
				play();
			}
			else{
				player.removeActions();
				showInformation("Second player");
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
		setAnnouncement("Place " + ships[player.howManyShipsPlaced()].toString() + ", size " + ships[player.howManyShipsPlaced()].getLength());
	}
	
	/**
	 * initializes and creates enemy views for the game
	 */
	public void setupAttack(){
		enemyView[1] = new Grid(grids[2]); //how Player 1 sees Player 2
		//this works strange
		//you have to add first the action
		//which you want the button to perform last
		enemyView[1].addAction(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				changePlayer();
			}
		});
		enemyView[1].addAction(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				updateLastMove();
			}
		});
		enemyView[1].addAttack();
		
		enemyView[2] = new Grid(grids[1]); //how Player 2 sees Player 1
		enemyView[2].addAction(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				changePlayer();
			}
		});
		enemyView[2].addAction(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				updateLastMove();
			}
		});
		enemyView[2].addAttack();
	}
	
	public static void hit(){
		showInformation("Hit!");
	}
	
	public static void noHit(){
		showInformation("No hit");
	}
	
	public void changePlayer(){
		if(ingame && enemyView[whichPlayer].destroyedAll()){
			endGame();
		}
		else{
			if(ingame && whichPlayer == 2){
				nextTurn();
			}
			whichPlayer = whichPlayer%2 + 1; //neat :>
			setPlayer();
			if(placing){
				updateView(mainView, grids[whichPlayer]);
			}
			else if(ingame){
				showInformation("Player " + whichPlayer);
				setAnnouncement("Choose a place to attack in");
				updateView(mainView, enemyView[whichPlayer]);
				updateView(secondView, grids[whichPlayer]);
				System.out.println(lastMove);
			}
		}
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
	
	@Override
	public void actionPerformed(ActionEvent e){}
	
	public void keyBinding(){
		Action vertical = new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e){
				System.out.println("pos changed");
				position = 'V';
				if(placing){
					nextShip(grids[whichPlayer], mainView, usedShips);
				}
			}
		};
		Action horizontal = new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e){
				System.out.println("pos changed");
				position = 'H';
				if(placing){
					nextShip(grids[whichPlayer], mainView, usedShips);
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
					nextShip(grids[whichPlayer], mainView, usedShips);
				}
			}
		};
		Action horizontal = new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e){
				System.out.println("pos changed");
				position = 'H';
				if(placing){
					nextShip(grids[whichPlayer], mainView, usedShips);
				}
			}
		};
		panel.getInputMap().put(KeyStroke.getKeyStroke("V"), "vertical");
		panel.getInputMap().put(KeyStroke.getKeyStroke("H"), "horizontal");
		panel.getActionMap().put("vertical", vertical);
		panel.getActionMap().put("horizontal", horizontal);
	}
}
