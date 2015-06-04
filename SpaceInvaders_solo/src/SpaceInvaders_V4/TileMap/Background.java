package SpaceInvaders_V4.TileMap;

import SpaceInvaders_V4.Util.Sprite;
import SpaceInvaders_V4.Main.ResourceFactory;


public class Background {

    private Sprite BGImage;
    
    //image position
    private float x;
    private float y;
    //image velocity
    private float dx;
    private float dy;
    //movement scale
    private float moveScale;

    //constructor methos
    //@param ref path to background image
    //@ ms scale of movement for parallax effect
    public Background(String ref, float ms) {
        BGImage = ResourceFactory.get().getSprite(ref);//use sprite store to prevent redundant image loads
        moveScale = ms;
    }

    //set position of background image, mod by image dimentions as bounds check
    //@param x horizontal position
    //@param y vertical positioin
    public void setPosition(float x, float y) {
        this.x = (x * moveScale) % BGImage.getWidth();
        this.y = (y * moveScale) % BGImage.getHeight();
    }
    
    //set movement speed of background
    //@param vertical movement speed in px/frame
    //@param horizontal movement in px/frame
    public void setVector(float dx, float dy) {
        this.dx = dx;
        this.dy = dy;
    }

    //calculate and apply movement
    //@param delta timing vector
    public void gameUpdate(double delta) {
        x += (dx * delta) ;
        y += (dy * delta) ;
        //bounds check
        x = x % BGImage.getWidth();
        y = y % BGImage.getHeight();
    }
    
    //draw image to graphics context
    //@param graphics context
    public void gameRender() {
        BGImage.draw((int) x, (int) y);
        
        //repeat image if it extents beyond bounds
        if (x + BGImage.getWidth() < ResourceFactory.get().getGameWindow().getWidth()) {
            BGImage.draw( (int) x + BGImage.getWidth(), (int) y);
            if (y + BGImage.getHeight() < ResourceFactory.get().getGameWindow().getHeight()) {
                BGImage.draw((int) x + BGImage.getWidth(), (int) y + BGImage.getHeight());
            }
            if (y > 0) {
                BGImage.draw((int) x + BGImage.getWidth(), (int) y - BGImage.getHeight());
            }
        }
        if (x > 0) {
            BGImage.draw( (int) x - BGImage.getWidth(), (int) y);
            if (y + BGImage.getHeight() < ResourceFactory.get().getGameWindow().getHeight()) {
                BGImage.draw( (int) x - BGImage.getWidth(), (int) y + BGImage.getHeight());
            }
            if (y > 0) {
                BGImage.draw( (int) x - BGImage.getWidth(), (int) y - BGImage.getHeight());
            }
        }
        if (y + BGImage.getHeight() < ResourceFactory.get().getGameWindow().getHeight()) {
            BGImage.draw( (int) x, (int) y + BGImage.getHeight());
            if (x + BGImage.getWidth() < ResourceFactory.get().getGameWindow().getWidth()) {
                BGImage.draw( (int) x + BGImage.getWidth(), (int) y + BGImage.getHeight());
            }
            if (x > 0) {
                BGImage.draw( (int) x - BGImage.getWidth(), (int) y + BGImage.getHeight());
            }
        }
        if (y > 0) {
            BGImage.draw( (int) x, (int) y - BGImage.getHeight());
            if (x + BGImage.getWidth() < ResourceFactory.get().getGameWindow().getWidth()) {
                BGImage.draw( (int) x + BGImage.getWidth(), (int) y - BGImage.getHeight());
            }
            if (x > 0) {
                BGImage.draw( (int) x - BGImage.getWidth(), (int) y - BGImage.getHeight());
            }
        }

    }
}
