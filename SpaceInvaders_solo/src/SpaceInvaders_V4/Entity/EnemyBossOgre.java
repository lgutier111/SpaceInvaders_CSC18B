package SpaceInvaders_V4.Entity;

import SpaceInvaders_V4.GameState.GameState;
import SpaceInvaders_V4.Main.ResourceFactory;
import SpaceInvaders_V4.Users.Score;
import SpaceInvaders_V4.Util.Sprite;
import SpaceInvaders_V4.Util.SystemTimer;
import java.awt.Color;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;
import java.util.ArrayList;

/**
 *
 * @author Travis
 */
public class EnemyBossOgre extends EnemyEntity {

    private final ArrayList<Turret> turretList;//list of turrets
    private final ArrayList<Entity> removeTurret;//list of turrets to remove
    private final ArrayList<Entity> effectsList;//array list to hold fire Entity
    private int currentTurret = 0;
    private double deadTimer = 0;

    /**
     *
     * @param game gate in which to add Entity
     * @param rank difficulty scaling factor (minimum 1)
     * @param x horizontal position
     * @param y vertical position
     * @param ref unused flag
     */
    public EnemyBossOgre(GameState game, int rank, int x, int y, String ref) {
        super(game, rank * 15, x, y, "resources/sprites/enemy/ogre.png");

        this.turretList = new ArrayList<>();
        this.removeTurret = new ArrayList<>();
        this.effectsList = new ArrayList<>();

        Turret t1 = new Turret(game, rank, x - 87, y + 22, "", this);
        turretList.add(t1);
        Turret t2 = new Turret(game, rank, x + 87, y + 22, "", this);
        turretList.add(t2);
        Turret t3 = new Turret(game, rank, x - 64, y - 63, "", this);
        turretList.add(t3);
        Turret t4 = new Turret(game, rank, x + 64, y - 63, "", this);
        turretList.add(t4);
        Turret t5 = new Turret(game, rank, x - 39, y - 43, "", this);
        turretList.add(t5);
        Turret t6 = new Turret(game, rank, x + 39, y - 43, "", this);
        turretList.add(t6);
        Turret t7 = new Turret(game, rank, x, y - 70, "", this);
        turretList.add(t7);

        speed = 150;
        angle = 0;

    }

    /**
     * Check collision
     *
     * @param other Entity against which to check collision
     * @return true if collision detected
     */
    @Override
    public boolean collidesWith(Entity other) {
        boolean collide = false;
        if (turretList.isEmpty()) {

            return hitBox.intersects(other.getHitBox());
        } else {

            for (int i = 0; i < turretList.size(); i++) {
                if (turretList.get(i).collidesWith(other)) {
                    turretList.get(i).collidedWith(other);
                    collide = true;
                    break;
                }
            }
            turretList.removeAll(removeTurret);
            removeTurret.clear();
            return collide;
        }
    }

