package com.GutierrezLeo.CIS81B.GroupProject.SpaceInvaders;

import javax.swing.JFrame;

public class TestHighScore {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		HighScore_Frame highScore_Frame = new HighScore_Frame();
		highScore_Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		highScore_Frame.setDefaultLookAndFeelDecorated(true);
		highScore_Frame.setSize(300,600);
		highScore_Frame.pack();
		highScore_Frame.setVisible(true);
	}

}
