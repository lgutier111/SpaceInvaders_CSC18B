package SpaceInvaders_V4.Entity;

import SpaceInvaders_V4.GameState.GameState;
import SpaceInvaders_V4.Main.ResourceFactory;
import SpaceInvaders_V4.Util.Sprite;
import SpaceInvaders_V4.Util.SystemTimer;
import java.awt.Color;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

/**
 *
 * @author Bee-Jay
 */
public class PowerUp extends Item {

    private GameState game;
    private Sprite[][] frames;
    private Sprite shadow;
    private int currentRow;
    private int currentCol;
    private double lastFrameChange;
    private double lastItemChange;

    private int ticks = 0;
    private int lastTick;

    private float angle;
    private int speed;
    private float incrementAngle;

    /**
     *
     * @param game
     * @param x
     * @param y
     * @param ref
     */
    public PowerUp(GameState game, int x, int y, String ref) {
        super(x, y, "resources/sprites/player/powerUps.png");
        this.type = Item.BULLET;

        this.game = game;

        frames = new Sprite[2][8];
        int height = sprite.getHeight() / frames.length;
        int width = sprite.getWidth() / frames[0].length;

        for (int row = 0; row < frames.length; row++) {
            for (int col = 0; col < frames[0].length; col++) {
                frames[row][col] = ResourceFactory.get().getSubSprite(sprite.getRef(), col * width, row * height, width, height);
            }
        }
        shadow = ResourceFactory.get().getShadedSubSprite(sprite.getRef(), 0, 0, width, height, Color.black, 0.5f);
        currentCol = 0;
        currentRow = 0;

        sprite = frames[currentRow][currentCol];

        angle = (float) (Math.random()*360);
        speed = 150;
        type = (int) (Math.random()*2); 
        
        incrementAngle = Math.random()>0.5?2:-2;
        
    }

    @Override
    public void doLogic() {
        super.doLogic();

        if (SystemTimer.getTime() - lastItemChange > 3) {
            type = type == Item.BULLET ? Item.LASER : Item.BULLET;
            lastItemChange = SystemTimer.getTime();
        }

        currentRow = type == Item.BULLET ? 1 : 0;

        if ((SystemTimer.getTime() - lastFrameChange > 0.04 && currentCol != 0) || (SystemTimer.getTime() - lastFrameChange > 1 && currentCol == 0)) {
            currentCol = currentCol + 1 >= frames[0].length ? 0 : currentCol + 1;
            lastFrameChange = SystemTimer.getTime();

            ticks++;
        }

        sprite = frames[currentRow][currentCol];
        if (ticks > lastTick) {

            if (ticks%100 < 50 && speed > 100) {
                speed -= 3;
            } else if (ticks%100 >= 50 && speed < 250) {
                speed += 3;
            }
            angle += incrementAngle;

            setHorizontalMovement((float) (sin(toRadians(angle)) * speed));
            setVerticalMovement((float) (cos(toRadians(angle)) * speed));
            lastTick = ticks;
        }

        //update hitbox location
        hitBox.setLocation((int) (x - sprite.getWidth() / 2), (int) (y - sprite.getHeight() / 2));
        hitBox.setSize(sprite.getWidth(), sprite.getHeight());
    }

    //move method based on timng system
    //@param delta the amount of time passed in milliseconds
    @Override
    public void move(Double delta, float xShift) {
        //update location based on move speed

        if (x + (delta * (dx + xShift)) > ResourceFactory.get().getGameWindow().getWidth()-(sprite.getWidth()/2) ) {
            x = ResourceFactory.get().getGameWindow().getWidth()-(sprite.getWidth()/2);
            angle +=180;
        } else if (x + (delta * (dx + xShift)) < 0+(sprite.getWidth()/2)) {
            x = 0+(sprite.getWidth()/2);
            angle +=180;
        } else {
            x += (delta * (dx + xShift));//shift horizontally
            
        }

        if (y + (delta * dy) > ResourceFactory.get().getGameWindow().getHeight()-(sprite.getHeight()/2)) {
            y = ResourceFactory.get().getGameWindow().getHeight()-(sprite.getHeight()/2);
            angle +=180;
        } else if (y + (delta * dy) < 0+(sprite.getHeight()/2) ) {
            y = 0+(sprite.getHeight()/2);
            angle +=180;
        } else {
            y += (delta * dy);//shift vertically
        }

    }

    @Override
    public void draw() {
        shadow.draw((int) x-5, (int) y-5);
        super.draw();
        //draw hitbox for debugging
//        (ResourceFactory.get().getGameWindow()).fillRect(Color.MAGENTA, hitBox);
    }

    @Override
    public void collidedWith(Entity other) {
        if (other instanceof PlayerEntity) {
            game.getRemoveItems().add(this);
        }
    }

}
