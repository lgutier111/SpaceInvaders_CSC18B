package SpaceInvaders_V4.Entity;

import SpaceInvaders_V4.GameState.GameState;
import SpaceInvaders_V4.Main.ResourceFactory;
import SpaceInvaders_V4.Users.Score;
import SpaceInvaders_V4.Util.Sprite;
import SpaceInvaders_V4.Util.SystemTimer;
import java.awt.Color;
import static java.lang.Math.*;

/**
 *
 * @author Bee-Jay
 */
public class EnemyWasp extends EnemyEntity {

    private Sprite[][] bodyFrames;
    private Sprite[][] shadowFrames;
    private Sprite[][] hitFrames;
    private Sprite[][] propellarFrames;
//    private Sprite propSprite;
    private int currentCol = 0;
    private int currentRow = 0;
    private int propCol = 0;
    private int blinkCol = 0;
    private int bladeCol = 0;
    private int propRow = 0;
    private double lastFrameChange = 0;
    private double deadTimer;

    /**
     *
     * @param game
     * @param rank
     * @param x
     * @param y
     * @param ref
     */
    public EnemyWasp(GameState game, int rank, int x, int y, String ref) {
        super(game, rank, x, y, ref.equals("g")?"resources/sprites/enemy/wasp_green.png":"resources/sprites/enemy/wasp_brown.png");

        bodyFrames = new Sprite[2][16];
        shadowFrames = new Sprite[bodyFrames.length][bodyFrames[0].length];
        hitFrames = new Sprite[bodyFrames.length][bodyFrames[0].length];
        propellarFrames = new Sprite[2][10];

        int height = sprite.getHeight() / 4;
        int width = sprite.getWidth() / 16;

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 16; col++) {
                if (row == 0) {
                    bodyFrames[0][col] = ResourceFactory.get().getSubSprite(sprite.getRef(), col * width, row * height, width, height);
                    shadowFrames[0][col] = ResourceFactory.get().getShadedSubSprite(sprite.getRef(), col * width, row * height, width, height, Color.BLACK, 0.5f);
                    hitFrames[0][col] = ResourceFactory.get().getShadedSubSprite(sprite.getRef(), col * width, row * height, width, height, Color.WHITE, 0.7f);
                } else if (row == 1) {
                    if (col < 10) {
                        propellarFrames[0][col] = ResourceFactory.get().getSubSprite(sprite.getRef(), col * width, row * height, width, height);
                    }
                } else if (row == 2) {
                    if (col < 8) {
                        propellarFrames[1][col] = ResourceFactory.get().getSubSprite(sprite.getRef(), col * width, row * height, width, height);
                    }
                } else if (row == 3) {
                    if (col < 8) {
                        bodyFrames[1][col] = ResourceFactory.get().getSubSprite(sprite.getRef(), col * width, row * height, width, height);
                        shadowFrames[1][col] = ResourceFactory.get().getShadedSubSprite(sprite.getRef(), col * width, row * height, width, height, Color.BLACK, 0.5f);
                        hitFrames[1][col] = ResourceFactory.get().getShadedSubSprite(sprite.getRef(), col * width, row * height, width, height, Color.red, 0.5f);
                    }
                }
            }
        }
        sprite = bodyFrames[currentRow][currentCol];

    }

    @Override
    public void doLogic() {
        super.doLogic();
        this.gunX = (int) x;
        this.gunY = (int) y;

        //animate sprite frames
        if (SystemTimer.getTime() - lastFrameChange > 0.04) {

            if (!dead) {
                //ship rotation
                float tangle = (((float) toDegrees(atan2((target.getX() - x), (target.getY() - x))) + 360) % 360);

                if (tangle < 11.25f || tangle >= 348.75f) {

                    currentCol = 0;
                } else if (tangle >= 11.25f && tangle < 33.75f) {

                    currentCol = 15;
                } else if (tangle >= 33.75f && tangle < 56.25f) {

                    currentCol = 14;
                } else if (tangle >= 56.25f && tangle < 78.75f) {

                    currentCol = 13;
                } else if (tangle >= 78.75f && tangle < 101.25f) {

                    currentCol = 12;
                } else if (tangle >= 101.25f && tangle < 123.75f) {

                    currentCol = 11;
                } else if (tangle >= 123.75f && tangle < 146.25f) {

                    currentCol = 10;
                } else if (tangle >= 146.25f && tangle < 168.75f) {

                    currentCol = 9;
                } else if (tangle >= 168.75f && tangle < 191.25f) {

                    currentCol = 8;
                } else if (tangle >= 191.25f && tangle < 213.75f) {

                    currentCol = 7;
                } else if (tangle >= 213.75f && tangle < 235.25f) {

                    currentCol = 6;
                } else if (tangle >= 235.25f && tangle < 258.75f) {

                    currentCol = 5;
                } else if (tangle >= 258.75f && tangle < 281.25f) {

                    currentCol = 4;
                } else if (tangle >= 281.25f && tangle < 303.75f) {

                    currentCol = 3;
                } else if (tangle >= 303.75f && tangle < 326.25) {

                    currentCol = 2;
                } else if (tangle >= 326.25 && tangle < 348.75) {

                    currentCol = 1;
                }
                currentRow = 0;
                currentCol = (currentCol + 16) % 16;//boundIndexCheck

                if (propRow == 0) {
                    propRow = 1;
                    propCol = blinkCol = (blinkCol + 1) % 8;
                } else if (propRow == 1) {
                    propRow = 0;
                    propCol = bladeCol = (bladeCol + 1) % 10;
                }

                //movement scripts
            } else if (SystemTimer.getTime() < deadTimer) {//if dead
                currentRow = 1;
                currentCol = (currentCol + 1) % 8;
                hitBox.setLocation(-500, -500);
                hitBox.setSize(0, 0);
            } else {
                hitBox.setSize(0, 0);
                hitBox.setLocation(-500, -500);
                game.getRemoveEnemyList().add(this);
                MedExplosion me = new MedExplosion(game, (int) x, (int) y, "long");
                me.setHorizontalMovement(dx * 0.6f);
                me.setVerticalMovement(dy * 0.6f);
                game.getEffects().add(me);
            }
            sprite = bodyFrames[currentRow][currentCol];
            lastFrameChange = SystemTimer.getTime();
        }

        if (!dead) {
            //move scripts
            if (moveTicks > lastMove) {
                if (moveTicks == 1) {//init direction 
                    angle = 0;
                    speed = 350;

                } else if (moveTicks > 1 && moveTicks < 50) {
                    if (speed > 0) {
                        if (speed - 5 > 0) {
                            speed -= 6;
                        } else {
                            speed = 0;
                        }
                    }
                } else if (moveTicks == 50) {
                    angle = (float) (200 - Math.random() * 40);
                    speed = 0;
                } else if (moveTicks > 150 && moveTicks < 300) {
                    if (speed < 350) {
                        speed += 1;
                    }
                }

                setHorizontalMovement((float) (sin(toRadians(angle)) * speed));
                setVerticalMovement((float) (cos(toRadians(angle)) * speed));
                lastMove = moveTicks;
            }

            //shootScripts
            if (shotTicks > lastShot) {
                if(shotTicks % 100 == 50){
                    shoot(175 + 20 * rank, target, -20);
                }else if(shotTicks % 100 == 53){
                    shoot(175 + 20 * rank, target, -10);
                }else if(shotTicks % 100 == 56){
                    shoot(175 + 20 * rank, target, 0);
                }else if(shotTicks % 100 == 59){
                    shoot(175 + 20 * rank, target, 10);
                }else if(shotTicks % 100 == 62){
                    shoot(175 + 20 * rank, target, 20);
                }
                
                lastShot = shotTicks;
            }

        }
    }

    @Override
    public void draw() {

        shadowFrames[currentRow][currentCol].draw((int) x - 5, (int) y - 5);
        //draw ship
        sprite.draw((int) x - (sprite.getWidth() / 2), (int) y - (sprite.getHeight() / 2));

        //if enemy has been hit, flash a colored frame
        if (hit) {
            hitFrames[currentRow][currentCol].draw((int) x - (sprite.getWidth() / 2), (int) y - (sprite.getHeight() / 2));
            hit = false;
        }
        if (!dead) {
            propellarFrames[propRow][propCol].draw((int) x - (sprite.getWidth() / 2), (int) y - (sprite.getHeight() / 2));
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
                deadTimer = SystemTimer.getTime() + 1;
                MedExplosion me = new MedExplosion(game, (int) x, (int) y, "");
                me.setHorizontalMovement(dx * 1f);
                me.setVerticalMovement(dy * 1f);
                game.getEffects().add(me);

            }

        }
    }

}
