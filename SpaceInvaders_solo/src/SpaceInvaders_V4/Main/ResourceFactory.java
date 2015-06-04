package SpaceInvaders_V4.Main;

//central reverence poin for creating resources for use in the game
import SpaceInvaders_V4.Java2D.Java2DGameWindow;
import SpaceInvaders_V4.Java2D.Java2DSpriteStore;
import SpaceInvaders_V4.LWJGL.LWJGLGameWindow;
import SpaceInvaders_V4.LWJGL.LWJGLSprite;
import SpaceInvaders_V4.Util.Sprite;
import java.awt.Color;


public class ResourceFactory {

    //singleton class instance
    private static final ResourceFactory factory = new ResourceFactory();

    //retrieve the single instance of this class
    //@return resource factory instance
    public static ResourceFactory get() {
        return factory;
    }

    //Enumerate render types
    public static final int JAVA2D = 1;
    public static final int OPENGL_LWJGL = 2;

    //the type of rendering we are currently utilizing
    private int renderingType = JAVA2D;

    //the window the game should use to render
    private GameWindow window;

    //default constructor to enforce singleton pattern
    private ResourceFactory() {
    }

    //Set the rendering method that should be used.
    //Note: This can only be done before the first resource is accessed.
    //@param renderingType The type of rendering to use
    public void setRenderingType(int renderingType) {
        // If the rendering type is unrecognised tell the caller
        if ((renderingType != JAVA2D) && (renderingType != OPENGL_LWJGL)) {
            //throw exception error
            throw new RuntimeException("Unknown rendering type specified: " + renderingType);
        }

        // If the window has already been created then we have already created resources in 
        // the current rendering method, we are not allowed to change rendering types
        if (window != null) {
            throw new RuntimeException("Attempt to change rendering method at game runtime");
        }

        this.renderingType = renderingType;
    }

    //retrieve the game window
    //@return the gae window
    public GameWindow getGameWindow() {
        // if we've yet to create the game window, create the appropriate one now
        if (window == null) {
            switch (renderingType) {
                case JAVA2D: {
                    window = new Java2DGameWindow();
                    break;
                }
                case OPENGL_LWJGL: {
                    window = new LWJGLGameWindow();
                    break;
                }
            }
        }
        return window;
    }

    //create or get a sprite that which displays the image that is pointed to the classpath
    //@param ref a reference to the image to load
    //@return a sprite that can be drawn onto the cirrent graphics context
    public Sprite getSprite(String ref) {
        if (window == null) {
            throw new RuntimeException("Attempt to retrieve sprite before game window was created");
        }

        switch (renderingType) {
            case JAVA2D: {
                return Java2DSpriteStore.get().getSprite((Java2DGameWindow) window, ref);
            }
            case OPENGL_LWJGL: {
                return new LWJGLSprite((LWJGLGameWindow) window, ref);
            }
        }

        throw new RuntimeException("Unknown rendering type: " + renderingType);
    }

    public Sprite getShadedSprite(String ref, Color color, float alpha ) {
        if (window == null) {
            throw new RuntimeException("Attempt to retrieve sprite before game window was created");
        }

        switch (renderingType) {
            case JAVA2D: {
                return Java2DSpriteStore.get().getSprite((Java2DGameWindow) window, ref, color, alpha);
            }
            case OPENGL_LWJGL: {
                int[] dummy = new int[0];
                return new LWJGLSprite((LWJGLGameWindow) window, ref, color, alpha, dummy);
            }
        }

        throw new RuntimeException("Unknown rendering type: " + renderingType);
    }

    public Sprite getSubSprite(String ref, int x, int y, int width, int height) {

        switch (renderingType) {
            case JAVA2D: {
                return Java2DSpriteStore.get().getSprite((Java2DGameWindow) window, ref, x, y, width, height);
            }
            case OPENGL_LWJGL: {
               int[] subData = { x, y, width, height};
                return new LWJGLSprite((LWJGLGameWindow) window, ref, null, 0, subData);
            }
        }
        throw new RuntimeException("Unknown rendering type: " + renderingType);
    }

    public Sprite getShadedSubSprite(String ref, int x, int y, int width, int height, Color color, float alpha) {

        switch (renderingType) {
            case JAVA2D: {
                return Java2DSpriteStore.get().getSprite((Java2DGameWindow) window, ref, x, y, width, height, color, alpha);
            }
            case OPENGL_LWJGL: {
                int[] subData = { x, y, width, height};
                return new LWJGLSprite((LWJGLGameWindow) window, ref, color, alpha, subData);
            }
        }
        throw new RuntimeException("Unknown rendering type: " + renderingType);
    }
}