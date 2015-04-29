package SpaceInvaders_V3.LWJGL;

import SpaceInvaders_V3.GameState.GameStateManager;
import SpaceInvaders_V3.Main.GameWindow;
import SpaceInvaders_V3.Main.GameWindowCallback;
import SpaceInvaders_V3.Util.Keyboard;
import SpaceInvaders_V3.Util.SystemTimer;
import java.awt.Color;
import java.awt.Rectangle;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;


public class LWJGLGameWindow implements GameWindow {

    //game loop control boolean
    private boolean running = true;

    //the callback which should be notified of window events
    private GameWindowCallback callback;

    //frame Dimentions
    private static int G_WIDTH = 600;
    private static int G_HEIGHT = 600;

    //counting system
    public static final int FPS = 60;
    public static float averageFPS;

    //Game State Manager
    private GameStateManager gsm;

    //the loader responsible for converting imaged into OpenGL Textures
//    private TextureLoader textureLoader;

    //title of the window
    private String title;

    //create new game window
    public LWJGLGameWindow() {
    }

    //retrieve access to the texture loader
    //@return the texture loader
//    TextureLoader getTextureLoader() {
//        return textureLoader;
//    }

    //set the title of the window
    //@param title the title to set on this window
    @Override
    public void setTitle(String title) {
        this.title = title;
        if (Display.isCreated()) {
            Display.setTitle(title);
        }
    }

    //set game display resolution
    //@param x horizontal resolution
    //@param y vertical resolution
    @Override
    public void setResolution(int x, int y
    ) {
        G_WIDTH = x;
        G_HEIGHT = y;
    }


    //start the rendering process
    @Override
    public void startRendering() {

        try {

            Display.setDisplayMode(new DisplayMode(G_WIDTH, G_HEIGHT));
            Display.create();
            Display.setTitle(title);

//            Mouse.setGrabbed(true);
            //enable textures for sprites
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            //disable depth test for 2d graphics
            GL11.glDisable(GL11.GL_DEPTH_TEST);

            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();

            GL11.glOrtho(0, G_WIDTH, G_HEIGHT, 0, -1, 1);

//            textureLoader = new TextureLoader();

            if (callback != null) {
                callback.init();
            }

        } catch (LWJGLException le) {
            le.printStackTrace();
            callback.windowClosed();
        }

        //initialize game state manager
        gsm = new GameStateManager();
        gameLoop();
    }

    //register callback for this window
    @Override
    public void setGameWindowCallback(GameWindowCallback callback) {
        this.callback = callback;
    }

    //check if a particular key is pressed
    //@param keyCode te code associated with the key to check
    //@return True if the particular key is pressed
    @Override
    public boolean isKeyPressed(int keyCode) {
        return Keyboard.isPressed(keyCode);
    }

    //main game loop
    private void gameLoop() {

        //set timing intervals
        double lastLoopTime;
        lastLoopTime = SystemTimer.getTime();
        double delta;//time reference for loop duration
        double totalTime = 0;

        int frameCount = 0;
        int maxFrameCount = FPS;

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

            if (Display.isCloseRequested()) {
                running = false;
                Display.destroy();
                callback.windowClosed();
            }

        }
    }

    private void gameUpdate(double delta) {
        gsm.gameUpdate(delta);
    }

    private void gameRender() {
        //clear screen
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        //have game entities draw themselves
        gsm.gameRender();
    }

    private void gameDraw() {
        // update window contents
        Display.update();
    }

    @Override
    public float getFPS() {
        return Math.round(averageFPS * 100) / 100.0f;
    }

    @Override
    public int getWidth() {
        return G_WIDTH;
    }

    @Override
    public int getHeight() {
        return G_HEIGHT;
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
    public void fillRect(Color color, Rectangle rect) {
    }

}
