
package com.GutierrezLeo.CIS81B.GroupProject.SpaceInvaders;

import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class HighScore_Frame extends JFrame implements ActionListener{

	// Define labels, panels and menubar with items
	private JLabel headlbl;
	private JLabel scorelbl1, scorelbl2, scorelbl3, scorelbl4, scorelbl5, scorelbl6,
				   scorelbl7, scorelbl8, scorelbl9, scorelbl10;
	private JLabel playerlbl1, playerlbl2, playerlbl3, playerlbl4, playerlbl5,
			       playerlbl6, playerlbl7, playerlbl8, playerlbl9, playerlbl10;
	private JPanel panel1, panel2, panel3, panel4, panel5,
				   panel6, panel7, panel8, panel9, panel10, headPanel, buttonPanel;
	private JMenuBar menuBar;
	private JMenu helpMenu, aboutMenu;
	private JMenuItem instructionMenuItem, authorsMenuItem;
	private JButton exitButton, playButton;
	
	private ArrayList<TopScores> scores = new ArrayList<TopScores>();
	private static final String ScoreFile = "scores.dat";
	
	private String holdInitial;
	private int holdScore;
	private int k;
	private int lowScore;
	private int highScore;
	
	FileInputStream fileInput = null;
	ObjectInputStream inputStream = null;
	FileOutputStream fileOutput = null;
	ObjectOutputStream outputStream = null;
	
	// High Score Frame Constructor
	public HighScore_Frame(){
		
		super("HIGH SCORES");
		
		getScores();
		getHighScores();
		this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		this.getContentPane().setBackground(Color.black);
		loadMenus();
		loadHeader();
		loadPanels();
		loadButtonPanel();
		//addScore("   ", 20000);
	}
	
	// Load header method to load the Bold Header
	public void loadHeader(){
		//scoresList = new ArrayList<TopScores>();
		
		headlbl = new JLabel("HIGH SCORES");
		headlbl.setFont(new Font("Impact", Font.BOLD, 40));
		headlbl.setForeground(Color.white);
		headPanel = new JPanel();
		headPanel.setLayout(new FlowLayout());
		headPanel.setAlignmentX(CENTER_ALIGNMENT);
		headPanel.setBackground(Color.black);
		headPanel.add(headlbl);
		this.add(headPanel);

	}
	
	// Declare all labels with default values for the moment.
	public void loadPanels(){
		
		
		k = 0;
		scorelbl1 = new JLabel();
		setFontColors(scorelbl1);
		playerlbl1 = new JLabel();
		setFontColors(playerlbl1);
		loadFileInfo(playerlbl1, scorelbl1);
		
		k = 1;
		scorelbl2 = new JLabel();
		setFontColors(scorelbl2);
		playerlbl2 = new JLabel();
		setFontColors(playerlbl2);
		loadFileInfo(playerlbl2, scorelbl2);
		
		k = 2;
		scorelbl3 = new JLabel();
		setFontColors(scorelbl3);
		playerlbl3 = new JLabel();
		setFontColors(playerlbl3);
		loadFileInfo(playerlbl3, scorelbl3);
		
		k = 3;
		scorelbl4 = new JLabel();
		setFontColors(scorelbl4);
		playerlbl4 = new JLabel();
		setFontColors(playerlbl4);
		loadFileInfo(playerlbl4, scorelbl4);
		
		k = 4;
		scorelbl5 = new JLabel();
		setFontColors(scorelbl5);
		playerlbl5 = new JLabel();
		setFontColors(playerlbl5);
		loadFileInfo(playerlbl5, scorelbl5);
		
		k = 5;
		scorelbl6 = new JLabel();
		setFontColors(scorelbl6);
		playerlbl6 = new JLabel();
		setFontColors(playerlbl6);
		loadFileInfo(playerlbl6, scorelbl6);
		
		k = 6;
		scorelbl7 = new JLabel();
		setFontColors(scorelbl7);
		playerlbl7 = new JLabel();
		setFontColors(playerlbl7);
		loadFileInfo(playerlbl7, scorelbl7);
		
		k = 7;
		scorelbl8 = new JLabel();
		setFontColors(scorelbl8);
		playerlbl8 = new JLabel();
		setFontColors(playerlbl8);
		loadFileInfo(playerlbl8, scorelbl8);
		
		k = 8;
		scorelbl9 = new JLabel();
		setFontColors(scorelbl9);
		playerlbl9 = new JLabel();
		setFontColors(playerlbl9);
		loadFileInfo(playerlbl9, scorelbl9);
		
		k = 9;
		scorelbl10 = new JLabel();
		setFontColors(scorelbl10);
		playerlbl10 = new JLabel();
		setFontColors(playerlbl10);
		loadFileInfo(playerlbl10, scorelbl10);
		
		// Declare all panels for the top players and scores.  Call the careteAPanel method to create
		//   each panel
		panel1 = new JPanel();
		createAPanel(panel1, playerlbl1, scorelbl1);
		panel2 = new JPanel();
		createAPanel(panel2, playerlbl2, scorelbl2);
		panel3 = new JPanel();
		createAPanel(panel3, playerlbl3, scorelbl3);
		panel4 = new JPanel();
		createAPanel(panel4, playerlbl4, scorelbl4);
		panel5 = new JPanel();
		createAPanel(panel5, playerlbl5, scorelbl5);
		panel6 = new JPanel();
		createAPanel(panel6, playerlbl6, scorelbl6);
		panel7 = new JPanel();
		createAPanel(panel7, playerlbl7, scorelbl7);
		panel8 = new JPanel();
		createAPanel(panel8, playerlbl8, scorelbl8);
		panel9 = new JPanel();
		createAPanel(panel9, playerlbl9, scorelbl9);
		panel10 = new JPanel();
		createAPanel(panel10, playerlbl10, scorelbl10);

		int sizeTopScores = scores.size();
		System.out.println("The size of the TopScores array is: " + sizeTopScores);

	}

	public void loadFileInfo(JLabel playerLabel, JLabel scoreLabel){
		
		if (scores.get(k).getInitials() == null){
			holdInitial = "";
			holdScore = 0;
		} else {
			holdInitial = scores.get(k).getInitials();
			holdScore = scores.get(k).getScore();
		}
		playerLabel.setText(holdInitial);
		scoreLabel.setText(Integer.toString(holdScore));
	}
	
	
	public void setFontColors(JLabel lblName){
		
		lblName.setFont(new Font("Courier", Font.BOLD, 20));
		lblName.setForeground(Color.WHITE);
		
	}
	
	// Create a panel from data passed to it from the calling method.  The name of the panel, the 
	//   player and score are required for each one.
	public void createAPanel(JPanel panelName, JLabel playerLabel, JLabel scoreLabel){
		
		panelName.setLayout(new FlowLayout());
		panelName.add(playerLabel);
		panelName.add(scoreLabel);
		panelName.setAlignmentX(CENTER_ALIGNMENT);
		panelName.setBackground(Color.black);
		this.add(panelName);
	}
	
	// Create the menu bar and menu items.
	public void loadMenus(){
		
		// Create a menu bar for the High Score frame
		menuBar = new JMenuBar();
		
		// Add the menu bar to the High Score frame
		setJMenuBar(menuBar);
		
		//Define and add the two drop down menus to the menu bar
		JMenu helpMenu = new JMenu("Help");
		JMenu aboutMenu = new JMenu("About");
		menuBar.add(helpMenu);
		menuBar.add(aboutMenu);
		
		// Create and add menu items to the drop down menu
		instructionMenuItem = new JMenuItem("Instructions");
		authorsMenuItem = new JMenuItem("Authors");
		helpMenu.add(instructionMenuItem);
		aboutMenu.add(authorsMenuItem);
		instructionMenuItem.addActionListener(this);
		authorsMenuItem.addActionListener(this);
	}
	
	public void loadButtonPanel(){
		
		buttonPanel = new JPanel();
		playButton = new JButton("Play Again");
		exitButton = new JButton("Exit");
		buttonPanel.add(playButton);
		buttonPanel.add(exitButton);
		buttonPanel.setBackground(Color.BLACK);
		this.add(buttonPanel);
		playButton.addActionListener(this);
		exitButton.addActionListener(this);
		
	}
	
	public ArrayList<TopScores> getScores(){
		
		loadScoreFile();
		sort();
		return scores;
	}
	
	public void sort(){
		VerifyScore verify = new VerifyScore();
		Collections.sort(scores, verify);
		
		System.out.println("You are in the sort method");
	}
	
	public void addScore(String initials, int score){
		String myInitials = "   ";
		lowScore = scores.get(9).getScore();
		highScore = scores.get(0).getScore();
		
		System.out.println("lowScore is: " + lowScore);
		System.out.println("highScore is: " + highScore);
		System.out.println("score is: " + score);
		
		if(score > lowScore){
			myInitials = JOptionPane.showInputDialog("Your score is " + score + 
					". Please enter your initials:");
		} else {
			myInitials = "   ";
		}

		initials = myInitials;
		//loadScoreFile();
		scores.add(new TopScores(initials, score));
		updateScoreFile();
		loadScoreFile();
		getHighScores();
		reloadScores();
	}
	
	public void reloadScores(){
		
		k=0;
		loadFileInfo(playerlbl1, scorelbl1);
		k=1;
		loadFileInfo(playerlbl2, scorelbl2);
		k=2;
		loadFileInfo(playerlbl3, scorelbl3);
		k=3;
		loadFileInfo(playerlbl4, scorelbl4);
		k=4;
		loadFileInfo(playerlbl5, scorelbl5);
		k=5;
		loadFileInfo(playerlbl6, scorelbl6);
		k=6;
		loadFileInfo(playerlbl7, scorelbl7);
		k=7;
		loadFileInfo(playerlbl8, scorelbl8);
		k=8;
		loadFileInfo(playerlbl9, scorelbl9);
		k=9;
		loadFileInfo(playerlbl10, scorelbl10);
		
	}
	
	public void loadScoreFile(){
		
		System.out.println("You are in the loadScoreFile method");
		
		try
		{
			fileInput = new FileInputStream(ScoreFile);
			inputStream = new ObjectInputStream(fileInput);
			scores = (ArrayList<TopScores>) inputStream.readObject();
		}
		catch (FileNotFoundException e)
		{
			System.err.println("fileNot Found on loadScoreFile method");
		}
		catch (IOException e)
		{
			System.err.println("IO Error on loadScoreFile method");
		}
		catch (ClassNotFoundException e)
		{
			System.err.println("Class Not Found on loadScoreFile method");
		}
		
		finally
		{
			try
			{
				inputStream.close();
				fileInput.close();
			}
			catch (IOException e)
			{
				System.err.println("IO Error on closing input files");
			}
		}
		
	}
	
	public void updateScoreFile(){
		
		System.out.println("You are in the updateScoreFile method");
		
		try
		{
			fileOutput = new FileOutputStream(ScoreFile);
			outputStream = new ObjectOutputStream(fileOutput);
			outputStream.writeObject(scores);
		}
		catch (FileNotFoundException e)
		{
			System.err.println("File Not Found on updateScoreFile method");
		}
		catch (IOException e)
		{
			System.err.println("IO Error on updateScoreFile method");
		}
		
		finally
		{
			try
			{
				outputStream.close();
				fileOutput.close();
			}
			catch (IOException e)
			{
				System.err.println("Error on closing output files");
			}
		}
		
	}
	
	public String getHighScores(){
		
		System.out.println("You are in the getHighScores method");
		
		String scoreString = "";
		int max = 10;
		
		ArrayList<TopScores> scores;
		scores = getScores();
		
		int i = 0;
		int x = scores.size();
		if (x > max) {
			x = max;
		}
		
		while (i < x){
			scoreString += (i + 1) + "./t" + scores.get(i).getInitials()
					+ "/t/t" + scores.get(i).getScore() + "/n";
			i++;
		}
		
		return scoreString;
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == playButton){
			System.out.println("You have clicked the Play Button");
		}
		
		if(e.getSource() == exitButton){
			System.exit(0);
		}
		
		if(e.getSource() == instructionMenuItem){
			//System.out.println("You have selected the Instructions menu item");
			
			JOptionPane.showMessageDialog(this,"Scoring:\n 10 Front row  \n20 Second row  \n30 Third Row"
					+ "\nSpaceShip is ????   \nObject of the game to hit all the aliens before you run"
					+ "out of lives.  \nPlay as long as possible to get the high score. ",
					"Instructions",JOptionPane.INFORMATION_MESSAGE);
		}
		
		if(e.getSource() == authorsMenuItem){
			//System.out.println("You have selected the Authors menu item");
			
			JOptionPane.showMessageDialog(this,"         SPACE INVADERS         \n"
										    +  "                                \n"
										    +  "          Developed by          \n"
											+  "                                \n"
											+  "         Travis Hajagos         \n"
											+  "         Brian Warfield         \n"
											+  "         Leo Gutierrez          \n"
											+  "          Ryan Cooper           \n"
											+  "          David Silva             ",
											"THE AUTHORS",JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	
}
