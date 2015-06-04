package SpaceInvaders_V3.Entity;

import SpaceInvaders_V3.GameState.GameState;
import static java.lang.Math.*;

public class EnemyDart extends EnemyEntity {

    public EnemyDart(GameState game, int rank, int x, int y, String ref) {
        
        super(game, rank, x, y, "resources/sprites/enemy/enemyship2a.png");
        try {
            this.pattern = Integer.parseInt(ref);
        } catch (NumberFormatException e) {
            this.pattern = 0;
            this.angle = 0;
            this.speed = 150;
        }
    }

    @Override
    public void doLogic() {
        super.doLogic();

        angle = angle % 360;

        if (pattern == 1) {

        } else {
            if (moveTicks == 1) {
                angle = 0;
                speed = (int) (300 + random() * 30 * rank);
            } else if (moveTicks > 1 && moveTicks < 35) {
                if (speed > 0) {
                    speed -= 4;
                }
            } else if (moveTicks >= 35) {
                if (speed < 150) {
                    speed += 2;
                }
                if (angle < 85f || angle > 275f) {
                    if (x - target.getX() < 0) {
                        angle += 0.75f;
                    } else {
                        angle -= 0.75f;
                    }
                }
            }
        }
        setHorizontalMovement((float) (sin(toRadians(angle)) * speed));
        setVerticalMovement((float) (cos(toRadians(angle)) * speed));

        if (pattern == 1) {
        } else {
            if (shotTicks % (int) (40 - random() * rank * 10) == 0) {
                shoot(150+20*rank, target, 0);
                shotTicks = 1;
            }
        }
    }

}
