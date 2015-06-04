package SpaceInvaders_V3.Entity;

import SpaceInvaders_V3.GameState.GameState;
import SpaceInvaders_V3.Main.ResourceFactory;
import SpaceInvaders_V3.Util.Sprite;
import SpaceInvaders_V3.Util.SystemTimer;
import java.awt.Color;
import static java.lang.Math.*;

public class EnemyEntity extends Entity {

    //the game in which the enemy exists
    protected GameState game;

    //enemy stats
    protected final int maxHealth;
    protected int health;
    //for dificulty scaling
    protected int rank;
    //for scoring system
    protected int value;

    //entity conditions
    protected boolean hit;
    protected boolean dead;
    
    //origin point on entity for bullet creation 
    protected int gunX;
    protected int gunY;
    
    //timer variables
    final double startTime;//referece point from creation of Entity
    protected double elapsed;//time since creation
    protected double ticker;//breaks time into tick units for scripting (approx 25 ticks/sec)
    protected int moveTicks;//ticker for movement scripts
    protected int shotTicks;//ticker for shooting scripts

    //scripting vatiables
    protected int pattern;// optional variable for setting up conditionals for multiple behavior patterns in a single enemy class 
    protected float angle;//direction of movement
    protected int speed;//speed in pixels per second
    
    //player entity for targeting
    protected Entity target;

    //Create a new entity to represent the enemy
    // @param game The game in which the enemy is being created
    // @param x The initial x location
    // @param y The initial y location
    // @param ref The reference to the sprite to show for the enemy
    public EnemyEntity(GameState game, int rank, int x, int y, String ref) {
        super(x, y, ref);
        this.game = game;

        //minimum rank of 1
        this.rank = rank < 1 ? 1 : rank;

        //scale vars according to rank
        this.health = 10 * rank;
        this.maxHealth = 20 * rank;
        this.value = 100 * rank;
        
        //init conditions
        this.hit = false;
        this.dead = false;
        
        this.gunX =(int) x;
        this.gunY =(int) y + (this.sprite.getHeight() / 2);
        
        //set random player as target
        if(game.getPlayers().size()>0){
            this.target = (Entity) game.getPlayers().get((int)random()*(game.getPlayers().size()-1));
        }

        //init movement
        setHorizontalMovement(0);
        setVerticalMovement(0);

        //init timers
        startTime = SystemTimer.getTime();
        ticker = 0;
        moveTicks = 0;
        shotTicks = 0;
    }

    @Override
    public void doLogic() {
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
        this.gunX =(int) x;
        this.gunY =(int) y + (this.sprite.getHeight() / 2);
        
//        rads = (float)(atan2(target.getX()  - (x + sprite.getWidth() / 2), target.getY()  - (y+sprite.getHeight() / 2)));
    }

    //shoot at angle
    //@param int projectile speed in px/sec
    //@param int angle in degrees (0deg at 6 o'clock)
    public void shoot(int speed, int deg) {
        EnemyShotEntity shot = new EnemyShotEntity(this.game, gunX, gunY, "resources/sprites/enemy/enemyShotAnim1a.gif");
        game.getEnemyEntities().add(shot);
        shot.setHorizontalMovement((float) (speed * sin(toRadians(deg))));
        shot.setVerticalMovement((float) (speed * cos(toRadians(deg))));
    }

    //target at angle to entity
    //@param int projectile speed in px/sec
    //@param Entity entity instance
    //@param int angle in degrees to adjust shot. Positive being ccw, negative being cw, 0 being on target
    public void shoot(int speed, Entity entity, int deg) {
        //angle to target in radians
        float rads = (float) (atan2((entity.getX() - gunX),(entity.getY() - gunY)));

        //spawn bullet at bottom middle of ship sprite (or wherever the guns will be)
        EnemyShotEntity shot = new EnemyShotEntity(this.game, gunX, gunY, "resources/sprites/enemy/enemyShotAnim1a.gif");
        game.getEnemyEntities().add(shot);
        //target player ship. scale diagonal shotSpeed using trig functions 
        shot.setHorizontalMovement((float) (speed * sin(rads + toRadians(deg))));
        shot.setVerticalMovement((float) (speed * cos(rads + toRadians(deg))));
    }

    //target entity at 45deg increments
    //@param int projectile speed in px/sec
    //@param Entity entity instance
    //@param boolean dummy value, true or false it's just used to select this overloaded method.
    public void shoot(int speed, Entity entity, boolean dummy) {

        //angle to target in radians
        float rads = (float) (atan2(entity.getX()  - gunX, entity.getY()  - gunY));
        int deg = 0;

        //set angle to nearest 45deg increment
        if (rads >= toRadians(0) && rads < toRadians(22.5)) {
            deg = 0;
        } else if (rads >= toRadians(22.5) && rads < toRadians(67.5)) {
            deg = 45;
        } else if (rads >= toRadians(67.5) && rads < toRadians(112.5)) {
            deg = 90;
        } else if (rads >= toRadians(112.5) && rads < toRadians(157.5)) {
            deg = 135;
        } else if (rads >= toRadians(157.5) && rads <= toRadians(181)) {
            deg = 180;
        } else if (rads >= toRadians(-181) && rads < toRadians(-157.5)) {
            deg = 180;
        } else if (rads >= toRadians(-157.5) && rads < toRadians(-112.5)) {
            deg = 225;
        } else if (rads >= toRadians(-112.5) && rads < toRadians(-67.5)) {
            deg = 270;
        } else if (rads >= toRadians(-67.5) && rads < toRadians(-22.5)) {
            deg = 315;
        } else {
            deg = 0;
        }

        //spawn bullet at bottom middle of ship sprite (or wherever the guns will be)
        EnemyShotEntity shot = new EnemyShotEntity(this.game, gunX, gunY, "resources/sprites/enemy/enemyShot1a.png");
        game.getEnemyEntities().add(shot);
        //target player ship. scale diagonal shotSpeed using trig functions 
        shot.setHorizontalMovement((float) (speed * Math.sin(toRadians(deg))));
        shot.setVerticalMovement((float) (speed * Math.cos(toRadians(deg))));
    }

    //is it dead?
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
            Sprite dmg = ResourceFactory.get().getShadedSprite(sprite.getRef(),  Color.WHITE, 0.7f);
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
                game.getRemoveEnemyList().add(this);

            }

        }
    }

}

