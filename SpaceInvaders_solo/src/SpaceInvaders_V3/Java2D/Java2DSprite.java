package SpaceInvaders_V3.Java2D;

import SpaceInvaders_V3.Util.Sprite;
import java.awt.Image;

public class Java2DSprite implements Sprite {

    // Sprite Image
    private Image image;
    
    //sprite ref
    private String ref;

    //The game window to which this sprite is going to be drawn
    private Java2DGameWindow window;

    //SpriteConsctuctor Method
    // @param window The game window to which this sprite is going to be drawn
    //@param image The image that is this sprite
    public Java2DSprite(Java2DGameWindow window, Image image, String ref) {
        this.image = image;
        this.window = window;
        this.ref = ref;
    }

    // Get the width of the drawn sprite
    // @return The width in pixels of this sprite
    @Override
    public int getWidth() {
        return image.getWidth(null);
    }

    //Get the height of the drawn sprite
    // @return The height in pixels of this sprite
    @Override
    public int getHeight() {
        return image.getHeight(null);
    }

    //Get Sprite Image
    //@return Sprite Image
    @Override
    public Image getImage() {
        return image;
    }

    @Override
    public String getRef() {
        return ref;
    }

  
    
    //draw the sprite onto the graphics context provided
    //@param g The graphics context on which to draw the sprite
    //@param x horizontal coodinate
    //@param y vertical coordinate
    @Override
    public void draw(int x, int y) {
        window.getDrawGraphics().drawImage(image, x, y, null);
    }
}
