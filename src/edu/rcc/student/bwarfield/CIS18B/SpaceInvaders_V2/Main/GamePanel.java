package edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.Main;

import edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.GameState.GameStateManager;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable, KeyListener {

    //Panel Dimentions
    public static final int G_WIDTH = 600;
    public static final int G_HEIGHT = 600;
    //game thread
    private Thread thread;
    private boolean running;
    //buffer for page flipping
    private BufferedImage image;
    //2d rendering canvas
    private Graphics2D g;
    //counting system
    public static final int FPS = 60;
    public static float averageFPS;
    private final long TARGETTIME = 1000 / FPS;

    //Game State Manager
    private GameStateManager gsm;
    //JPanel Constructor
    public GamePanel() {
        //run default constructor operations
        super();
        //set panel dimentions
        setPreferredSize(new Dimension(G_WIDTH, G_HEIGHT));
        //set and grab panel focus
        setFocusable(true);
        requestFocus();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        if (thread == null) {
            thread = new Thread(this);
            addKeyListener(this);
            thread.start();
        }

    }

    //initialize game
    private void init() {
        //start game
        running = true;
        //create immage buffer
        image = new BufferedImage(G_WIDTH, G_HEIGHT, BufferedImage.TYPE_INT_RGB);
        g = image.createGraphics();//graphics2d for drawing
        
        //initialize game state manager
        gsm = new GameStateManager();
    }

    @Override
    public void run() {
        //initialize game
        init();
        
        //set timing intervals
        long lastLoopTime;
        lastLoopTime = System.currentTimeMillis();
        long delta;//time reference for loop duration
        long totalTime = 0;

        int frameCount = 0;
        int maxFrameCount = FPS;

        //Main game loop
        while (running) {
            //get timing reference
            delta = System.currentTimeMillis() - lastLoopTime;
            lastLoopTime = System.currentTimeMillis();

            //update game mechanics
            gameUpdate(delta);
            //render the buffered image
            gameRender();
            //swap the buffer
            gameDraw();

            //wait the remaining time to get desired fps
            try {
                Thread.sleep(TARGETTIME - delta/ 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //calculate FPS
            totalTime += System.currentTimeMillis() - lastLoopTime;
            frameCount++;
            if (frameCount == maxFrameCount) {
                averageFPS = (float) (1000.0 / (totalTime / frameCount));
                frameCount = 0;
                totalTime = 0;
            }

        }
    }

     
    private void gameUpdate(long delta) {
        gsm.gameUpdate(delta);
    }

    private void gameRender() {
        gsm.gameRender(g);

    }

    private void gameDraw() {
        //get current graphics
        Graphics g2 = this.getGraphics();
        //draw buffered image
        g2.drawImage(image, 0, 0, null);
        //release graphics context
        g2.dispose();
    }

    @Override
    public void keyTyped(KeyEvent key) {
       gsm.keyTyped(key.getKeyCode());
    }

    @Override
    public void keyPressed(KeyEvent key) {
        gsm.keyPressed(key.getKeyCode()); 
    }

    @Override
    public void keyReleased(KeyEvent key) {
        gsm.keyReleased(key.getKeyCode());
    }
    
    

}
