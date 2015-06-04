package com.GutierrezLeo.CIS81B.GroupProject.SpaceInvaders;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

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
	private JMenuItem instructionAction, authorsAction;
	private JButton exitButton, playButton;
	
	// High Score Frame Constructor
	public HighScore_Frame(){
		
		super("HIGH SCORES");
		
		this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		loadMenus();
		loadHeader();
		loadPanels();
		loadButtonPanel();
		
	}
	
	// Load header method to load the Bold Header
	public void loadHeader(){
		
		headlbl = new JLabel("HIGH SCORES");
		headlbl.setFont(new Font("Impact", Font.BOLD, 40));
		headPanel = new JPanel();
		headPanel.setLayout(new FlowLayout());
		headPanel.setAlignmentX(CENTER_ALIGNMENT);
		headPanel.add(headlbl);
		this.add(headPanel);

	}
	
	// Declare all labels with default values for the moment.
	public void loadPanels(){
		
		scorelbl1 = new JLabel("00000");
		scorelbl1.setFont(new Font("Courier", Font.BOLD, 20));
		playerlbl1 = new JLabel("AAA");
		playerlbl1.setFont(new Font("Courier", Font.BOLD, 20));
		
		scorelbl2 = new JLabel("00000");
		scorelbl2.setFont(new Font("Courier", Font.BOLD, 20));
		playerlbl2 = new JLabel("BBB");
		playerlbl2.setFont(new Font("Courier", Font.BOLD, 20));
		
		scorelbl3 = new JLabel("00000");
		scorelbl3.setFont(new Font("Courier", Font.BOLD, 20));
		playerlbl3 = new JLabel("BBB");
		playerlbl3.setFont(new Font("Courier", Font.BOLD, 20));
		
		scorelbl4 = new JLabel("00000");
		scorelbl4.setFont(new Font("Courier", Font.BOLD, 20));
		playerlbl4 = new JLabel("CCC");
		playerlbl4.setFont(new Font("Courier", Font.BOLD, 20));
		
		scorelbl5 = new JLabel("00000");
		scorelbl5.setFont(new Font("Courier", Font.BOLD, 20));
		playerlbl5 = new JLabel("ZZZ");
		playerlbl5.setFont(new Font("Courier", Font.BOLD, 20));
		
		scorelbl6 = new JLabel("00000");
		scorelbl6.setFont(new Font("Courier", Font.BOLD, 20));
		playerlbl6 = new JLabel("XXX");
		playerlbl6.setFont(new Font("Courier", Font.BOLD, 20));
		
		scorelbl7 = new JLabel("00000");
		scorelbl7.setFont(new Font("Courier", Font.BOLD, 20));
		playerlbl7 = new JLabel("RRR");
		playerlbl7.setFont(new Font("Courier", Font.BOLD, 20));
		
		scorelbl8 = new JLabel("00000");
		scorelbl8.setFont(new Font("Courier", Font.BOLD, 20));
		playerlbl8 = new JLabel("SSS");
		playerlbl8.setFont(new Font("Courier", Font.BOLD, 20));
		
		scorelbl9 = new JLabel("00000");
		scorelbl9.setFont(new Font("Courier", Font.BOLD, 20));
		playerlbl9 = new JLabel("QQQ");
		playerlbl9.setFont(new Font("Courier", Font.BOLD, 20));
		
		scorelbl10 = new JLabel("00000");
		scorelbl10.setFont(new Font("Courier", Font.BOLD, 20));
		playerlbl10 = new JLabel("MMM");
		playerlbl10.setFont(new Font("Courier", Font.BOLD, 20));
		
		
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
		
	}

	
	// Create a panel from data passed to it from the calling method.  The name of the panel, the 
	//   player and score are required for each one.
	public void createAPanel(JPanel panelName, JLabel playerLabel, JLabel scoreLabel){
		
		panelName.setLayout(new FlowLayout());
		panelName.add(playerLabel);
		panelName.add(scoreLabel);
		panelName.setAlignmentX(CENTER_ALIGNMENT);
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
		instructionAction = new JMenuItem("Instructions");
		authorsAction = new JMenuItem("Authors");
		helpMenu.add(instructionAction);
		aboutMenu.add(authorsAction);
		instructionAction.addActionListener(this);
		authorsAction.addActionListener(this);
	}
	
	public void loadButtonPanel(){
		
		buttonPanel = new JPanel();
		playButton = new JButton("Play Again");
		exitButton = new JButton("Exit");
		buttonPanel.add(playButton);
		buttonPanel.add(exitButton);
		this.add(buttonPanel);
		playButton.addActionListener(this);
		exitButton.addActionListener(this);
		
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
		
		if(e.getSource() == instructionAction){
			//System.out.println("You have selected the Instructions menu item");
			
			JOptionPane.showMessageDialog(this,"Scoring:\n 10 Front row  \n20 Second row  \n30 Third Row"
					+ "\nSpaceShip is ????   \nObject of the game to hit all the aliens before you run"
					+ "out of lives.  \nPlay as long as possible to get the high score. ",
					"Instructions",JOptionPane.INFORMATION_MESSAGE);
		}
		
		if(e.getSource() == authorsAction){
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