    /**
     * Apply movement, firing, turrets, and effects logic
     */
    @Override
    public void doLogic() {
        super.doLogic();
        gunX = (int) x;
        gunY = (int) (y - 100);
        if (!dead) {
            if (moveTicks > lastMove) {
                if (moveTicks == 1) {//init direction 
                    angle = 0;
                    speed = 170;

                } else if (moveTicks > 1 && moveTicks < 150) {
                    if (speed > 0) {
                        if (speed - 1 > 0 && y < 200) {
                            speed -= 2;
                        } else {
                            speed = 0;

                        }
                    }
                } else if (moveTicks == 150) {
                    speed = 0;
                    angle = 90;

                } else if (moveTicks > 150 && turretList.isEmpty()) {
                    if (moveTicks % 100 == 0) {
                        angle = x >= 350 ? -90 : 90;
                    } else if (moveTicks % 100 > 0 && moveTicks % 100 < 50) {
                        if (speed < 100) {
                            speed += 3;
                        }
                    } else if (moveTicks % 100 > 50 && moveTicks % 100 < 100) {
                        if (speed > 0) {
                            speed -= 2;
                        }
                    }
                }

                lastMove = moveTicks;
            }
            if (shotTicks > lastShot) {
                if (turretList.size() > 0) {
                    if (currentTurret >= turretList.size()) {
                        currentTurret = 0;
                    }

                    if (shotTicks % 108 == 8 || shotTicks % 108 == 18 || shotTicks % 108 == 28 || shotTicks % 108 == 38 || shotTicks % 108 == 48 || shotTicks % 108 == 58 || shotTicks % 108 == 68) {
                        turretList.get(currentTurret).shoot(350, target, -25);
                        turretList.get(currentTurret).shoot(350, target, 0);
                        turretList.get(currentTurret).shoot(350, target, 25);
                    } else if (shotTicks % 10 == 0) {
                        currentTurret++;
                    }

                } else {
                    if (shotTicks % 90 == 5 || shotTicks % 90 == 15 || shotTicks % 90 == 25) {
                        shootHomingLaser("");
                        shootHomingLaser("");
                        shootHomingLaser("");
                    }
                }
                lastShot = shotTicks;
            }

        } else {//if dead
            hitBox.setSize(0, 0);
            hitBox.setLocation(-500, -500);
            if (SystemTimer.getTime() < deadTimer) {
                if (moveTicks > lastMove) {
                    if (moveTicks % 2 == 0) {
                        int ve = (int) (x - 125 + Math.random() * 250);
                        int he = (int) (y - 150 + Math.random() * 300);
                        MedExplosion me = new MedExplosion(game, ve, he, Math.random() > 0.5 ? "long" : "");
                        game.getEffects().add(me);
                        Fire fire = new Fire(ve, he, Math.random() > 0.5 ? "large" : "med");
                        effectsList.add(fire);
                        if (moveTicks % 100 == 75 || moveTicks % 100 == 80 || moveTicks % 100 == 85 || moveTicks % 100 == 90 || moveTicks % 100 == 95) {
                            LargeExplosion le = new LargeExplosion(game,ve, he, "");
                            game.getEffects().add(le);
                        }
                    }
                    if (moveTicks % 50 == 0) {
                        LargeExplosion le = new LargeExplosion(game, (int) x, (int) y, "shockwave");
                        game.getEffects().add(le);
                    }
                    lastMove = moveTicks;
                }
            } else {
                LargeExplosion le = new LargeExplosion(game, (int) x, (int) y, "");
                game.getEffects().add(le);
                game.getRemoveEnemyList().add(this);
            }
        }

        setHorizontalMovement((float) (sin(toRadians(angle)) * speed));
        setVerticalMovement((float) (cos(toRadians(angle)) * speed));

        hitBox.setLocation((int) (x - sprite.getWidth() / 2) + 20, (int) (y - sprite.getHeight() / 2) + 35);
        hitBox.setSize(sprite.getWidth() - 40, sprite.getHeight() - 70);

        for (int i = 0; i < turretList.size(); i++) {
            turretList.get(i).doLogic();
        }
        for (int i = 0; i < effectsList.size(); i++) {
            effectsList.get(i).doLogic();
        }
    }

    /**
     * move method based on timing system
     *
     * @param delta the amount of time passed in seconds
     * @param xShift horizontal alignment parameter
     */
    @Override
    public void move(Double delta, float xShift) {
        //update location based on move speed
        x += (delta * (dx + xShift));//shift horizontally
        y += (delta * dy);//shift vertically

        for (int i = 0; i < turretList.size(); i++) {
            turretList.get(i).move(delta, dx + xShift, dy);
        }
        for (int i = 0; i < effectsList.size(); i++) {
            effectsList.get(i).move(delta, dx + xShift, dy);
        }
    }

    /**
     * Draw Entity to graphics context
     */
    @Override
    public void draw() {
        //draw shadow image under ship
        Sprite shadow = ResourceFactory.get().getShadedSprite(sprite.getRef(), Color.BLACK, 0.5f);
        shadow.draw((int) x - (sprite.getWidth() / 2) + 20, (int) y - (sprite.getHeight() / 2) + 20);

        //center sprite on x/y coords
        sprite.draw((int) x - (sprite.getWidth() / 2), (int) y - (sprite.getHeight() / 2));

        //if enemy has been hit, flash a colored frame
        if (hit) {
            Sprite dmg = ResourceFactory.get().getShadedSprite(sprite.getRef(), Color.WHITE, 0.7f);
            dmg.draw((int) x - (dmg.getWidth() / 2), (int) y - (dmg.getHeight() / 2));
            hit = false;
        }

        for (int i = 0; i < turretList.size(); i++) {
            turretList.get(i).draw();
        }
        for (int i = 0; i < effectsList.size(); i++) {
            effectsList.get(i).draw();
        }

        //draw hitbox for debugging
//        (ResourceFactory.get().getGameWindow()).fillRect(Color.RED, hitBox);
    }

