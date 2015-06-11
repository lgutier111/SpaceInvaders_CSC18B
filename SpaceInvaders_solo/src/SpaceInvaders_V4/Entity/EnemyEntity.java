package SpaceInvaders_V4.Entity;

import SpaceInvaders_V4.GameState.GameState;
import SpaceInvaders_V4.Main.ResourceFactory;
import SpaceInvaders_V4.Users.Score;
import SpaceInvaders_V4.Util.Sprite;
import SpaceInvaders_V4.Util.SystemTimer;
import java.awt.Color;
import java.awt.Point;
import static java.lang.Math.*;

/**
 *
 * @author Bee-Jay
 */
public class EnemyEntity extends Entity {


    /**
     *the game in which the enemy exists
     */
    protected GameState game;

    //enemy stats
    /**
     *Enemy maximum health
     */
    protected int maxHealth;

    /**
     *Enemy current health
     */
    protected int health;
 

    /**
     *scaling factory for enemy health and score value
     */
    protected int rank;


    /**
     * scoring value
     */
    protected int value;


    /**
     * collision condition
     */
    protected boolean hit;

    /**
     *Enemy death status
     */
    protected boolean dead;

    //origin point on entity for bullet creation 
    /**
     * horizontal origin for projectiles
     */
    protected int gunX;

    /**
     * vertical origin for projectiles
     */
    protected int gunY;

    //timer variables
    final double startTime;//referece point from creation of Entity

    /**
     * time since creation
     */
    protected double elapsed;

    /**
     * breaks time into tick units for scripting (approx 25 ticks/sec)
     */
    protected double ticker;

    /**
     * ticker for movement scripts (approx 25 ticks/sec)
     */
    protected int moveTicks;

    /**
     * ticker tracker for movement scripts, use if(moveTicks >
     * lastMove){lastMove = moveTicks;} to ensure single execution for each tick
     */
    protected int lastMove;

    /**
     * ticker for shooting scripts (approx 25 ticks/sec)
     */
    protected int shotTicks;

    /**
     * ticker tracker for shooting scripts, use if(shotTicks >
     * lastShot){lastShot = shotTicks;} to ensure single execution for each tick
     */
    protected int lastShot;//ticker for shooting scripts

    /**
     * direction of movement
     */
    protected float angle;

    /**
     * speed in pixels per second
     */
    protected int speed;

    /**
     * Target coordinated for shooting
     */
    protected Point target;

    /**
     * angle from shooting origin to target coordinates
     */
    protected int targetAngle;

    /**
     * Create a new entity to represent the enemy
     *
     * @param game game The game in which the enemy is being created
     * @param rank
     * @param x The initial x location
     * @param y The initial y location
     * @param ref The reference to the sprite image file to show for the enemy
     */
    public EnemyEntity(GameState game, int rank, int x, int y, String ref) {
        super(x, y, ref);
        this.game = game;

        //minimum rank of 1
        this.rank = rank < 1 ? 1 : rank;

        //scale vars according to rank
        this.health = 10 * rank;
        this.maxHealth = this.health;
        this.value = 100 * rank;

        //init conditions
        this.hit = false;
        this.dead = false;

        this.gunX = (int) x;
        this.gunY = (int) y + (this.sprite.getHeight() / 2);

        this.target = new Point(x, y);
        //set random player as target

        //init movement
        setHorizontalMovement(0);
        setVerticalMovement(0);

        //init timers
        startTime = SystemTimer.getTime();
        ticker = 0;
        moveTicks = 0;
        shotTicks = 0;
        lastShot = 0;
        lastMove = 0;
    }

    @Override
    public void doLogic() {
        angle = (angle + 360) % 360;//bind range between 0 and 360 

        //set target to random player position if one exists, else tart is straight down
        if (game.getPlayers().size() > 0) {
            Entity player = (Entity) game.getPlayers().get((int) (random() * (game.getPlayers().size() - 1)));
            target.setLocation(player.getX(), player.getY());
        } else {
            target.setLocation(x, y + 30);
        }

        elapsed = SystemTimer.getTime() - startTime;
        if (SystemTimer.getTime() - ticker > 0.04) {//approx 25ticks/sec
            moveTicks++;
            shotTicks++;
            ticker = SystemTimer.getTime();
        }

        //update hitbox location
        hitBox.setLocation((int) (x - sprite.getWidth() / 2), (int) (y - sprite.getHeight() / 2));
        hitBox.setSize(sprite.getWidth(), sprite.getHeight());

        //update bullet origin
        this.gunX = (int) x;
        this.gunY = (int) y + (this.sprite.getHeight() / 2);
    }

    /**
     * shoot at angle relative to self
     *
     * @param speed projectile speed in pix/sec
     * @param deg angle in degrees (0 degrees at 6 o'clock)
     */
    public void shoot(int speed, int deg) {
        EnemyShotEntity shot = new EnemyShotEntity(this.game, gunX, gunY, "resources/sprites/enemy/enemyShot1a.png");
        game.getEnemyEntities().add(shot);
        shot.setHorizontalMovement((float) (speed * sin(toRadians(deg))));
        shot.setVerticalMovement((float) (speed * cos(toRadians(deg))));
    }

    /**
     * shoot at angle relative to target
     *
     * @param speed projectile speed in px/sec
     * @param point coordinated to target entity
     * @param deg angle in degrees to adjust shot. Positive being ccw, negative
     * being cw, 0 being on target
     */
    public void shoot(int speed, Point point, int deg) {
        //angle to target in radians
        float rads = (float) (atan2((point.getX() - gunX), (point.getY() - gunY)));

        //spawn bullet at bottom middle of ship sprite (or wherever the guns will be)
        EnemyShotEntity shot = new EnemyShotEntity(this.game, gunX, gunY, "resources/sprites/enemy/enemyShot1a.png");
        game.getEnemyEntities().add(shot);
        //target player ship. scale diagonal shotSpeed using trig functions 
        shot.setHorizontalMovement((float) (speed * sin(rads + toRadians(deg))));
        shot.setVerticalMovement((float) (speed * cos(rads + toRadians(deg))));
    }

    /**
     * is it dead?
     *
     * @return true if entity is in death sequence
     */
    public boolean isDead() {
        return dead;
    }

    //override draw method
    @Override
    public void draw() {

        //draw shadow image under ship
        Sprite shadow = ResourceFactory.get().getShadedSprite(sprite.getRef(), Color.BLACK, 0.5f);
        shadow.draw((int) x, (int) y);

        //draw ship
        super.draw();

        //if enemy has been hit, flash a colored frame
        if (hit) {
            Sprite dmg = ResourceFactory.get().getShadedSprite(sprite.getRef(), Color.WHITE, 0.7f);
            dmg.draw((int) x - (dmg.getWidth() / 2), (int) y - (dmg.getHeight() / 2));
            hit = false;
        }
        //draw hitbox for debugging
//        (ResourceFactory.get().getGameWindow()).fillRect(Color.RED, hitBox);
    }

    @Override
    public void collidedWith(Entity other) {

        if (dead) {
            return;
        }

        // if shot, take damage
        if (other instanceof ShotEntity) {
            health -= ((ShotEntity) other).getDmg();
            hit = true;

            // remove the affected entities
            if (health <= 0) {
                dead = true;
                Score.addKill();
                Score.addScore(value);
                game.getRemoveEnemyList().add(this);

            }

        }
    }

}
