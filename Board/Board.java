package Board;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import Grid.Grid;
import Ships.*;

public class Board extends JPanel implements ActionListener{

	private final String COLS = "ABCDEFGHIJ";
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
	private Grid p1;
	private Grid p2;
	private final Ship[] standardShips = {new AircraftCarrier(), new Battleship(), new Destroyer(), new PatrolBoat(), new Submarine()};
	/**
	 * Create the panel.
	 */
	public Board() {
		initBoard();
	}
	
	public void initBoard(){
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
		
		p1 = new Grid(); //standard grid, creates buttons
		p2 = new Grid(); //standard grid, creates buttons
		
		updateView(mainView, p1);
		updateView(secondView, p2);
		
		turnCounter = 0;
		whichPlayer = 1;
		//changePlayer(); //works, because at first player is '0', so will change it to '1' ACTUALLY DOESN'T WHEN GAME ENDS WITH PLAYER 1
		
		setTurn();
		
		if(setupShips(p1, mainView, standardShips)){
			changePlayer();
		}
		
		if(setupShips(p2, mainView, standardShips)){
			changePlayer();
		}
		
		/*while(ingame){
			play();
		}*/
	}
	
	public void updateView(JPanel view, Grid grid){
		view.removeAll();
		view.add(new JLabel(""));
		for(int i=0; i<COLS.length(); i++){
			view.add(new JLabel(COLS.substring(i, i+1), SwingConstants.CENTER));
		}
		for(int i=0; i<COLS.length(); i++){
			for(int j=0; j<11; j++){
				/*switch(j){
					case(0):
						view.add(new JLabel(Integer.toString(i+1), SwingConstants.CENTER));
					default:
						JButton b = new JButton();
						b.setFocusPainted(false);
						b.setBackground(Color.WHITE);
						b.setBorder(new LineBorder(Color.BLACK, 1));
						b.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								 turnLabel.setText("hey");
							}
						});
						//view.add(b);
						view.add(player.getButton(i, j-1));
				}*/
				if(j == 0){
					view.add(new JLabel(Integer.toString(i+1), SwingConstants.CENTER));
				}
				else{
					view.add(grid.getButton(i, j-1));
				}
			}
		}
	}
	
	public void play(){
		
	}
	
	public boolean setupShips(Grid player, JPanel view, Ship[] ships){ //add mode later, for now just standard mode
		announcementsLabel.setText("Place your ships");
		/*
		 * add actionlisteners
		 */
		return true;
	}
	
	public void changePlayer(){
		whichPlayer = whichPlayer%2 + 1; //neat :>
		whichPlayerLabel.setText("Player: " + whichPlayer);
	}
	
	public void nextTurn(){
		turnCounter++;
		setTurn();
	}
	
	public void setTurn(){
		turnLabel.setText("Turn: " + turnCounter);
	}
	
	public void updateGrid(Grid player, JPanel view){
		view.add(new JLabel(""));
		for(int i=0; i<COLS.length(); i++){
			view.add(new JLabel(COLS.substring(i, i+1), SwingConstants.CENTER));
		}
		for(int i=0; i<COLS.length(); i++){
			for(int j=0; j<10; j++){
				switch(j){
					case(0):
						view.add(new JLabel(Integer.toString(i+1), SwingConstants.CENTER));
					default:
						JButton b = new JButton();
						b.setFocusPainted(false);
						if(player.getValue(i, j-1)){ //j-1 because we start with column 1
							b.setBackground(Color.GREEN);
						}
						else{
							b.setBackground(Color.WHITE);
						}
						b.setBorder(new LineBorder(Color.BLACK, 1));
						view.add(b);
				}
			}
		}
	}
	
	public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_H) {
            position = 'H';
        }

        if (key == KeyEvent.VK_V) {
            position = 'V';
        }
    }
	
	public char getPosition(){
		return position;
	}
	
	@Override
	public void actionPerformed(ActionEvent e){
		
	}
}
