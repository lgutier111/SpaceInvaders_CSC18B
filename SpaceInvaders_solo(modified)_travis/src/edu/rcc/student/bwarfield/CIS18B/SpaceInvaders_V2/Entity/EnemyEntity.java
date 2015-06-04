package edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.Entity;

import edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.GameState.GameState;
import edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.Main.GamePanel;
import edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.Main.ImageShader;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import static java.lang.Math.*;

public class EnemyEntity extends Entity {

    //the game in which the enemy exists
    private GameState game;

    private int health;
    private int type;
    private int rank;
    private boolean hit;
    private boolean dead;
    private int value;

    //Create a new entity to represent the enemy
    // @param game The game in which the enemy is being created
    // @param x The initial x location
    // @param y The initial y location
    // @param ref The reference to the sprite to show for the enemy
    public EnemyEntity(GameState game, int x, int y, String ref) {
        super(x, y, ref);
        this.game = game;

        health = 10;
        dead = hit = false;

        setHorizontalMovement(0);
        setVerticalMovement(0);

    }

    //move enemy
    //@param delta time in miliseconds since last movement
    @Override
    public void move(long delta) {

        //keep moving
        super.move(delta);
        // if enemy goes off the screen, remove enemy
        if (y > GamePanel.G_HEIGHT + 200 || x < -200 || x > GamePanel.G_WIDTH + 200) {
            game.getRemoveEnemyList().add(this);
        }
        hitBox = new Rectangle((int) (x - sprite.getWidth() / 2), (int) (y - sprite.getHeight() / 2), sprite.getWidth(), sprite.getHeight());
    }


    //shoot at angle
    //@param int projectile speed in px/sec
    //@param int angle in degrees (0deg at 6 o'clock)
    public void shoot(int speed, int deg) {
        EnemyShotEntity shot = new EnemyShotEntity(this.game, (int) x + (this.sprite.getWidth() / 2) - 4, (int) y + (this.sprite.getHeight()), "enemyShotAnim1a.gif");
        game.getEnemyEntities().add(shot);
        shot.setHorizontalMovement((float) (speed * sin(toRadians(deg))));
        shot.setVerticalMovement((float) (speed * cos(toRadians(deg))));
    }

    //target at angle to entity
    //@param int projectile speed in px/sec
    //@param Entity entity instance
    //@param int angle in degrees to adjust shot. Positive being ccw, negative being cw, 0 being on target
    public void shoot(int speed, Entity entity, int deg) {
        //horizontal difference between player ship and enemy
        float xDiff = (entity.getX() - x);
        //vertical difference between player ship and enemy
        float yDiff = (entity.getY()) - (y + (this.sprite.getHeight() / 2));
        //angle to target in radians
        float rads = (float) (atan2(xDiff, yDiff));

        //spawn bullet at bottom middle of ship sprite (or wherever the guns will be)
        EnemyShotEntity shot = new EnemyShotEntity(this.game, (int) x, (int) y + (this.sprite.getHeight() / 2), "resources/sprites/enemy/enemyShotAnim1a.gif");
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
        //
        //horizontal difference between player ship and enemy
        float xDiff = (entity.getX() + entity.sprite.getWidth() / 2) - (x + (this.sprite.getWidth() / 2));
        //vertical difference between player ship and enemy
        float yDiff = (entity.getY() + entity.sprite.getHeight() / 2) - (y + (this.sprite.getHeight()));
        //angle to target in radians
        float rads = (float) (atan2(xDiff, yDiff));
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
        EnemyShotEntity shot = new EnemyShotEntity(this.game, (int) x + (this.sprite.getWidth() / 2) - 4, (int) y + (this.sprite.getHeight()), "resources/sprites/enemy/enemyShot1a.png");
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
    public void draw(Graphics g) {

        //draw shadow image under ship
        BufferedImage shadow = ImageShader.generateMask((BufferedImage) sprite.getImage(), Color.BLACK, 0.5f);
        g.drawImage(shadow, (int) x, (int) y, null);

        //draw ship
        super.draw(g);

        //if enemy has been hit, flash a colored frame
        if (hit) {
            BufferedImage dmg = ImageShader.generateMask((BufferedImage) sprite.getImage(), Color.white, 0.7f);
            g.drawImage(dmg, (int) x - (dmg.getWidth() / 2), (int) y - (dmg.getHeight() / 2),null);
            hit = false;
        }
        //draw hitbox for debugging
        g.setColor(Color.RED);
        g.drawRect((int) hitBox.getX(), (int) hitBox.getY(), (int) hitBox.getWidth(), (int) hitBox.getHeight());
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
