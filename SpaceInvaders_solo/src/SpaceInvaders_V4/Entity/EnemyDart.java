package SpaceInvaders_V4.Entity;

import SpaceInvaders_V4.GameState.GameState;
import static java.lang.Math.*;

public class EnemyDart extends EnemyEntity {

    float turn = 0.5f;

    public EnemyDart(GameState game, int rank, int x, int y, String ref) {

        super(game, rank, x, y, "resources/sprites/enemy/enemyship2a.png");
        try {
            this.pattern = Integer.parseInt(ref);
        } catch (NumberFormatException e) {
            //default setting shoud ref translation fail
            this.pattern = 0;
            this.angle = 0;
            this.speed = 150;
        }
    }

    @Override
    public void doLogic() {
        super.doLogic();

        //movement script
        if (moveTicks > lastMove) {
            if (pattern == 1) {

            } else {
                if (moveTicks == 1) {
                    angle = 0;
                    speed = (int) (300 + random() * 50 * rank);
                } else if (moveTicks > 1 && moveTicks < 35) {
                    if (speed > 0) {
                        speed -= 10;
                       
                    }
                }else if(moveTicks == 35){
                     turn = x - target.getX() < 0 ? 1 :-1;
                } 
                else if (moveTicks > 35) {
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
            }
            setHorizontalMovement((float) (sin(toRadians(angle)) * speed));
            setVerticalMovement((float) (cos(toRadians(angle)) * speed));
            lastMove = moveTicks;
        }
        //shooting script
        if (shotTicks > lastShot) {
            if (pattern == 1) {
            } else {
                if (shotTicks % (int) (50 - random() * rank * 10) == 0) {
                    shoot(150 + 20 * rank, target, 0);
                    shotTicks = 1;
                }
            }
            lastShot = shotTicks;
        }
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
                game.getRemoveEnemyList().add(this);
                SmallExplosion sm = new SmallExplosion(game, (int) x, (int) y, "");
                sm.setHorizontalMovement(dx * 0.6f);
                sm.setVerticalMovement(dy * 0.6f);
                game.getEffects().add(sm);
            }

        }
    }

}
