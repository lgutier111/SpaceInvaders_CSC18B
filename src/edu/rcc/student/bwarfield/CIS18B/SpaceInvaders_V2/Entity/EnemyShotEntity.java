
package edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.Entity;

import edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.GameState.GameState;
import edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.Main.GamePanel;

public class EnemyShotEntity extends Entity {

    //projectile speed
    private float shotSpeed = 500;
    //The game in which this entity exists
    private GameState game;
    //True if this shot hit something
    private boolean hit = false;

    //Create a new entity to represent the enemy
    // @param game The game in which the enemy is being created
    // @param x The initial x location
    // @param y The initial y location
    // @param ref The reference to the sprite to show for the enemy   
    public EnemyShotEntity(GameState game, int x, int y, String ref) {
        super(x, y, ref);
        this.game = game;
        dy = shotSpeed;

    }
    //return shotspeed
    //@return float shotspeed
    public float getShotSpeed() {
        return shotSpeed;
    }

    //move shot
    //@param delta time in miliseconds since last movement
    @Override
    public void move(long delta) {
        //keep moving
        super.move(delta);
        // if shot goes off the screen, remove shot
        if (y < -100 || y > GamePanel.G_HEIGHT+100 || x < -100 || x > GamePanel.G_WIDTH+100) {
            game.getRemoveList().add(this);
        }
    }

    //Notification that the player's ship has collided with something
    // @param other The entity with which the shot has collided
    @Override
    public void collidedWith(Entity other) {
        // prevents double kills, if we've already hit something, don't collide
        if (hit) {
            return;
        }

    }

}

