package SpaceInvaders_V4.Entity;

import SpaceInvaders_V4.GameState.GameState;
import SpaceInvaders_V4.Main.ResourceFactory;
import SpaceInvaders_V4.Users.Score;
import SpaceInvaders_V4.Util.Sprite;
import SpaceInvaders_V4.Util.SystemTimer;
import java.awt.Color;
import static java.lang.Math.cos;
import static java.lang.Math.random;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

/**
 *
 * @author Bee-Jay
 */
public class EnemySwarmer extends EnemyEntity {

    private Sprite[] frames;
    private Sprite[] shadowFrames;
    private Sprite[] hitFrames;
    private int currentFrame;
    private double lastFrameChange = 0;

    /**
     *
     * @param game
     * @param rank
     * @param x
     * @param y
     * @param ref
     */
    public EnemySwarmer(GameState game, int rank, int x, int y, String ref) {
        super(game, rank, x, y, ref.equals("p") ? "resources/sprites/enemy/swarmer_p.png" : "resources/sprites/enemy/swarmer_r.png");

        frames = new Sprite[9];
        shadowFrames = new Sprite[frames.length];
        hitFrames = new Sprite[frames.length];

        int width = sprite.getWidth() / frames.length;
        int height = sprite.getHeight();

        for (int col = 0; col < frames.length; col++) {
            frames[col] = ResourceFactory.get().getSubSprite(sprite.getRef(), col * width, 0, width, height);
            shadowFrames[col] = ResourceFactory.get().getShadedSubSprite(sprite.getRef(), col * width, 0, width, height, Color.black, 0.5f);
            hitFrames[col] = ResourceFactory.get().getShadedSubSprite(sprite.getRef(), col * width, 0, width, height, Color.white, 0.7f);
        }

        currentFrame = 0;
        sprite = frames[currentFrame];

    }

    @Override
    public void doLogic() {
        super.doLogic();

        //animate sprite frames
        if (SystemTimer.getTime() - lastFrameChange > 0.04) {
            currentFrame = currentFrame + 1 >= frames.length ? 0 : currentFrame + 1;
            sprite = frames[currentFrame];
            lastFrameChange = SystemTimer.getTime();
        }

        //movement script
        if (moveTicks > lastMove) {

            if (moveTicks == 1) {//init direction 
                angle = 0;
                speed = 250;

            } else if (moveTicks > 1 && moveTicks < 25) {//slow down for the first second
                if (speed > 0) {
                    speed -= 10;

                }
            } else if (moveTicks == 25) {//set left or right based on screen pos
                angle = x > 400 ? -70 : 70;
            } else if (moveTicks > 25) {
                if (moveTicks < 75 && speed < 100) {//speed up in one directon for next second
                    speed += 4;
                } else if (speed > 0) {//slow down in same directon for next second
                    speed -= 4;
                }
                if (moveTicks == 125) {// loop timer back
                    moveTicks = 24;

                }
            }
            setHorizontalMovement((float) (sin(toRadians(angle)) * speed));
            setVerticalMovement((float) (cos(toRadians(angle)) * speed));
            lastMove = moveTicks;
        }
        //shooting script
        if (shotTicks > lastShot) {

            if (shotTicks % 60 == 50 || shotTicks % 60 == 51) {
                shoot(150 + 20 * rank, target, 0);
                shoot(150 + 20 * rank, target, 5);
                shoot(150 + 20 * rank, target, -5);
            }

            lastShot = shotTicks;
        }
    }

    @Override
    public void draw() {

        //draw shadow image under ship
        shadowFrames[currentFrame].draw((int) x - (sprite.getWidth() / 4), (int) y);

        //draw ship
        sprite.draw((int) x - (sprite.getWidth() / 2), (int) y - (sprite.getHeight() / 2));

        //if enemy has been hit, flash a colored frame
        if (hit) {
            hitFrames[currentFrame].draw((int) x - (sprite.getWidth() / 2), (int) y - (sprite.getHeight() / 2));
            hit = false;
        }
        //draw hitbox for debugging
//        (ResourceFactory.get().getGameWindow()).fillRect(Color.RED, hitBox);
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
                MedExplosion me = new MedExplosion(game, (int) x, (int) y, "long");
                me.setHorizontalMovement(dx * 0.6f);
                me.setVerticalMovement(dy * 0.6f);
                game.getEffects().add(me);
                Item pu = new PowerUp(game, (int) x, (int) y, "");
                game.getItems().add(pu);
            }

        }
    }
}
