package edu.rcc.student.bwarfield.CIS18B.SpaceInvaders;
//Enemy projectiles

public class EnemyShotEntity extends Entity {

    //projectile speed
    private float shotSpeed = 500;
    //The game in which this entity exists
    private InvaderGame game;
    //True if this shot hit something
    private boolean hit = false;

    //Create a new entity to represent the enemy
    // @param game The game in which the enemy is being created
    // @param x The initial x location
    // @param y The initial y location
    // @param ref The reference to the sprite to show for the enemy   
    public EnemyShotEntity(InvaderGame game, int x, int y, String ref) {
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
        if (y < -50 || y > game.getHeight()+50 || x < -50 || x > game.getWIDTH()+50) {
            game.removeEntity(this);
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
