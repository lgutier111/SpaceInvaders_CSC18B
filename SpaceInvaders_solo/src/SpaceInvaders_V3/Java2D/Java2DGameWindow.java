package SpaceInvaders_V3.Java2D;

import SpaceInvaders_V3.GameState.GameStateManager;
import SpaceInvaders_V3.Main.GameWindow;
import SpaceInvaders_V3.Main.GameWindowCallback;
import SpaceInvaders_V3.Util.Keyboard;
import SpaceInvaders_V3.Util.SystemTimer;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Java2DGameWindow extends Canvas implements GameWindow {

    //frame Dimentions
    private static int G_WIDTH = 600;
    private static int G_HEIGHT = 600;
    //the Callback which should be notified of events caused by this window
    private GameWindowCallback callback;
    //the frame in which the canvas will be displayed
    private JFrame frame;
    //loop control
    private boolean running = true;
    //buffer for page flipping
    private BufferStrategy strategy;
    //2d rendering canvas
    private Graphics2D g;
    //counting system
    public static final int FPS = 60;
    public static float averageFPS;

    //Game State Manager
    private GameStateManager gsm;

    //create a new window to render using Java 2D    
    public Java2DGameWindow() {
        frame = new JFrame();
    }

    //set title of the game window
    //@param title for the game window
    @Override
    public void setTitle(String title) {
        frame.setTitle(title);
    }

    //set game display resolution
    //@param x horizontal resolution
    //@param y vertical resolution
    @Override
    public void setResolution(int x, int y) {
        G_WIDTH = x;
        G_HEIGHT = y;
    }

    //get window height and/or width
    //@return int vertical and/or horizontal witdh
    public int getWidth() {
        return G_WIDTH;
    }

    public int getHeight() {
        return G_HEIGHT;
    }

    //start the game window rendering the display
    @Override
    public void startRendering() {
        //get hold the content of the frame and set up the resolution of the game
        JPanel panel = (JPanel) frame.getContentPane();
        panel.setPreferredSize(new Dimension(G_WIDTH, G_HEIGHT));
        panel.setLayout(null);

        Keyboard.init(this);

        //setup our canvad size and put it into the content of the frame
        setBounds(0, 0, G_WIDTH, G_HEIGHT);
        panel.add(this);

        //tell AWT not to repaint this frame 
        setIgnoreRepaint(true);

        //make the window visible
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        //add listner to respond to respond to window close event
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (callback != null) {
                    callback.windowClosed();
                } else {
                    System.exit(0);
                }
            }
        });

        //request the focus
        requestFocus();

        //create the buffereing strategy with will allow AWT to manage accelerated graphics
        createBufferStrategy(2);
        strategy = getBufferStrategy();

        //notify callback if exists
        if (callback != null) {
            callback.init();
        }

        //initialize game state manager
        gsm = new GameStateManager();

        //start game loop
        gameLoop();
    }

    //set the callback that should be notified on the window events
    //@param callback The callback that should be notified
    public void setGameWindowCallback(GameWindowCallback callback) {
        this.callback = callback;
    }

    //check if a particular key is pressed
    //@param keyCode te code associated with the key to check
    //@return True if the particular key is pressed
    public boolean isKeyPressed(int keyCode) {
        return Keyboard.isPressed(keyCode);
    }

    //retrieve the current accelerated graphics context (package scoped)
    Graphics2D getDrawGraphics() {
        return g;
    }

    //main game loop
    public void gameLoop() {

        //set timing intervals
        double lastLoopTime;
        lastLoopTime = SystemTimer.getTime();
        double delta;//time reference for loop duration
        double totalTime = 0;

        int frameCount = 0;
        int maxFrameCount = FPS;

        //Main game loop
        while (running) {
            //get timing reference
            delta = SystemTimer.getTime() - lastLoopTime;
            lastLoopTime = SystemTimer.getTime();

            //update game mechanics
            gameUpdate(delta);
            //render the buffered image
            gameRender();
            //swap the buffer
            gameDraw();

            //wait the remaining time to get desired fps
            SystemTimer.sync(FPS);

            //calculate FPS
            totalTime += SystemTimer.getTime() - lastLoopTime;
            frameCount++;
            if (frameCount == maxFrameCount) {
                averageFPS = 1 / ((float) totalTime / frameCount);
                frameCount = 0;
                totalTime = 0;
            }

        }
    }

    private void gameUpdate(double delta) {
        gsm.gameUpdate(delta);
    }

    public void fillRect(Color color, Rectangle rect){
        g = (Graphics2D) strategy.getDrawGraphics();
        g.setColor(color);
        g.fillRect(rect.x, rect.y, rect.width, rect.height);
    }
    
    private void gameRender() {
        //clear screen
        g = (Graphics2D) strategy.getDrawGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, G_WIDTH, G_HEIGHT);//clear image buffer

        gsm.gameRender();

    }

    @Override
    public float getFPS() {
        return Math.round(averageFPS*100)/100.0f;
    }
    
    

    private void gameDraw() {
        g.dispose();
        strategy.show();
    }

    @Override
    public void keyTyped(int key) {
        gsm.keyTyped(key);
    }

    @Override
    public void keyPressed(int key) {
        gsm.keyPressed(key);
    }

    @Override
    public void keyReleased(int key) {
        gsm.keyReleased(key);
    }
}
