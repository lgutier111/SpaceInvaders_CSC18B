package SpaceInvaders_V3.Main;

import java.awt.Color;
import java.awt.Rectangle;

public interface GameWindow {

    //set title of the game window
    //@param title for the game window
    public void setTitle(String title);

    //set game display resolution
    //@param x horizontal resolution
    //@param y vertical resolution
    public void setResolution(int x, int y);

 
    //get average framerate
    //@returns float of average framerate
    public float getFPS();
    
    //start the game window rendering the display
    public void startRendering();

    //set the callback that should be notified on the window events
    //@param callback The callback that should be notified
    public void setGameWindowCallback(GameWindowCallback callback);

    //check if a particular key is pressed
    //@param keyCode te code associated with the key to check
    //@return True if the particular key is pressed
    public boolean isKeyPressed(int keyCode);

    public void keyPressed(int k);

    public void keyReleased(int k);

    public void keyTyped(int k);

    //get window height and/or width
    //@return int vertical and/or horizontal witdh
    public int getWidth();

    public int getHeight();

    public void fillRect(Color color, Rectangle rect);
}
