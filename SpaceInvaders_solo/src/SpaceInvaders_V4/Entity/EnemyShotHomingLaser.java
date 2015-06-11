package SpaceInvaders_V4.Entity;

import SpaceInvaders_V4.GameState.GameState;
import SpaceInvaders_V4.Util.SystemTimer;
import java.awt.Point;
import static java.lang.Math.*;

/**
 *
 * @author Bee-Jay
 */
public class EnemyShotHomingLaser extends EnemyShotLaser {

    private double ticker = 0;
    private int ticks = 0;
    private int lastTick = 0;
    private float angle;
    private int speed;
    private Point target = new Point(0, 0);
    private double targetAngle;
    private double angleDistance;

    /**
     * Create homing laser entity
     *
     * @param game GameState in which to add laser
     * @param x Horizontal Position
     * @param y Vertical Position
     * @param ref projectile color reference (optional) default=yellow : "r"=red
     * : "p"=purple : "b"= blue : "t" = teal : "g" = green
     */
    public EnemyShotHomingLaser(GameState game, int x, int y, String ref) {
        super(game, x, y, ref);
        angle = (float) (random() * 360);
        speed = 250;
    }

    /**
     * Execute movement logic
     */
    @Override
    public void doLogic() {

        //set target to random player position if one exists, else tart is straight down
        if (game.getPlayers().size() > 0) {
            Entity player = (Entity) game.getPlayers().get((int) (random() * (game.getPlayers().size() - 1)));
            target.setLocation(player.getX(), player.getY());
        } else {
            target.setLocation(x, y + 30);

        }

        angle = (angle + 360) % 360;//bind range between 0 and 360 
        targetAngle = toDegrees(atan2(target.x - x, target.y - y));
        angleDistance = targetAngle - angle;

        if (angleDistance > 180) {
            angleDistance -= 360;
        } else if (angleDistance < -180) {
            angleDistance += 360;
        }

        if (SystemTimer.getTime() - ticker > 0.04) {
            ticks++;
            ticker = SystemTimer.getTime();
        }

        if (ticks > lastTick) {

            if (ticks > 0 && ticks < 40) {
                if (speed > 10) {
                    speed -= 10;
                }
            } else {
                if (speed < 1500) {
                    speed += 30;
                }
            }
//            if (ticks % 2 == 0) {
//                if (angleDistance > 0) {
//                    angle += 5f;
//                } else {
//                    angle -= 5f;
//                }
                angle += angleDistance * 0.1f;
//            }

            setHorizontalMovement((float) (sin(toRadians(angle)) * speed));
            setVerticalMovement((float) (cos(toRadians(angle)) * speed));

            lastTick = ticks;
        }
    }
}
