package SpaceInvaders_V3.Entity;

//player projectiles
import SpaceInvaders_V3.GameState.GameState;
import SpaceInvaders_V3.Java2D.Java2DGameWindow;
import SpaceInvaders_V3.Main.ResourceFactory;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class ShotEntity extends Entity {

    //projectile speed
    private float shotSpeed;
    private int dmg;
    //projectile angle
    private float rad;

    //current count of shot instances
//    private static int shots;
    //The game in which this entity exists
    private GameState game;
    //True if this shot hit something
    private boolean hit = false;

    //Create a new entity to represent the enemy
    // @param game The game in which the enemy is being created
    // @param x The initial x location
    // @param y The initial y location
    // @param ref The reference to the sprite to show for the enemy   
    public ShotEntity(GameState game, int dmg, int speed, float angle, int x, int y, String ref) {
        super(x, y, ref);
        this.game = game;

        rad = (float) Math.toRadians(angle);
        this.shotSpeed = speed;
        this.dmg = dmg;
        dx = (float) Math.cos(rad) * shotSpeed;
        dy = (float) Math.sin(rad) * shotSpeed;

//        shots++;
    }

    //get damage value
    public int getDmg() {
        return dmg;
    }

//    //get shot count
//    //@returns static count of shots
//    public static int getShots() {
//        return shots;
//    }
    //move shot
    //@param delta time in miliseconds since last movement
    @Override
    public void move(Double delta) {
        //keep moving
        super.move(delta);
//        // if shot goes off the screen, remove shot
//        if (y < -200 || y > ResourceFactory.get().getGameWindow().getHeight() + 200 || x < -200 || x > ResourceFactory.get().getGameWindow().getWidth() + 200) {
//            game.getRemovePlayerList().add(this);
//            shots--;
//        }

    }

    @Override
    public void doLogic() {
        super.doLogic();

        hitBox.setLocation((int) (x - sprite.getWidth() / 2), (int) (y - sprite.getHeight() / 2));
        hitBox.setSize(sprite.getWidth(), sprite.getHeight());
    }

    //override draw method
    @Override
    public void draw() {

        //draw ship
        super.draw();

        //draw hitbox for debugging
//        g.setColor(Color.GREEN);
//        g.drawRect((int) hitBox.getX(), (int) hitBox.getY(), (int) hitBox.getWidth(), (int) hitBox.getHeight());
//        (ResourceFactory.get().getGameWindow()).fillRect(Color.GREEN, hitBox);
    }

    public boolean hit() {
        return hit;
    }

    @Override
    public boolean collidesWith(Entity other) {
        //cancel collision if shot already hit something
        if (hit) {
            return false;
        }

        return super.collidesWith(other);
    }

    //Notification that the player's ship has collided with something
    // @param other The entity with which the shot has collided
    @Override
    public void collidedWith(Entity other) {
        // prevents double kills, if we've already hit something, don't collide
        if (hit) {
            return;
        }

        // if we've hit an alien, damage it!
        if (other instanceof EnemyEntity) {
            if (((EnemyEntity) other).isDead()) {
                return;
            } else {
                // remove the affected entities
                game.getRemovePlayerList().add(this);
//                shots--;
                hit = true;
            }
        }
    }

}
