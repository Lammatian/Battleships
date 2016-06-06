package Board;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import Grid.Grid;
import java.awt.Font;

public class Board extends JPanel implements ActionListener{

	private final String COLS = "ABCDEFGHIJ";
	private boolean ingame;
	private int turnCounter;
	private int whichPlayer;
	private JLabel turnLabel;
	private JLabel lastMoveLabel;
	private JLabel announcementsLabel;
	private JLabel whichPlayerLabel;
	private Grid p1;
	private Grid p2;
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
		JPanel mainView = new JPanel(new GridLayout(11, 11));
		mainView.setBounds(204, 224, 385, 385);
		mainView.setBorder(new LineBorder(Color.BLACK, 2));
		add(mainView);
		
		//the small grid on the screen
		JPanel secondView = new JPanel(new GridLayout(11, 11));
		secondView.setBounds(10, 27, 186, 186);
		secondView.setBorder(new LineBorder(Color.BLACK, 2));
		add(secondView);
		
		fillInView(mainView);
		fillInView(secondView);
		
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
	
	public void fillInView(JPanel view){
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
						b.setBackground(Color.WHITE);
						b.setBorder(new LineBorder(Color.BLACK, 1));
						view.add(b);
				}
			}
		}
	}
	
	public void setupNewGame(){
		ingame = true;
		
		p1 = new Grid(); //standard grid
		p2 = new Grid(); //standard grid
		
		turnCounter = 0;
		whichPlayer = 1;
		whichPlayerLabel.setText("Player: " + whichPlayer);
		
		setTurn();
		
		if(setupShips(p1)){
			changePlayer();
		}
		
		if(setupShips(p2)){
			changePlayer();
		}
		
		/*while(ingame){
			play();
		}*/
	}
	
	public void play(){
		
	}
	
	public boolean setupShips(Grid player){ //add mode later, for now just standard mode
		announcementsLabel.setText("Place your ships");
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
	
	@Override
	public void actionPerformed(ActionEvent e){
		
	}
}
