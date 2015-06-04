
package SpaceInvaders_V3.Main;


public interface GameWindowCallback {
    //notification that game should initialize any resource it needs to use,
    //including sprites
    public void init();
    
//    //Notification that the display is being rendered. The implementor should 
//    //render the scene and update any game logic
//    public void frameRendereing();
    
    //notification that game window has been closed
      public void windowClosed();
}
