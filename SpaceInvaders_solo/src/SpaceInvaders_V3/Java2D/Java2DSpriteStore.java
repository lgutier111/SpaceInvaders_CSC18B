package SpaceInvaders_V3.Java2D;

//sprite resource manager
import SpaceInvaders_V3.Util.ImageShader;
import SpaceInvaders_V3.Util.Sprite;
import java.awt.Color;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import javax.imageio.ImageIO;

public class Java2DSpriteStore {

    //there will be only a store istance of this class
    private static final Java2DSpriteStore store = new Java2DSpriteStore();

    //get and return class instance
    //@return the store instance of this class
    public static Java2DSpriteStore get() {
        return store;
    }
    //Cached sprite map, from reference to sprite instance
    private final HashMap sprites = new HashMap();

    //retrieve a sprite from the sprite store
    //@param ref the reference to the image of the sprite
    //@return A sprite instance
    public Sprite getSprite(Java2DGameWindow window, String ref) {
        //if reference allready exists, retien cached reference
        if (sprites.get(ref) != null) {
            return (Sprite) sprites.get(ref);
        }

        Image image = imageIO(ref);

        // create a sprite, add to cache and return it
        Sprite sprite = new Java2DSprite(window, image, ref);
        sprites.put(ref, sprite);

        return sprite;

    }

    //create shaded sprite instance
    //@param java2d game window instance
    //@param ref reference to sprite image file
    //@param color color to tint sprite
    //@param alpha transparacy to shade sprite
    //@return shaded sprite instance
    public Sprite getSprite(Java2DGameWindow window, String ref, Color color, float alpha) {
        //if reference allready exists, retien cached reference
        if (sprites.get(ref) != null) {
            BufferedImage tempImage = (BufferedImage) ((Sprite) sprites.get(ref)).getImage();
            tempImage = ImageShader.generateMask(tempImage, color, alpha);
            Sprite sprite = new Java2DSprite(window, tempImage, ref);
            return sprite;
        }

        Image image = imageIO(ref);

        // create a sprite, add to cache and return it
        Sprite sprite = new Java2DSprite(window, image, ref);
        sprites.put(ref, sprite);

        //convert to image color mask
        BufferedImage tempImage = (BufferedImage) sprite.getImage();
        tempImage = ImageShader.generateMask(tempImage, color, alpha);
        Sprite shadedSprite = new Java2DSprite(window, tempImage, ref);

        return shadedSprite;
    }

    //retrieve a sub image of sprite from the sprite store
    //@param window Java2DGameWindow instance
    //@param ref the reference to the image of the sprite
    //@param x vertical position to start subImage
    //@param y horizontal position to start subImage
    //@param width of subImage
    //@param height of subImage
    //@return A subImage sprite instance
    public Sprite getSprite(Java2DGameWindow window, String ref, int x, int y, int width, int height) {
        //if reference allready exists, retrieve cached reference
        if (sprites.get(ref) != null) {
            BufferedImage tempImage = (BufferedImage) ((Sprite) sprites.get(ref)).getImage();
            tempImage = tempImage.getSubimage(x, y, width, height);
            Sprite sprite = new Java2DSprite(window, tempImage, ref);
            return sprite;
        }

        Image image = imageIO(ref);
        
        // create a sprite, add to cache and return it
        Sprite sprite = new Java2DSprite(window, image, ref);
        sprites.put(ref, sprite);

        BufferedImage tempImage = (BufferedImage) sprite.getImage();
        tempImage = tempImage.getSubimage(x, y, width, height);
        Sprite subSprite = new Java2DSprite(window, tempImage, ref);

        return subSprite;

    }

    //retrieve a sub image of sprite from the sprite store
    //@param window Java2DGameWindow instance
    //@param ref the reference to the image of the sprite
    //@param x vertical position to start subImage
    //@param y horizontal position to start subImage
    //@param width of subImage
    //@param height of subImage
    //@param Color to tint the sprite
    //@param alpha to shade sprite
    //@return A shaded subImage sprite instance
    public Sprite getSprite(Java2DGameWindow window, String ref, int x, int y, int width, int height, Color color, float alpha) {
        //if reference allready exists, retrieve cached reference
        if (sprites.get(ref) != null) {
            BufferedImage tempImage = (BufferedImage) ((Sprite) sprites.get(ref)).getImage();
            tempImage = tempImage.getSubimage(x, y, width, height);
            tempImage = ImageShader.generateMask(tempImage, color, alpha);
            Sprite sprite = new Java2DSprite(window, tempImage, ref);
            return sprite;
        }
        
        Image image = imageIO(ref);

        // create a sprite, add to cache and return it
        Sprite sprite = new Java2DSprite(window, image, ref);
        sprites.put(ref, sprite);

        BufferedImage tempImage = (BufferedImage) sprite.getImage();
        tempImage = tempImage.getSubimage(x, y, width, height);
        tempImage = ImageShader.generateMask(tempImage, color, alpha);
        Sprite shadedSubSprite = new Java2DSprite(window, tempImage, ref);

        return shadedSubSprite;

    }

    //perform IO function
    //@param ref reference to image file
    //@return Image instance of Image file
    private Image imageIO(String ref) {
        //otherwise get image from resource loader
        BufferedImage sourceImage = null;

        try {
            //get resource url using classLoader
            URL url = this.getClass().getClassLoader().getResource(ref);
            //call fail if ref url returns empty
            if (url == null) {
                fail("Unable to find ref: " + ref);
            }

            // use ImageIO to read the image in
            sourceImage = ImageIO.read(url);
        } catch (IOException e) {
            fail("Failed to load ref: " + ref);
        }

        // create an accelerated image of the right size to store our sprite in
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        Image image = gc.createCompatibleImage(sourceImage.getWidth(), sourceImage.getHeight(), Transparency.BITMASK);

        //draw source image into the accelerated graphics
        image.getGraphics().drawImage(sourceImage, 0, 0, null);

        return image;
    }

    //if resource loading fails, display error and close game

    private void fail(String mes) {
        System.err.println(mes);
        System.exit(0);
    }

}