    /**
     * Check collision with Entity
     *
     * @param other Entity against which to check collisions
     */
    @Override
    public void collidedWith(Entity other) {

        if (dead || turretList.size() > 0) {
            return;
        }

        // if shot, take damage
        if (other instanceof ShotEntity) {
            health -= ((ShotEntity) other).getDmg();
            hit = true;

            // is dead, start death sequence
            if (health <= 0) {
                dead = true;
                Score.addKill();
                Score.addScore(value);

                //create shockwave explosion
                LargeExplosion le = new LargeExplosion(game, (int) x, (int) y, "shockwave");
                game.getEffects().add(le);
                speed = 0;
                //set death timers
                deadTimer = SystemTimer.getTime() + 5;
                moveTicks = lastMove = 0;

            }

        }
    }

    /**
     * Get Turret remove List
     *
     * @return list of active Turret Entities to remove
     */
    ArrayList<Entity> getRemoveTurret() {
        return removeTurret;
    }

    /**
     * Get Turret List
     *
     * @return list of active Turret Entities
     */
    public ArrayList<Entity> getEffects() {
        return effectsList;
    }

    /**
     * Shoot homing laser
     *
     * @param color projectile color reference (optional) default=yellow :
     * "r"=red : "p"=purple : "b"= blue : "t" = teal : "g" = green
     */
    void shootHomingLaser(String color) {
        EnemyShotEntity shot = new EnemyShotHomingLaser(this.game, gunX, gunY, color);
        game.getEnemyEntities().add(shot);
    }
}

class Turret extends EnemyEntity {

    AffineTransform at;
    EnemyBossOgre boss;
    float rads;

    public Turret(GameState game, int rank, int x, int y, String ref, EnemyBossOgre boss) {
        super(game, rank, x, y, "resources/sprites/enemy/ogreTurret.png");
        this.boss = boss;
    }

    @Override
    public void doLogic() {
        super.doLogic();
        at = new AffineTransform();
        rads = (float) (atan2(target.getX() - x, target.getY() - y));

    }

    /*target at angle to entity
     *@param int projectile speed in px/sec
     *@param Entity entity instance
     *@param deg in degrees to adjust shot. Positive being ccw, negative being cw, 0 being on target
     */
    @Override
    public void shoot(int speed, Point point, int deg) {
        float shipRads;
        shipRads = (float) (atan2((point.getX() - gunX), (point.getY() - gunY)));

        //spawn bullet at bottom middle of ship sprite (or wherever the guns will be)
        EnemyShotEntity shot = new EnemyShotLaser(this.game, gunX, gunY, "");
        //target player ship. scale diagonal shotSpeed using trig functions 
        shot.setHorizontalMovement((float) (speed * sin(shipRads + toRadians(deg))));
        shot.setVerticalMovement((float) (speed * cos(shipRads + toRadians(deg))));
        game.getEnemyEntities().add(shot);

    }

    @Override
    public void draw() {

        at.translate(x, y);
        at.rotate(-rads);
        at.translate(-sprite.getWidth() / 2, -sprite.getHeight() / 2);

        sprite.drawRotate(at);

        //center sprite on x/y coords
        if (hit) {
            Sprite dmg = ResourceFactory.get().getShadedSprite(sprite.getRef(), Color.WHITE, 0.7f);
            dmg.drawRotate(at);
            hit = false;
        }

        //draw hitbox for debugging
//        (ResourceFactory.get().getGameWindow()).fillRect(Color.MAGENTA, hitBox);
    }

    @Override
    public void collidedWith(Entity other) {

        if (dead) {
            return;
        }

        // if shot, take damage
        if (other instanceof ShotEntity) {
            health -= ((ShotEntity) other).getDmg();
            hit = true;

            // remove the affected entities
            if (health <= 0) {
                dead = true;
                Score.addKill();
                Score.addScore(value);
                boss.getRemoveTurret().add(this);
                Fire fire = new Fire((int) x, (int) y, "large");
                boss.getEffects().add(fire);
                MedExplosion me = new MedExplosion(game, (int) x, (int) y, "long");
                game.getEffects().add(me);

            }

        }
    }

}
