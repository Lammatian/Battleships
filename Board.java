package Board;

import Grid.Grid;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class Board extends JPanel implements ActionListener{

	private final String COLS = "ABCDEFGHIJ";
	/**
	 * Create the panel.
	 */
	public Board() {
		initBoard();
	}
	
	public void initBoard(){
		setLayout(null);
		
		JPanel mainView = new JPanel(new GridLayout(11, 11));
		mainView.setBounds(204, 224, 385, 385);
		//mainView.setBackground(new Color(204,119,34));
		mainView.setBorder(new LineBorder(Color.BLACK, 2));
		add(mainView);
		
		JPanel secondView = new JPanel(new GridLayout(11, 11));
		secondView.setBounds(10, 27, 186, 186);
		//secondView.setBackground(new Color(204,119,34));
		secondView.setBorder(new LineBorder(Color.BLACK, 2));
		add(secondView);
		
		JToolBar menu = new JToolBar();
		menu.setFloatable(false);
		menu.setBounds(0, 0, 620, 23);
		menu.add(new JButton("New game")); //TODO - add functionality
		menu.addSeparator();
		menu.add(new JButton("Restart")); //TODO - add functionality
		menu.addSeparator();
		menu.add(new JButton("Swap views")); //TODO - add functionality
		menu.addSeparator();
		menu.add(new JButton("Undo")); //TODO - add functionality
		add(menu);
		
		fillInView(mainView);
		fillInView(secondView);
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
	
	@Override
	public void actionPerformed(ActionEvent e){
		
	}
}
