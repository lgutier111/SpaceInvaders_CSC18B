package SpaceInvaders_V4.Util;

//Sprite class to hold image information which can be instantiated
import java.awt.Image;
import java.awt.geom.AffineTransform;

//multiple times without the need to load multiple images 
public interface Sprite {

    /**
     * get width of the drawn sprite
     *
     * @return int width in pixels of the sprite
     */
    public int getWidth();

    /**
     * get the height of the drawn sprite
     *
     * @return int height in pixels
     */
    public int getHeight();

    /**
     * Get Sprite Image
     *
     * @return Sprite Image
     */
    public Image getImage();

    public String getRef();

    /**
     * draw the sprite onto the graphics context provided
     *
     * @param x horizontal coordinate
     * @param y vertical coordinate
     */
    public void draw(int x, int y);

    public void drawRotate(AffineTransform tra);

}
