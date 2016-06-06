package Board;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;

public class Battleships extends JFrame{

	/**
	 * Launch the application.
	 */
	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable(){
			public void run() {
				try {
					Battleships frame = new Battleships();
					frame.setMinimumSize(frame.getSize());
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Battleships(){
		add(new Board());
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setTitle("Battleships");
		
		Dimension minimumSize = new Dimension(610, 650);
		setMinimumSize(minimumSize);
		
		pack();
		setResizable(false);
	
	}

}
