package SpaceInvaders_V4.Score;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class HighScore_Frame extends JFrame implements ActionListener {

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

    // Define array for the scores
    private ArrayList<TopScores> scores = new ArrayList<TopScores>();

    // Define the score file name
    private static final String ScoreFile = "scores.dat";

    // Define program variables
    private String holdInitial;
    private int holdScore;
    private int k;
    private int lowScore;
    private int highScore;

    // Define input / output streams
    FileInputStream fileInput = null;
    ObjectInputStream inputStream = null;
    FileOutputStream fileOutput = null;
    ObjectOutputStream outputStream = null;

    // High Score Frame Constructor
    public HighScore_Frame() {

        super("HIGH SCORES");

        // Get and load scores into the score array
        getScores();
        getHighScores();

		// Set up the score panel layouts
        //this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        this.setLayout(new GridLayout(12, 2));

        this.getContentPane().setBackground(Color.black);

        // Load all panel items
        loadMenus();
        loadHeader();
        loadPanels();
        loadButtonPanel();
    }

    // Load header method to load the Bold Header
    public void loadHeader() {

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

    // Declare all labels and set font colors for each label.
    public void loadPanels() {

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
    }

	// Loads the file data for each player score.  If there are not enough scores to fill all 10
    //    spots, then the fields will be to black (invisible).
    public void loadFileInfo(JLabel playerLabel, JLabel scoreLabel) {

        System.out.println("you are in the loadFileInfo method");

        holdInitial = scores.get(k).getInitials();
        holdScore = scores.get(k).getScore();

        playerLabel.setText(holdInitial);
        scoreLabel.setText(Integer.toString(holdScore));

        if (scores.get(k).getScore() == 0) {
            playerLabel.setForeground(Color.BLACK);
            scoreLabel.setForeground(Color.BLACK);
        } else {
            playerLabel.setForeground(Color.WHITE);
            scoreLabel.setForeground(Color.WHITE);
        }
    }

    // Set font and font colors
    public void setFontColors(JLabel lblName) {

        System.out.println("You are in the setFontColors method");

        lblName.setFont(new Font("Courier", Font.BOLD, 20));
        lblName.setForeground(Color.WHITE);
    }

	// Create a panel from data passed to it from the calling method.  The name of the panel, the 
    //   player and score are required for each one.
    public void createAPanel(JPanel panelName, JLabel playerLabel, JLabel scoreLabel) {

        // Using the grid layout to set up the player scores
        GridLayout layout = new GridLayout(0, 2);

        // Set layout for player/score panel
        panelName.setLayout(layout);

        // Set a horizontal and vertical gap of 5
        layout.setHgap(5);
        layout.setVgap(5);

        // Add the 2 lables (player and score) to the panel
        panelName.add(playerLabel);
        panelName.add(scoreLabel);

        // Set the background color to black for each panel
        panelName.setBackground(Color.black);

        // Add the panel to the JFrame
        this.add(panelName);

    }

    // Create the menu bar and menu items.
    public void loadMenus() {

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

    // Define the button panel and add the 2 buttons for the panel
    public void loadButtonPanel() {

        // Define the button panel
        buttonPanel = new JPanel();

        // Define the 2 buttons
        playButton = new JButton("Play Again");
        exitButton = new JButton("Exit");

        // Add the 2 buttons to the button panel
        buttonPanel.add(playButton);
        buttonPanel.add(exitButton);

        // Set the background color to black 
        buttonPanel.setBackground(Color.BLACK);

        // Add the button panel to the JFrame
        this.add(buttonPanel);

        // Define the actions listeners for the 2 buttons
        playButton.addActionListener(this);
        exitButton.addActionListener(this);

    }

    // Load the Array so that the scores can be sorted in Descending order
    public ArrayList<TopScores> getScores() {

        // Call the loadScoreFile method
        loadScoreFile();

        // Call the sort method
        sort();

        // return the scores
        return scores;
    }

    // Sort method
    public void sort() {

        // Verify score method will determine if the score should be in the top 10
        VerifyScore verify = new VerifyScore();
        Collections.sort(scores, verify);
    }

	// addScore method will request user input from player for their initial if their score is 
    //    greater than the lowest of the top 10 scores.
    public void addScore(String initials, int score) {

        // method variables
        String myInitials = "   ";
        String spaceOut = "            ";
        int myScore = score;

        // Get the lowest and highest scores
        lowScore = scores.get(9).getScore();
        highScore = scores.get(0).getScore();

        // If the user's score is greater than the lowest score in the array, then ask for their initials.
        if (score > lowScore) {
            myInitials = JOptionPane.showInputDialog("Your score is " + score
                    + ". Please enter your initials:");
        } else {
            myInitials = "   ";
            myScore = score;
        }

		// Initials are limited to 3 bytes only.  If the user enters more than 3 bytes, accept the user
        //    input and truncate any bytes beyond 3.
        if (myInitials != null) {
            if (myInitials.length() > 3) {
                myInitials = myInitials.substring(0, 3);
            }
        }

		// If the user hit cancel and did not enter any initials, then the initials will get spaces and
        //    will store only the score.  If the user did enter initials, and they were lower case, we
        //    will convert the initials to upper case.
        if (myInitials != null) {
            initials = spaceOut + myInitials.toUpperCase();
        }

        // Add the scores to the top 10 scores with initials if entered
        scores.add(new TopScores(initials, myScore));

        // Call the updateScoreFile method
        updateScoreFile();

        // Call the loadScoreFile method
        loadScoreFile();

        // Call the getHighScores method
        getHighScores();

        // Call the reloadScores method
        reloadScores();
    }

    //Set the default scores to blanks and 0 if it is the first time running the game
    public void defaultScore(String initials, int score) {

        String myInitials = "   ";
        int defaultScore = 0;
        initials = myInitials.toUpperCase();
        score = defaultScore;

        // Fill the array with 10 initial 0 scores
        for (int i = 0; i < 10; i++) {
            scores.add(new TopScores(initials, score));
            updateScoreFile();
        }
    }

    // reloadScores method is to refresh the score screen after a new score has been updated
    public void reloadScores() {

        // Call the loadFileInfo for each player/score to refresh the scores.
        k = 0;
        loadFileInfo(playerlbl1, scorelbl1);
        k = 1;
        loadFileInfo(playerlbl2, scorelbl2);
        k = 2;
        loadFileInfo(playerlbl3, scorelbl3);
        k = 3;
        loadFileInfo(playerlbl4, scorelbl4);
        k = 4;
        loadFileInfo(playerlbl5, scorelbl5);
        k = 5;
        loadFileInfo(playerlbl6, scorelbl6);
        k = 6;
        loadFileInfo(playerlbl7, scorelbl7);
        k = 7;
        loadFileInfo(playerlbl8, scorelbl8);
        k = 8;
        loadFileInfo(playerlbl9, scorelbl9);
        k = 9;
        loadFileInfo(playerlbl10, scorelbl10);

    }

    // loadScoreFile method is usded to read the scores.dat file and load the array with data
    public void loadScoreFile() {

        try {
			//  If the file does not exist (used for first time run), then create the file and create
            //     10 default 0 scores.
            File file = new File("scores.dat");
            if (!file.exists()) {
                file.createNewFile();
                defaultScore("AAA", 0);
            }

            //  Declare the file input stream for serialization with error traps
            fileInput = new FileInputStream(ScoreFile);
            inputStream = new ObjectInputStream(fileInput);

            // Read the scores
            scores = (ArrayList<TopScores>) inputStream.readObject();
        } catch (FileNotFoundException e) {
            System.err.println("fileNot Found on loadScoreFile method");
        } catch (IOException e) {
            System.err.println("IO Error on loadScoreFile method");
        } catch (ClassNotFoundException e) {
            System.err.println("Class Not Found on loadScoreFile method");
        } finally {
            try {
                // Close the input streams for the file and the object
                inputStream.close();
                fileInput.close();
            } catch (IOException e) {
                System.err.println("IO Error on closing input files");
            }
        }

    }

    // updateScoreFile method is to update the scores.dat file with the new score just entered
    public void updateScoreFile() {

        try {
            // Declare the output streams for writing the data
            fileOutput = new FileOutputStream(ScoreFile);
            outputStream = new ObjectOutputStream(fileOutput);

            // Write the new data to the scores.dat file
            outputStream.writeObject(scores);
        } catch (FileNotFoundException e) {
            System.err.println("File Not Found on updateScoreFile method");
        } catch (IOException e) {
            System.err.println("IO Error on updateScoreFile method");
        } finally {
            try // Close the output streams
            {
                outputStream.close();
                fileOutput.close();
            } catch (IOException e) {
                System.err.println("Error on closing output files");
            }
        }

    }

    // The high scores toString method
    public String getHighScores() {

        // Method variables
        String scoreString = "";
        int max = 10;

        // Array list for the scores
        ArrayList<TopScores> scores;
        scores = getScores();

        // Set the maximum number of scores.  If there are more than 10 scores, then set the max to 10.
        int x = scores.size();
        if (x > max) {
            x = max;
        }

        // Will get the top 10 scores from the array.
        int i = 0;
        while (i < x) {
            scoreString += (i + 1) + "./t" + scores.get(i).getInitials()
                    + "/t/t" + scores.get(i).getScore() + "/n";
            i++;
        }

        // Return the string
        return scoreString;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == playButton) {
            //**************************************************************
            //    insert call to start the game again and hide this JFrame
            //**************************************************************
            System.out.println("You have clicked the Play Button");
        }

        // Exit the game
        if (e.getSource() == exitButton) {

            System.exit(0);
        }

        // Instructions menu
        if (e.getSource() == instructionMenuItem) {

            JOptionPane.showMessageDialog(this, "Scoring:\n 10 Front row  \n20 Second row  \n30 Third Row"
                    + "\nSpaceShip is ????   \nObject of the game to hit all the aliens before you run"
                    + "out of lives.  \nPlay as long as possible to get the high score. ",
                    "Instructions", JOptionPane.INFORMATION_MESSAGE);
        }

        // Who collaborated in this game
        if (e.getSource() == authorsMenuItem) {

            JOptionPane.showMessageDialog(this, "         SPACE INVADERS         \n"
                    + "                                \n"
                    + "          Developed by          \n"
                    + "                                \n"
                    + "         Travis Hajagos         \n"
                    + "         Brian Warfield         \n"
                    + "         Leo Gutierrez          \n"
                    + "          David Silva             ",
                    "THE AUTHORS", JOptionPane.INFORMATION_MESSAGE);
        }
    }

}
