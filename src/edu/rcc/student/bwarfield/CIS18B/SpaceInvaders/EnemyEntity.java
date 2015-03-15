/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rcc.student.bwarfield.CIS18B.SpaceInvaders;

/**
 *
 * @author Bee-Jay
 */
public class EnemyEntity extends Entity {

    //the game in which the enemy exists
    private InvaderGame game;

    //Create a new entity to represent the enemy
    // @param game The game in which the enemy is being created
    // @param x The initial x location
    // @param y The initial y location
    // @param ref The reference to the sprite to show for the enemy
    public EnemyEntity(InvaderGame game, int x, int y, String ref) {
        super(x, y, ref);
        this.game = game;
    }
    //move enemy
    //@param delta time in miliseconds since last movement
    @Override
    public void move(long delta) {
        //keep moving
        super.move(delta);
        // if enemy goes off the screen, remove enemy
        if (y > 700 || x<-100 ||x>900) {
           game.removeEntity(this);
        }
    }

    @Override
    public void collidedWith(Entity other) {
        //nothing yet
    }

}
