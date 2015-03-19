
package edu.rcc.student.bwarfield.CIS18B.SpaceInvaders;

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
        if (y > 700 || x < -100 || x > 900) {
            game.removeEntity(this);
        }
    }

    //shoot straight down
    public void shoot() {
        EnemyShotEntity shot = new EnemyShotEntity(this.game, (int) x+(this.sprite.getWidth()/2)-4, (int) y+(this.sprite.getHeight()), "sprites/enemyShot1a.png");
        game.addShot(shot);
        shot.setVerticalMovement(500);
    }

    //target player
    //@param player entity instance
    public void shoot(PlayerEntity ship) {
        //MATHS
        float xDiff = (ship.getX()+ship.sprite.getWidth()/2) - (x-(this.sprite.getWidth()/2));//horizontal difference between player ship and enemy
        float yDiff = (ship.getY()+ship.sprite.getHeight()/2) - (y-(this.sprite.getHeight()/2));//vertical difference between player ship and enemy
        float hyp = (float) Math.sqrt(Math.pow(Math.abs(xDiff), 2)+Math.pow(Math.abs(yDiff), 2));//hypotenuse calculation
        
        //spawn bullet at bottom middle of ship sprite (or wherever the guns will be)
        EnemyShotEntity shot = new EnemyShotEntity(this.game, (int) x+(this.sprite.getWidth()/2)-4, (int) y+(this.sprite.getHeight()), "sprites/enemyShot1a.png");
        game.addShot(shot);
        //target player ship. scale diagonal shotSpeed using trig functions 
        shot.setHorizontalMovement((float)(shot.getShotSpeed()*(xDiff/hyp)));
        shot.setVerticalMovement((float)(shot.getShotSpeed()*(yDiff/hyp)));
    }
    
    //target player
    //@param player entity instance
    public void shoot3Way(PlayerEntity ship) {
        //MATHS
        float xDiff = (ship.getX()+ship.sprite.getWidth()/2) - (x+(this.sprite.getWidth()/2));//horizontal difference between player ship and enemy
        float yDiff = (ship.getY()+ship.sprite.getHeight()/2) - (y+(this.sprite.getHeight()));//vertical difference between player ship and enemy
        float hyp = (float) Math.sqrt(Math.pow(xDiff, 2)+Math.pow(yDiff, 2));//hypotenuse calculation
        
        //spawn bullet at bottom middle of ship sprite (or wherever the guns will be)
        EnemyShotEntity shot = new EnemyShotEntity(this.game, (int) x+(this.sprite.getWidth()/2)-4, (int) y+(this.sprite.getHeight()), "sprites/enemyShot1a.png");
        game.addShot(shot);
        //target player ship. scale diagonal shotSpeed using trig functions 
        shot.setHorizontalMovement((float)(shot.getShotSpeed()*(xDiff/hyp)));
        shot.setVerticalMovement((float)(shot.getShotSpeed()*(yDiff/hyp)));
 
        //
        System.out.println("x: "+Math.asin(Math.asin(xDiff/hyp)));
        
        //spawn bullet at bottom middle of ship sprite (or wherever the guns will be)
        EnemyShotEntity shot2 = new EnemyShotEntity(this.game, (int) x+(this.sprite.getWidth()/2)-4, (int) y+(this.sprite.getHeight()), "sprites/enemyShot2a.png");
        game.addShot(shot2);
        //target player ship. scale diagonal shotSpeed using trig functions 
        shot2.setHorizontalMovement((float)(shot2.getShotSpeed()*((xDiff/hyp)+Math.toRadians(10))));
        shot2.setVerticalMovement((float)(shot2.getShotSpeed()*((yDiff/hyp)+Math.toRadians(10))));
        
        //spawn bullet at bottom middle of ship sprite (or wherever the guns will be)
        EnemyShotEntity shot3 = new EnemyShotEntity(this.game, (int) x+(this.sprite.getWidth()/2)-4, (int) y+(this.sprite.getHeight()), "sprites/enemyShot3a.png");
        game.addShot(shot3);
        //target player ship. scale diagonal shotSpeed using trig functions 
        shot3.setHorizontalMovement((float)(shot3.getShotSpeed()*((xDiff/hyp)-Math.toRadians(10))));
        shot3.setVerticalMovement((float)(shot3.getShotSpeed()*((yDiff/hyp)-Math.toRadians(10))));
    }

    @Override
    public void collidedWith(Entity other) {
        //nothing yet
    }

}
