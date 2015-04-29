package com.GutierrezLeo.CIS81B.GroupProject.SpaceInvaders;

import javax.swing.JFrame;

public class TestHighScore {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		HighScore_Frame highScore_Frame = new HighScore_Frame();
		highScore_Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		highScore_Frame.setDefaultLookAndFeelDecorated(true);
		highScore_Frame.setSize(500,700);
		//highScore_Frame.pack();
		
		//This line below is used to test the score screen.  Enter a value greater than the
		// lowest value in the top 10 scores.  If you enter a value that is lower, then 
		// the top 10 scores will just display and you will not be asked to enter any
		// initials.
		highScore_Frame.addScore("   ", 113000);
		highScore_Frame.setVisible(true);
		
		
	}

}
