package SpaceInvaders_V4.Java2D;

import SpaceInvaders_V4.GameState.GameStateManager;
import SpaceInvaders_V4.Main.GameWindow;
import SpaceInvaders_V4.Main.GameWindowCallback;
import SpaceInvaders_V4.Users.Score;
import SpaceInvaders_V4.Util.Keyboard;
import SpaceInvaders_V4.Util.SystemTimer;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
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

    /**
     * set title of the game window
     *
     * @param title for the game window
     */
    @Override
    public void setTitle(String title) {
        frame.setTitle(title);
    }

    /*set game display resolution
     *@param x horizontal resolution
     *@param y vertical resolution
     */
    @Override
    public void setResolution(int x, int y) {
        G_WIDTH = x;
        G_HEIGHT = y;
    }

    /**
     * get window height and/or width
     *
     * @return horizontal width
     */
    @Override
    public int getWidth() {
        return G_WIDTH;
    }

    /**
     * get window height
     *
     * @return vertical height
     */
    @Override
    public int getHeight() {
        return G_HEIGHT;
    }

    /**
     * start the game window rendering the display
     */
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
            @Override
            public void windowClosing(WindowEvent e) {
                new Thread() {
                    @Override
                    public void run() {
                        Score.submit();
                    }
                }.start();

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

    /**
     * set the callback that should be notified on the window events
     *
     * @param callback The callback that should be notified
     */
    @Override
    public void setGameWindowCallback(GameWindowCallback callback) {
        this.callback = callback;
    }

    /**
     * check if a particular key is pressed
     *
     * @param keyCode the code associated with the key to check
     * @return True if the particular key is pressed
     */
    @Override
    public boolean isKeyPressed(int keyCode) {
        return Keyboard.isPressed(keyCode);
    }

    /**
     * retrieve the current accelerated graphics context (package scoped)
     *
     * @return current accelerated graphics context
     */
    Graphics2D getDrawGraphics() {
        return g;
    }

    /**
     * main game loop Update, Render, Draw cycle clamped to calculated loop per
     * second
     */
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

    /**
     * update game entities based on timing factors
     *
     * @param delta time in seconds since last update
     */
    private void gameUpdate(double delta) {
        gsm.gameUpdate(delta);
    }

    /**
     * draw rectangle, used for hitBox debugging
     *
     * @param color color of rectangle
     * @param rect Rectangle object to draw
     */
    @Override
    public void fillRect(Color color, Rectangle rect) {
        g = (Graphics2D) strategy.getDrawGraphics();
        g.setColor(color);
        g.draw3DRect(rect.x, rect.y, rect.width, rect.height, true);
    }

    /**
     * render entities to image buffer
     */
    private void gameRender() {
        //clear screen
        g = (Graphics2D) strategy.getDrawGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, G_WIDTH, G_HEIGHT);//clear image buffer

        gsm.gameRender();

    }

    /**
     * get average frame rate in frames per second
     *
     * @return average frame rate in frames per second
     */
    @Override
    public float getFPS() {
        return Math.round(averageFPS * 100) / 100.0f;
    }

    /**
     * swap image buffer
     */
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

    @Override
    public void setVisable(boolean bool) {
        frame.setVisible(bool);
    }

}
