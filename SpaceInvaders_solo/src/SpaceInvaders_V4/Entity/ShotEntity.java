package SpaceInvaders_V4.Entity;

//player projectiles
import SpaceInvaders_V4.GameState.GameState;

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

    }

    //get damage value
    public int getDmg() {
        return dmg;
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
