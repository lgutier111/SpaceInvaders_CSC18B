/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpaceInvaders_V4.Entity;

import SpaceInvaders_V4.GameState.GameState;
import java.awt.Point;

/**
 *
 * @author Bee-Jay
 */
public class EnemyShotLaser extends EnemyShotEntity {

    /**
     *
     */
    protected Point[] trail;

    /**
     * Enemy Laser: Creates a straight trail of projectiles
     *
     * @param game current GameState to contain entity
     * @param x horizontal position to place entity
     * @param y vertical position to place entity
     * @param ref projectile color reference (optional) default=yellow : "r"=red
     * : "p"=purple : "b"= blue : "t" = teal : "g" = green
     */
    public EnemyShotLaser(GameState game, int x, int y, String ref) {
        super(game, x, y, ref.toLowerCase().equals("r") ? "resources/sprites/enemy/enemyShot2a.png"
                : ref.toLowerCase().equals("p") ? "resources/sprites/enemy/enemyShot3a.png"
                        : ref.toLowerCase().equals("b") ? "resources/sprites/enemy/enemyShot4a.png"
                                : ref.toLowerCase().equals("t") ? "resources/sprites/enemy/enemyShot5a.png"
                                        : ref.toLowerCase().equals("g") ? "resources/sprites/enemy/enemyShot6a.png"
                                                : "resources/sprites/enemy/enemyShot1a.png");

        trail = new Point[20];
        for (int i = 0; i < trail.length; i++) {
            trail[i] = new Point(x, y);
        }
    }


    /**
     * 
     * @param delta
     * @param xShift
     */
    @Override
    public void move(Double delta, float xShift) {
        //keep moving
        x += (delta * (dx + xShift));//shift horizontally
        y += (delta * dy);//shift vertically

        for (int i = trail.length - 1; i > 0; i--) {
            trail[i].setLocation(trail[i - 1].x +(delta * xShift) , trail[i - 1].y );

        }
        trail[0].setLocation(x, y);
        //move hitbox along with sprite image
        //update hitbox location
        hitBox.setLocation((int) (x - sprite.getWidth() / 2), (int) (y - sprite.getHeight() / 2));
        hitBox.setSize(sprite.getWidth(), sprite.getHeight());
    }

    @Override
    public void draw() {
        //center sprite on x/y coords

        for (int i = trail.length - 1; i >= 0; i--) {
            sprite.draw(trail[i].x - (sprite.getWidth() / 2), trail[i].y - (sprite.getHeight() / 2));
        }

    }

    /**
     * check collision with Entity
     *
     * @param other the entity to check against
     * @return true if collision detected
     */
    @Override
    public boolean collidesWith(Entity other) {
        hitBox.setSize(sprite.getWidth(), sprite.getHeight());
        for (Point t : trail) {
            hitBox.setLocation(t.x - sprite.getWidth() / 2, t.y - sprite.getHeight() / 2);
            if (other.getHitBox().contains(hitBox)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Notification that this entity collided with another.
     *
     * @param other The entity with which this entity collided.
     */
    @Override
    public void collidedWith(Entity other) {

    }
}
