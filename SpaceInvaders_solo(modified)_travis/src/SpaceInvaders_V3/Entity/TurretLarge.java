/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpaceInvaders_V3.Entity;

import SpaceInvaders_V3.GameState.GameState;
import SpaceInvaders_V3.Main.ResourceFactory;
import SpaceInvaders_V3.Util.Sprite;
import SpaceInvaders_V3.Util.SystemTimer;
import java.awt.Color;
import static java.lang.Math.atan2;
import static java.lang.Math.random;

/**
 *
 * @author Travis
 */
public class TurretLarge extends EnemyEntity{

    protected int end;
    protected float rads;
    
    public TurretLarge(GameState game, int rank, int x, int y, int end, int speed, String ref) {
        super(game, rank, x, y, "resources/sprites/enemy/turret_large.png");
        this.end = end;
        this.speed = speed;
    }
    

  @Override
  public void doLogic() {
        

         if (shotTicks % (int) (100 - random() * rank * 10) == 0) {
                shoot(150+20*rank, target, 0);
                shotTicks = 1;
            }
          
        elapsed = SystemTimer.getTime() - startTime;
        if (SystemTimer.getTime() - ticker > 0.04) {//approx 25ticks/sec
            moveTicks++;
            shotTicks++;
            ticker = SystemTimer.getTime();
        }

        
        //update hitbox location
        hitBox.setLocation((int) (x - sprite.getWidth() / 2 ), (int) (y - sprite.getHeight() / 2 ));
        hitBox.setSize(sprite.getWidth(), sprite.getHeight());
        
        
        
        //update bullet origin
        this.gunX =(int) x;
        this.gunY =(int) y + (this.sprite.getHeight() / 2);

        if(y<this.end){
          setVerticalMovement(speed);
        }else{
          setVerticalMovement(0);
        }

        setHorizontalMovement(0);
        
        rads = (float)(atan2(target.getX()  - (x + sprite.getWidth() / 2), target.getY()  - (y+sprite.getHeight() / 2)));
        

  }
    
    @Override
    public void draw() {

//        rads = (float) (atan2((target.getX() - gunX),(target.getY() - gunY)));
//        
//   //     sprite.rotate(rads);
//        
//        sprite.draw((int) x - (sprite.getWidth() / 2), (int) y - (sprite.getHeight() / 2));
//
//        //if enemy has been hit, flash a colored frame
//        if (hit) {
//            Sprite dmg = ResourceFactory.get().getShadedSprite(sprite.getRef(),  Color.WHITE, 0.7f);
//            dmg.draw((int) x - (dmg.getWidth() / 2), (int) y - (dmg.getHeight() / 2));
//            hit = false;
//        }

    }
}
