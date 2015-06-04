package edu.rcc.student.bwarfield.CIS18B.SpaceInvaders;

//sprite resource manager
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import javax.imageio.ImageIO;

public class SpriteStore {

    //there will be only a single istance of this class

    private static SpriteStore single = new SpriteStore();

    //get and return class instance
    //@return the single instance of this class
    public static SpriteStore get() {
        return single;
    }
    //Cached sprite map, from reference to sprite instance
    private HashMap sprites = new HashMap();
    
    //retrieve a sprite from the sprite store
    //@param ref the reference to the image of the sprite
    //@return A sprite instance
    public InvaderSprite getSprite(String ref){
        //if reference allready exists, retien cached reference
        if (sprites.get(ref) != null){
            return (InvaderSprite) sprites.get(ref);
        }
        //otherwise get image rom resource loader
        BufferedImage sourceImage = null;
        
        try{
            //get resource url suning classLoader
            URL url = this.getClass().getClassLoader().getResource(ref);
            
            //call fail if ref url returns empty
            if (url == null){
                fail("Unable to find ref: "+ref);
            }
            
            // use ImageIO to read the image in
            sourceImage = ImageIO.read(url);
        }catch(IOException e){
            fail("Failed to load ref: "+ref);
        }
        
        // create an accelerated image of the right size to store our sprite in
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        Image image = gc.createCompatibleImage(sourceImage.getWidth(),sourceImage.getHeight(),Transparency.BITMASK);
        
        //draw source image into the accelerated image
        image.getGraphics().drawImage(sourceImage,0,0,null);
        
        // create a sprite, add to cache and return it
        InvaderSprite sprite = new InvaderSprite(image);
        sprites.put(ref,sprite);
        return sprite;
        
    }
    
    //if resource loading fails, display error and close game
    private void fail(String mes){
        System.err.println(mes);
        System.exit(0);
    }
}
