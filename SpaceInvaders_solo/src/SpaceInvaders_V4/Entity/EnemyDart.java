package SpaceInvaders_V4.Entity;

import SpaceInvaders_V4.GameState.GameState;
import SpaceInvaders_V4.Main.ResourceFactory;
import SpaceInvaders_V4.Users.Score;
import SpaceInvaders_V4.Util.Sprite;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import static java.lang.Math.*;

/**
 * Basic enemy class
 * @author Bee-Jay
 */
public class EnemyDart extends EnemyEntity {

    float turn = 0.5f;
    AffineTransform at = new AffineTransform();

    /**
     *
     * @param game current gameState
     * @param rank health/value factor
     * @param x horizontal position
     * @param y vertical position
     * @param ref optional flag, "b" for bronze, "c" for copper, default green
     */
    public EnemyDart(GameState game, int rank, int x, int y, String ref) {

        super(game, rank, x, y, ref.equals("c") ? "resources/sprites/enemy/enemyship2c.png" : ref.equals("b") ? "resources/sprites/enemy/enemyship2b.png" : "resources/sprites/enemy/enemyship2a.png");

        //default setting 
        this.angle = 0;
        this.speed = 150;

    }

    @Override
    public void doLogic() {
        super.doLogic();

        //movement script
        if (moveTicks > lastMove) {

            if (moveTicks == 1) {
                angle = 0;
                speed = (int) (300 + random() * 50 * rank);
            } else if (moveTicks > 1 && moveTicks < 35) {
                if (speed > 0) {
                    speed -= 10;

                }
            } else if (moveTicks == 35) {
                turn = x - target.getX() < 0 ? 1 : -1;
            } else if (moveTicks > 35) {
                if (speed < 150) {
                    speed += 4;
                }
                if (angle <= 85f || angle >= 275f) {
                    angle += turn;
                } else if (angle > 85 && angle < 180) {
                    angle = 85;
                } else if (angle < 275 && angle < 180) {
                    angle = 275;
                }

            }
            setHorizontalMovement((float) (sin(toRadians(angle)) * speed));
            setVerticalMovement((float) (cos(toRadians(angle)) * speed));
            lastMove = moveTicks;
        }
        //shooting script
        if (shotTicks > lastShot) {

            if (shotTicks % 40 == 0) {
                shoot(150 + 20 * rank, target, 0);
                
            }

            lastShot = shotTicks;
        }
    }

    @Override
    public void draw() {
        Sprite shadow = ResourceFactory.get().getShadedSprite(sprite.getRef(), Color.BLACK, 0.5f);
        
        at.setToIdentity();
        at.translate(x, y);

        at.rotate(-toRadians(angle));
        
        shadow.drawRotate(at);
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
                game.getRemoveEnemyList().add(this);
                SmallExplosion sm = new SmallExplosion(game, (int) x, (int) y, "");
                sm.setHorizontalMovement(dx * 0.6f);
                sm.setVerticalMovement(dy * 0.6f);
                game.getEffects().add(sm);
            }

        }
    }

}
