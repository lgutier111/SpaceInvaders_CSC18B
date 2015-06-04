/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package SpaceInvaders_V3.Entity;


import SpaceInvaders_V3.GameState.GameState;
import SpaceInvaders_V3.Java2D.Java2DGameWindow;
import SpaceInvaders_V3.Main.ResourceFactory;
import SpaceInvaders_V3.Util.Sprite;
import SpaceInvaders_V3.Util.SystemTimer;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;


/**
 *
 * @author rcc
 */   
 public class BossEntity extends EnemyEntity {

  //  protected int turrets = 7;
    
    protected int bossSpeed= 50;
    
    protected int end = 120;
    
    //turret end location has to be 2 more than y
    TurretLarge turret1L = new TurretLarge(this.game, 3, (int)x-87, (int)y+22, end+24, bossSpeed, "");
    TurretLarge turret2L = new TurretLarge(this.game, 3, (int)x+87, (int)y+22, end+24, bossSpeed, "");
    TurretLarge turret3L = new TurretLarge(this.game, 3, (int)x-64, (int)y-63, end-61, bossSpeed, "");
    TurretLarge turret4L = new TurretLarge(this.game, 3, (int)x+64, (int)y-63, end-61, bossSpeed, "");
    TurretLarge turret5L = new TurretLarge(this.game, 3, (int)x-39, (int)y-43, end-41, bossSpeed, "");
    TurretLarge turret6L = new TurretLarge(this.game, 3, (int)x+39, (int)y-43, end-41, bossSpeed, "");
    TurretLarge turret7L = new TurretLarge(this.game, 3, (int)x, (int)y+70, end+72, bossSpeed, "");        
            
            
    protected boolean t1LDead, t2LDead, t3LDead, t4LDead, t5LDead, t6LDead, t7LDead;        
            
            
            
            
    public BossEntity(GameState game, int rank, int x, int y, String ref) {
        
        super(game, rank, x, y, "resources/sprites/enemy/Boss_ship.png");

        this.rank = 4;
        this.speed = bossSpeed;
    //    this.turrets=turrets;
        this.t1LDead=this.t2LDead=this.t3LDead=this.t4LDead=this.t5LDead=this.t6LDead=this.t7LDead=false; 
       
       game.getEnemyEntities().add(turret1L);
       turret1L.setVerticalMovement(speed);
       game.getEnemyEntities().add(turret2L);
       turret2L.setVerticalMovement(speed);
       game.getEnemyEntities().add(turret3L);
       turret3L.setVerticalMovement(speed);
       game.getEnemyEntities().add(turret4L);
       turret4L.setVerticalMovement(speed);
       game.getEnemyEntities().add(turret5L);
       turret5L.setVerticalMovement(speed);
       game.getEnemyEntities().add(turret6L);
       turret6L.setVerticalMovement(speed);
       game.getEnemyEntities().add(turret7L);
       turret7L.setVerticalMovement(speed);
       
        
    }

    
    @Override
    public void doLogic() {
        
        elapsed = SystemTimer.getTime() - startTime;
        if (SystemTimer.getTime() - ticker > 0.04) {//approx 25ticks/sec
            moveTicks++;
            shotTicks++;
            ticker = SystemTimer.getTime();
        }

        
        //update hitbox location
        hitBox.setLocation((int) (x - sprite.getWidth() / 2 +10),
                           (int) (y - sprite.getHeight() / 2 + 20));
        
        if(t1LDead && t2LDead && t3LDead && t4LDead && t5LDead && t6LDead && t7LDead){
            hitBox.setSize(sprite.getWidth()-20, sprite.getHeight()-45);
        }else{
            hitBox.setSize(0,0);
        }
        
        //update bullet origin
        this.gunX =(int) x;
        this.gunY =(int) y + (this.sprite.getHeight() / 2);

        if(y<end){
          setVerticalMovement(speed);   
        }else{
          setVerticalMovement(0);     
        }

        setHorizontalMovement(0);
        
        
        

    }
    
    
    //override draw method
    @Override
    public void draw() {

        //draw shadow image under ship
        Sprite shadow = ResourceFactory.get().getShadedSprite(sprite.getRef(), Color.BLACK, 0.5f);
        shadow.draw((int)(x - 115), (int)(y - 160));

        //draw ship
        //center sprite on x/y coords
        sprite.draw((int) x - (sprite.getWidth() / 2), (int) y - (sprite.getHeight() / 2));
          

        //if enemy has been hit, flash a colored frame
        if (hit) {
            Sprite dmg = ResourceFactory.get().getShadedSprite(sprite.getRef(),  Color.WHITE, 0.7f);
            dmg.draw((int) x - (dmg.getWidth() / 2), (int) y - (dmg.getHeight() / 2));
            hit = false;
        }
        
        //Draw the turrets ontop of the boss
        if(turret1L.isDead()){      
            t1LDead=true;      //if its dead flag it
        }else{
        //draw the turret
            drawTurret(turret1L);
        }
        
        if(turret2L.isDead()){
            t2LDead=true;
           
        }else{
         //draw the turret
            drawTurret(turret2L);
        }
        
        if(turret3L.isDead()){
            t3LDead=true;     
        }else{
         //draw the turret
            drawTurret(turret3L);
        }
  
        if(turret4L.isDead()){
            t4LDead=true;
            
        }else{
          //draw the turret
            drawTurret(turret4L);
        }

        if(turret5L.isDead()){
            t5LDead=true;
 
        }else{
         //draw the turret
            drawTurret(turret5L);
        }  
        
        if(turret6L.isDead()){
            t6LDead=true;
            
        }else{
           //draw the turret
            drawTurret(turret6L);

        }
        
        if(turret7L.isDead()){
            t7LDead=true;
            
        }else{
            //draw the turret
            drawTurret(turret7L);
 
        }    
    }
    
    @Override
    public void collidedWith(Entity other) {

        if (dead) {
            return;
        }

        // if shot, take damage
        if (other instanceof ShotEntity) {
        //    health -= ((ShotEntity) other).getDmg();
            hit = true;

            // remove the affected entities
            if (health <= 0) {
                dead = true;
                game.getRemoveEnemyList().add(this);

            }

        }
    }
    
    
    public void turret1(int speed, float x, float y, int end) {

        //spawn turret
        TurretLarge turret = new TurretLarge(this.game, 3, (int)x, (int)y, end, speed, "");
        game.getEnemyEntities().add(turret);
        
        turret.setHorizontalMovement(0);
        turret.setVerticalMovement(speed);
    }
    
    
    public void drawTurret(TurretLarge ent){
        
        
        AffineTransform at = new AffineTransform();
        at.translate(ent.getX(), ent.getY() );
        at.rotate(-ent.rads);
        at.translate(-ent.sprite.getWidth()/2, -ent.sprite.getHeight()/2);

        ent.sprite.drawRotate(at);

            //if enemy has been hit, flash a colored frame
            if (ent.hit) {
                Sprite dmg = ResourceFactory.get().getShadedSprite(ent.sprite.getRef(),  Color.WHITE, 0.7f);
                dmg.draw((int) ent.x - (dmg.getWidth() / 2), (int) ent.y - (dmg.getHeight() / 2));
                ent.hit = false;
            } 
    }
    

 }
