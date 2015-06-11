package SpaceInvaders_V4.Main;

import com.badlogic.jglfw.Glfw;
import java.awt.Canvas;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Game extends Canvas implements GameWindowCallback {

    //the window that is being used to render the game
    private GameWindow window;
    private String windowTitle = "Invaders From Space v4";

    /**
     * class constructor
     *
     * @param renderingType constants in resource factory class
     */
    public Game(int renderingType) {

        //create a window based on a chosed rendering methoc
        ResourceFactory.get().setRenderingType(renderingType);
        window = ResourceFactory.get().getGameWindow();

        window.setResolution(600, 600);
        window.setGameWindowCallback(this);
        window.setTitle(windowTitle);
    }

    /**
     * start the game window rendering the display
     *
     */
    public void startRendering() {
        window.startRendering();
    }

    /**
     * initialize the common elements for the game
     *
     */
    @Override
    public void init() {
        //load images into memory
        ResourceFactory.get().getSprite("resources/sprites/enemy/ogre.png");
        ResourceFactory.get().getSprite("resources/sprites/enemy/ogreTurret.png");
        ResourceFactory.get().getSprite("resources/sprites/enemy/enemyship2c.png");
        ResourceFactory.get().getSprite("resources/sprites/enemy/enemyship2b.png");
        ResourceFactory.get().getSprite("resources/sprites/enemy/enemyship2a.png");
        ResourceFactory.get().getSprite("resources/sprites/enemy/enemyShot1a.png");
        ResourceFactory.get().getSprite("resources/sprites/enemy/enemyShot2a.png");
        ResourceFactory.get().getSprite("resources/sprites/enemy/enemyShot3a.png");
        ResourceFactory.get().getSprite("resources/sprites/enemy/enemyShot4a.png");
        ResourceFactory.get().getSprite("resources/sprites/enemy/enemyShot5a.png");
        ResourceFactory.get().getSprite("resources/sprites/enemy/enemyShot6a.png");
        ResourceFactory.get().getSprite("resources/sprites/enemy/swarmer_p.png");
        ResourceFactory.get().getSprite("resources/sprites/enemy/swarmer_r.png");
        ResourceFactory.get().getSprite("resources/sprites/enemy/wasp_green.png");
        ResourceFactory.get().getSprite("resources/sprites/enemy/wasp_brown.png");
        ResourceFactory.get().getSprite("resources/sprites/effects/fire_16px_8x4tile.png");
        ResourceFactory.get().getSprite("resources/sprites/effects/fire_32x48px_8x5tile.png");
        ResourceFactory.get().getSprite("resources/sprites/effects/fire_32x64px_8x4tile.png");
        ResourceFactory.get().getSprite("resources/sprites/effects/explosion_260px_9tile.png");
        ResourceFactory.get().getSprite("resources/sprites/effects/explosion_200px_19tile.png");
        ResourceFactory.get().getSprite("resources/sprites/effects/explosion_100px_16tile.png");
        ResourceFactory.get().getSprite("resources/sprites/effects/explosion_100px_9tile.png");
        ResourceFactory.get().getSprite("resources/sprites/effects/explosion_65px_12tile.png");
        ResourceFactory.get().getSprite("resources/sprites/effects/explosion_50px_12tile.png");
        ResourceFactory.get().getSprite("resources/sprites/player/Player1Sprite.png");
        ResourceFactory.get().getSprite("resources/sprites/player/thrusters.png");
        ResourceFactory.get().getSprite("resources/sprites/player/bulletFlash.png");
        ResourceFactory.get().getSprite("resources/sprites/player/laserFlash.png");
        ResourceFactory.get().getSprite("resources/sprites/player/shotBullet1.png");
        ResourceFactory.get().getSprite("resources/sprites/player/shotBullet1.png");
        ResourceFactory.get().getSprite("resources/sprites/player/shotBullet2.png");
        ResourceFactory.get().getSprite("resources/sprites/player/shotBullet3.png");
        ResourceFactory.get().getSprite("resources/sprites/player/shotBullet4.png");
        ResourceFactory.get().getSprite("resources/sprites/player/shotBullet4L.png");
        ResourceFactory.get().getSprite("resources/sprites/player/shotBullet4R.png");
        ResourceFactory.get().getSprite("resources/sprites/player/shotLaser1.png");
        ResourceFactory.get().getSprite("resources/sprites/player/shotLaser2.png");
        ResourceFactory.get().getSprite("resources/sprites/player/shotLaser3.png");
        ResourceFactory.get().getSprite("resources/sprites/player/shotLaser4.png");
        ResourceFactory.get().getSprite("resources/sprites/player/shotLaser5.png");
        ResourceFactory.get().getSprite("resources/sprites/player/shotLaser6.png");
        ResourceFactory.get().getSprite("resources/sprites/player/Player1Shrapnel.png");
        ResourceFactory.get().getSprite("resources/sprites/player/powerUps.png");
        ResourceFactory.get().getSprite("resources/tilesets/PFArmaFive20Red.png");
        ResourceFactory.get().getSprite("resources/tilesets/PFArmaFive20Yel.png");
        ResourceFactory.get().getSprite("resources/title/title.png");
        ResourceFactory.get().getSprite("resources/backgrounds/Space.gif");

    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {

        //initialize glfw library
        boolean glfwResult = Glfw.glfwInit();
        if (glfwResult == false) {
            throw new IllegalStateException("GLFW Failed to initialize");
        }

        Game game = new Game(ResourceFactory.JAVA2D);
        game.init();
        game.startRendering();

    }

    /**
     * Close the game
     */
    @Override
    public void windowClosed() {
        Glfw.glfwTerminate();
        System.exit(0);
    }
}