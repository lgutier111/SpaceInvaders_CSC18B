package edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.Entity;

//player projectiles
import edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.GameState.GameState;
import edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.Main.GamePanel;
import edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.Main.ImageShader;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class ShotEntity extends Entity {

    //projectile speed
    private float shotSpeed;
    //projectile angle
    private float rad;

    //The game in which this entity exists
    private GameState game;
    //True if this shot hit something
    private boolean hit = false;

    //Create a new entity to represent the enemy
    // @param game The game in which the enemy is being created
    // @param x The initial x location
    // @param y The initial y location
    // @param ref The reference to the sprite to show for the enemy   
    public ShotEntity(GameState game, float angle, int x, int y, String ref) {
        super(x, y, ref);
        this.game = game;

        rad = (float) Math.toRadians(angle);
        shotSpeed = 450;
        dx = (float) Math.cos(rad) * shotSpeed;
        dy = (float) Math.sin(rad) * shotSpeed;

    }

    //move shot
    //@param delta time in miliseconds since last movement
    @Override
    public void move(long delta) {
        //keep moving
        super.move(delta);
        // if shot goes off the screen, remove shot
        if (y < -200 || y > GamePanel.G_HEIGHT + 200 || x < -200 || x > GamePanel.G_WIDTH + 200) {
            game.getRemoveList().add(this);
        }
        hitBox = new Rectangle((int) (x - sprite.getWidth() / 2), (int) (y - sprite.getHeight() / 2), sprite.getWidth(), sprite.getHeight());
    }

    //override draw method
    @Override
    public void draw(Graphics g) {

        //draw ship
        super.draw(g);

        //draw hitbox for debugging
//        g.setColor(Color.GREEN);
//        g.fillRect((int) hitBox.getX(), (int) hitBox.getY(), (int) hitBox.getWidth(), (int) hitBox.getHeight());
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
//        if (other instanceof EnemyEntity) {
//            // remove the affected entities
//            game.removeEntity(this);
//
//            hit = true;
//        }
    }

}
