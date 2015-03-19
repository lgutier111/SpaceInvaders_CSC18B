package edu.rcc.student.bwarfield.CIS18B.SpaceInvaders;

//entity that represents the player's ship
public class PlayerEntity extends Entity {

    //the game in which the ship exists
    private InvaderGame game;

    //Create a new entity to represent the players ship
    // @param game The game in which the ship is being created
    // @param x The initial x location of the player's ship
    // @param y The initial y location of the player's ship
    // @param ref The reference to the sprite to show for the ship
    public PlayerEntity(InvaderGame game, int x, int y, String ref) {
        super(x, y, ref);
        this.game = game;
    }

    //move ship
    //@param delta time in miliseconds since last movement
    @Override
    public void move(long delta) {
        //set bounds to prevent movement offscreen
        if ((dx < 0) && (x <= 10)) {//right bound
            dx = 0;
            x=10;
        }
        if ((dx > 0) && (x >= game.getWIDTH() - this.sprite.getWidth()-10)) {//left bound
            dx = 0;
            x=game.getWIDTH() - this.sprite.getWidth()-10;
        }
        if ((dy < 0) && (y <= 10)) {//upper bound
            dy = 0;
            y=10;
        }
        if ((dy > 0) && (y >= game.getHEIGHT() - this.sprite.getHeight()-10)) {//lower bound
            dy = 0;
            y = game.getHEIGHT() - this.sprite.getHeight()-10;
        }

        super.move(delta);
    }

    //Notification that the player's ship has collided with something
    // @param other The entity with which the ship has collided
    public void collidedWith(Entity other) {
        // if its an alien, notify the game that the player is dead
//        if (other instanceof EnemyEntity) {
//            game.notifyDeath();
//
//        }
    }

}
