package SpaceInvaders_V4.Entity;

import SpaceInvaders_V4.GameState.GameState;
import SpaceInvaders_V4.Main.ResourceFactory;
import java.awt.Color;
import java.awt.Rectangle;

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

    @Override
    public void draw() {
        super.draw();

        //debug hitbox position
//        (ResourceFactory.get().getGameWindow()).fillRect(Color.orange, hitBox);
    }

    //move shot
    //@param delta time in miliseconds since last movement
    @Override
    public void move(Double delta) {
        //keep moving
        super.move(delta);

        //move hitbox along with sprite image
        //update hitbox location
        hitBox.setLocation((int) (x - sprite.getWidth() / 2), (int) (y - sprite.getHeight() / 2));
        hitBox.setSize(sprite.getWidth(), sprite.getHeight());
    }
    
    //move shot
    //@param delta time in miliseconds since last movement
    @Override
    public void move(Double delta, float xShift) {
        //keep moving
        super.move(delta, xShift);

        //move hitbox along with sprite image
        //update hitbox location
        hitBox.setLocation((int) (x - sprite.getWidth() / 2), (int) (y - sprite.getHeight() / 2));
        hitBox.setSize(sprite.getWidth(), sprite.getHeight());
    }

    @Override
    public void doLogic(){
        //bound check. Remove entity if it strays too far off entity map
            if (x + sprite.getWidth()/2 < -10
                    || x - sprite.getWidth()/2  > ResourceFactory.get().getGameWindow().getWidth() + 10
                    || y - sprite.getHeight()/2  > ResourceFactory.get().getGameWindow().getHeight() + 10
                    || y + sprite.getHeight()/2  < - 10) {
                game.getRemoveEnemyList().add(this);
            }
    }
    
    public boolean isHit(){
        return hit;
    }
    
    //Notification that the player's ship has collided with something
    // @param other The entity with which the shot has collided
    @Override
    public void collidedWith(Entity other) {
        // prevents double kills, if we've already hit something, don't collide
        if (hit) {
            return;
        }
        if (other instanceof PlayerEntity) {
            hit = true;
            game.getRemoveEnemyList().add(this);
        }

    }

}
