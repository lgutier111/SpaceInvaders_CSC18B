package SpaceInvaders_V4.Entity;

import SpaceInvaders_V4.GameState.GameState;
import SpaceInvaders_V4.Main.ResourceFactory;
import SpaceInvaders_V4.Users.Score;

import SpaceInvaders_V4.Util.Sprite;
import SpaceInvaders_V4.Util.SystemTimer;
import java.awt.Color;
import java.awt.Rectangle;

/**
 * @author Bee-Jay
 */
public class PlayerEntity extends Entity {

    //the game in which the ship exists
    private final GameState game;

    //animation frames
    private final Sprite[][] frames;
    private final Sprite[][] shadowFrames;
    private final Sprite[][] thrusterFrames;
    private final Sprite[][] bulletFlash;
    private final Sprite[][] laserFlash;
    private double lastFrameChange = 0;
    private int shipRollFrame = 0;
    private int thrusterRow = 0;
    private int thrusterCol = 0;
    private final int[] thrusterPos = new int[4];
    private int throttle;

    /**
     * Constant value for setThrottle method Thrusters will not render
     */
    public static final int THROTTLE_OFF = 0;

    /**
     * Constant value for setThrottle method Thrusters will render at normal
     * length
     */
    public static final int THROTTLE_FULL = 1;

    /**
     * Constant value for setThrottle method Thrusters will render at extended
     * length
     */
    public static final int THROTTLE_TURBO = 2;

    //collision fields
    private boolean flinching;
    private double flinchTimer;

    //Time since players last shot
    private double lastShot = 0;
    private boolean shooting = false;
    private double flashTimer = 0;
    //player shootrate in 1/ms
    private double shotInterval;
    private final double laserInterval = 0.025;
    private final double bulletInterval = 0.08;

    //shot vars
    private int shotType;
    private int shotLevel;

    /**
     * Constant value for shotLevel bullets will be fired in shoot method
     */
    public static final int BULLET = 1;

    /**
     * Constant value for shotLevel Lasers will be fired in shoot method
     */
    public static final int LASER = 2;
    private final int BULLETSPEED = 750;
    private final int LASERSPEED = 800;

    //ships speed in pixels/sec
    private float moveSpeed = 300;

    //Diagonal movement factor
    private final float DIAGONAL = (float) Math.sin(45);

    //Key events
    private boolean left;
    private boolean right;
    private boolean down;
    private boolean up;
    private boolean trigger;//firing mechanism

    /**
     * Create a new entity to represent the players ship
     *
     * @param game The game in which the ship is being created
     * @param x The initial x location of the player's ship
     * @param y The initial y location of the player's ship
     * @param ref optional flag
     */
    public PlayerEntity(GameState game, int x, int y, String ref) {
        super(x, y, "resources/sprites/player/Player1Sprite.png");
        this.throttle = THROTTLE_FULL;
        int tileSize = 40;
        this.game = game;

        //split tileset image into individual tiles
        int cols = sprite.getWidth() / tileSize;
        int rows = sprite.getHeight() / tileSize;

        frames = new Sprite[rows][cols];
        shadowFrames = new Sprite[rows][cols];

        for (int col = 0; col < cols; col++) {
            for (int row = 0; row < rows; row++) {
                frames[row][col] = ResourceFactory.get().getSubSprite(
                        sprite.getRef(),
                        col * tileSize,
                        row * tileSize,
                        tileSize,
                        tileSize
                );
                shadowFrames[row][col] = ResourceFactory.get().getShadedSubSprite(
                        sprite.getRef(),
                        col * tileSize,
                        row * tileSize,
                        tileSize,
                        tileSize,
                        Color.black,
                        0.5f
                );
            }

        }

        Sprite temp = ResourceFactory.get().getSprite("resources/sprites/player/thrusters.png");
        thrusterFrames = new Sprite[3][4];
        int height = temp.getHeight() / thrusterFrames.length;
        int width = temp.getWidth() / thrusterFrames[0].length;

        for (int col = 0; col < thrusterFrames[0].length; col++) {
            for (int row = 0; row < thrusterFrames.length; row++) {
                thrusterFrames[row][col] = ResourceFactory.get().getSubSprite(
                        temp.getRef(),
                        col * width,
                        row * height,
                        width,
                        height
                );
            }
        }

        Sprite bulletTemp = ResourceFactory.get().getSprite("resources/sprites/player/bulletFlash.png");

        Sprite laserTemp = ResourceFactory.get().getSprite("resources/sprites/player/laserFlash.png");
        int tileHeight = laserTemp.getHeight() / 4;
        int tileWidth = laserTemp.getWidth() / 11;

        laserFlash = new Sprite[4][11];
        bulletFlash = new Sprite[4][11];

        for (int row = 0; row < laserFlash.length; row++) {
            for (int col = 0; col < laserFlash[row].length; col++) {
                laserFlash[row][col] = ResourceFactory.get().getSubSprite(laserTemp.getRef(), col * tileWidth, row * tileHeight, tileWidth, tileHeight);
                bulletFlash[row][col] = ResourceFactory.get().getSubSprite(bulletTemp.getRef(), col * tileWidth, row * tileHeight, tileWidth, tileHeight);
            }
        }

        //set default frame
        shipRollFrame = 5;
        sprite = frames[0][shipRollFrame];

        //initialize key event vars
        up = down = left = right = trigger = false;

        shotType = BULLET;
        shotInterval = bulletInterval;
        shotLevel = 1;
    }

    //do logic associated with this player
    @Override
    public void doLogic() {

        boolean[] keys = game.getKeys();
        up = keys[0];
        down = keys[1];
        left = keys[2];
        right = keys[3];
        trigger = keys[4];

        if (flinching && SystemTimer.getTime() > flinchTimer) {
            flinching = false;

        }
        if (game.getPlayerControl()) {
            // resolve the movement of the ship. First assume the ship
            // isn't moving. If either cursor key is pressed then
            // update the movement appropraitely
            setHorizontalMovement(0);
            setVerticalMovement(0);

            if ((left) && (!right)) {//left
                if ((up) && (!down)) {//upLeft
                    //factor for diagonnal movement speed
                    setVerticalMovement(DIAGONAL * -moveSpeed);
                    setHorizontalMovement(DIAGONAL * -moveSpeed);
                } else if ((down) && (!up)) {//downLeft
                    setVerticalMovement(DIAGONAL * moveSpeed);
                    setHorizontalMovement(DIAGONAL * -moveSpeed);
                } else {
                    setHorizontalMovement(-moveSpeed);//left only
                }
            } else if ((right) && (!left)) {//right
                if ((up) && (!down)) {//up
                    setVerticalMovement(DIAGONAL * -moveSpeed);
                    setHorizontalMovement(DIAGONAL * moveSpeed);
                } else if ((down) && (!up)) {//down
                    setVerticalMovement(DIAGONAL * moveSpeed);
                    setHorizontalMovement(DIAGONAL * moveSpeed);

                } else {
                    setHorizontalMovement(moveSpeed);//right only
                }
            } else if ((up) && (!down) && (!right) && (!left)) {//up
                setVerticalMovement(-moveSpeed);
            } else if ((down) && (!up) && (!right) && (!left)) {//down
                setVerticalMovement(moveSpeed);
            }

            if (trigger) {
                shoot();
            }
        }
        //ship tilt animation
        if (SystemTimer.getTime() - lastFrameChange > 0.04) {
            //thruster frames
            if (dy < 0) {
                thrusterRow = 1;
            } else {
                thrusterRow = 0;
            }

            thrusterCol++;
            if (thrusterCol >= thrusterFrames[thrusterRow].length) {
                thrusterCol = 0;
            }

            if (dx < 0) {
                if (shipRollFrame > 0) {
                    shipRollFrame--;
                }

            } else if (dx == 0) {
                if (shipRollFrame > 5) {
                    shipRollFrame--;
                } else if (shipRollFrame < 5) {
                    shipRollFrame++;
                }
            } else {
                if (shipRollFrame < frames[0].length - 1) {
                    shipRollFrame++;
                }

            }
            sprite = frames[0][shipRollFrame];
            lastFrameChange = SystemTimer.getTime();

        }

        //set thruster positions according to ship frame
        switch (shipRollFrame) {
            case 0: {
                thrusterPos[0] = (int) x - 6;
                thrusterPos[2] = (int) x - 4;
                break;
            }
            case 1: {
                thrusterPos[0] = (int) x - 7;
                thrusterPos[2] = (int) x - 3;
                break;
            }
            case 2: {
                thrusterPos[0] = (int) x - 8;
                thrusterPos[2] = (int) x - 2;
                break;
            }
            case 3: {
                thrusterPos[0] = (int) x - 9;
                thrusterPos[2] = (int) x - 1;
                break;
            }
            case 4: {
                thrusterPos[0] = (int) x - 10;
                thrusterPos[2] = (int) x;
                break;
            }
            case 5: {
                thrusterPos[0] = (int) x - 11;
                thrusterPos[2] = (int) x + 1;
                break;
            }
            case 6: {
                thrusterPos[0] = (int) x;
                thrusterPos[2] = (int) x - 10;
                break;
            }
            case 7: {
                thrusterPos[0] = (int) x - 1;
                thrusterPos[2] = (int) x - 9;
                break;
            }
            case 8: {
                thrusterPos[0] = (int) x - 2;
                thrusterPos[2] = (int) x - 8;
                break;
            }
            case 9: {
                thrusterPos[0] = (int) x - 3;
                thrusterPos[2] = (int) x - 7;
                break;
            }
            case 10: {
                thrusterPos[0] = (int) x - 4;
                thrusterPos[2] = (int) x - 6;
                break;
            }
        }
        thrusterPos[1] = thrusterPos[3] = (int) y + 18;

    }

    /**
     * Activate temporary invulnerability
     *
     * @param flinchTime time in seconds for invulnerability to expire
     */
    public void setFlinching(double flinchTime) {
        flinching = true;
        flinchTimer = SystemTimer.getTime() + flinchTime;

    }

    /**
     * Set Throttle Level
     *
     * @param throttle PlayerEntity.THROTTLE_OFF, PlayerEntity.THROTTLE_FULL, or
     * PlayerEntity.THROTTLE_Turbo
     */
    public void setThrottle(int throttle) {
        if (throttle < THROTTLE_OFF || throttle > THROTTLE_TURBO) {//bound check
            throttle = THROTTLE_FULL;
        }
        this.throttle = throttle;
    }

    /**
     * shoot projectile according to shotType and shotLevel
     */
    public void shoot() {
        //check firing interval
        if (SystemTimer.getTime() - lastShot < shotInterval) {
            return;
        }
        //shoot if interval has passed
        lastShot = SystemTimer.getTime();

        if (shotType == BULLET) {
            if (shotLevel == 1) {
                if (game.getPlayerEntities().size() / 1 <= 5) {
                    ShotEntity shot = new ShotEntity(game, 2, BULLETSPEED, 270, (int) (x), (int) (y - 6), "resources/sprites/player/shotBullet1.png");
                    game.getPlayerEntities().add(shot);
                    flashTimer = SystemTimer.getTime();
                    shooting = true;
                }
            } else if (shotLevel == 2) {
                if (game.getPlayerEntities().size() / 2 <= 5) {
                    ShotEntity shot1 = new ShotEntity(game, 2, BULLETSPEED, -90, (int) (x + 6), (int) (y - 6), "resources/sprites/player/shotBullet2.png");
                    game.getPlayerEntities().add(shot1);
                    ShotEntity shot2 = new ShotEntity(game, 2, BULLETSPEED, -90, (int) (x - 6), (int) (y - 6), "resources/sprites/player/shotBullet2.png");
                    game.getPlayerEntities().add(shot2);
                    flashTimer = SystemTimer.getTime();
                    shooting = true;
                }

            } else if (shotLevel == 3) {
                if (game.getPlayerEntities().size() / 3 <= 5) {
                    ShotEntity shot1 = new ShotEntity(game, 2, BULLETSPEED, 280, (int) (x + 6), (int) (y - 6), "resources/sprites/player/shotBullet2.png");
                    game.getPlayerEntities().add(shot1);
                    ShotEntity shot2 = new ShotEntity(game, 2, BULLETSPEED, 270, (int) (x), (int) (y - 6), "resources/sprites/player/shotBullet2.png");
                    game.getPlayerEntities().add(shot2);
                    ShotEntity shot3 = new ShotEntity(game, 2, BULLETSPEED, 260, (int) (x - 6), (int) (y - 6), "resources/sprites/player/shotBullet2.png");
                    game.getPlayerEntities().add(shot3);
                    flashTimer = SystemTimer.getTime();
                    shooting = true;
                }

            } else if (shotLevel == 4) {
                if (game.getPlayerEntities().size() / 3 <= 5) {
                    ShotEntity shot1 = new ShotEntity(game, 3, BULLETSPEED, 280, (int) (x + 6), (int) (y - 6), "resources/sprites/player/shotBullet3.png");
                    game.getPlayerEntities().add(shot1);
                    ShotEntity shot2 = new ShotEntity(game, 3, BULLETSPEED, 270, (int) (x), (int) (y - 6), "resources/sprites/player/shotBullet3.png");
                    game.getPlayerEntities().add(shot2);
                    ShotEntity shot3 = new ShotEntity(game, 3, BULLETSPEED, 260, (int) (x - 6), (int) (y - 6), "resources/sprites/player/shotBullet3.png");
                    game.getPlayerEntities().add(shot3);
                    flashTimer = SystemTimer.getTime();
                    shooting = true;
                }

            } else if (shotLevel == 5) {
                if (game.getPlayerEntities().size() / 5 <= 5) {
                    ShotEntity shot1 = new ShotEntity(game, 2, BULLETSPEED, 290, (int) (x + 6), (int) (y - 6), "resources/sprites/player/shotBullet2.png");
                    game.getPlayerEntities().add(shot1);
                    ShotEntity shot2 = new ShotEntity(game, 2, BULLETSPEED, 280, (int) (x + 6), (int) (y - 6), "resources/sprites/player/shotBullet2.png");
                    game.getPlayerEntities().add(shot2);
                    ShotEntity shot3 = new ShotEntity(game, 2, BULLETSPEED, 270, (int) (x), (int) (y - 6), "resources/sprites/player/shotBullet2.png");
                    game.getPlayerEntities().add(shot3);
                    ShotEntity shot4 = new ShotEntity(game, 2, BULLETSPEED, 260, (int) (x - 6), (int) (y - 6), "resources/sprites/player/shotBullet2.png");
                    game.getPlayerEntities().add(shot4);
                    ShotEntity shot5 = new ShotEntity(game, 2, BULLETSPEED, 250, (int) (x - 6), (int) (y - 6), "resources/sprites/player/shotBullet2.png");
                    game.getPlayerEntities().add(shot5);
                    flashTimer = SystemTimer.getTime();
                    shooting = true;
                }
            } else if (shotLevel == 6) {
                if (game.getPlayerEntities().size() / 5 <= 5) {
                    ShotEntity shot1 = new ShotEntity(game, 3, BULLETSPEED, 290, (int) (x + 6), (int) (y - 6), "resources/sprites/player/shotBullet3.png");
                    game.getPlayerEntities().add(shot1);
                    ShotEntity shot2 = new ShotEntity(game, 3, BULLETSPEED, 280, (int) (x + 6), (int) (y - 6), "resources/sprites/player/shotBullet3.png");
                    game.getPlayerEntities().add(shot2);
                    ShotEntity shot3 = new ShotEntity(game, 3, BULLETSPEED, 270, (int) (x), (int) (y - 6), "resources/sprites/player/shotBullet3.png");
                    game.getPlayerEntities().add(shot3);
                    ShotEntity shot4 = new ShotEntity(game, 3, BULLETSPEED, 260, (int) (x - 6), (int) (y - 6), "resources/sprites/player/shotBullet3.png");
                    game.getPlayerEntities().add(shot4);
                    ShotEntity shot5 = new ShotEntity(game, 3, BULLETSPEED, 250, (int) (x - 6), (int) (y - 6), "resources/sprites/player/shotBullet3.png");
                    game.getPlayerEntities().add(shot5);
                    flashTimer = SystemTimer.getTime();
                    shooting = true;
                }
            } else if (shotLevel == 7) {
                if (game.getPlayerEntities().size() / 5 <= 5) {
                    ShotEntity shot1 = new ShotEntity(game, 4, BULLETSPEED, 290, (int) (x + 6), (int) (y - 6), "resources/sprites/player/shotBullet4.png");
                    game.getPlayerEntities().add(shot1);
                    ShotEntity shot2 = new ShotEntity(game, 4, BULLETSPEED, 280, (int) (x + 6), (int) (y - 6), "resources/sprites/player/shotBullet4.png");
                    game.getPlayerEntities().add(shot2);
                    ShotEntity shot3 = new ShotEntity(game, 4, BULLETSPEED, 270, (int) (x), (int) (y - 6), "resources/sprites/player/shotBullet4.png");
                    game.getPlayerEntities().add(shot3);
                    ShotEntity shot4 = new ShotEntity(game, 4, BULLETSPEED, 260, (int) (x - 6), (int) (y - 6), "resources/sprites/player/shotBullet4.png");
                    game.getPlayerEntities().add(shot4);
                    ShotEntity shot5 = new ShotEntity(game, 4, BULLETSPEED, 250, (int) (x - 6), (int) (y - 6), "resources/sprites/player/shotBullet4.png");
                    game.getPlayerEntities().add(shot5);
                    flashTimer = SystemTimer.getTime();
                    shooting = true;
                }
            } else if (shotLevel == 8) {
                if (game.getPlayerEntities().size() / 7 <= 5) {
                    ShotEntity shot1 = new ShotEntity(game, 4, BULLETSPEED, 315, (int) (x + 6), (int) (y - 6), "resources/sprites/player/shotBullet4R.png");
                    game.getPlayerEntities().add(shot1);
                    ShotEntity shot2 = new ShotEntity(game, 4, BULLETSPEED, 290, (int) (x + 6), (int) (y - 6), "resources/sprites/player/shotBullet4.png");
                    game.getPlayerEntities().add(shot2);
                    ShotEntity shot3 = new ShotEntity(game, 4, BULLETSPEED, 280, (int) (x + 6), (int) (y - 6), "resources/sprites/player/shotBullet4.png");
                    game.getPlayerEntities().add(shot3);
                    ShotEntity shot4 = new ShotEntity(game, 4, BULLETSPEED, 270, (int) (x), (int) (y - 6), "resources/sprites/player/shotBullet4.png");
                    game.getPlayerEntities().add(shot4);
                    ShotEntity shot5 = new ShotEntity(game, 4, BULLETSPEED, 260, (int) (x - 6), (int) (y - 6), "resources/sprites/player/shotBullet4.png");
                    game.getPlayerEntities().add(shot5);
                    ShotEntity shot6 = new ShotEntity(game, 4, BULLETSPEED, 250, (int) (x - 6), (int) (y - 6), "resources/sprites/player/shotBullet4.png");
                    game.getPlayerEntities().add(shot6);
                    ShotEntity shot7 = new ShotEntity(game, 4, BULLETSPEED, 225, (int) (x - 6), (int) (y - 6), "resources/sprites/player/shotBullet4L.png");
                    game.getPlayerEntities().add(shot7);
                    flashTimer = SystemTimer.getTime();
                    shooting = true;
                }
            }
        } else if (shotType == LASER) {
            if (shotLevel == 1) {
                if (game.getPlayerEntities().size() / 1 <= 10) {
                    ShotEntity shot = new ShotEntity(game, 2, LASERSPEED, 270, (int) (x), (int) (y - 12), "resources/sprites/player/shotLaser1.png");
                    game.getPlayerEntities().add(shot);
                    flashTimer = SystemTimer.getTime();
                    shooting = true;
                }
            } else if (shotLevel == 2) {
                if (game.getPlayerEntities().size() / 1 <= 10) {
                    ShotEntity shot = new ShotEntity(game, 4, LASERSPEED, 270, (int) (x), (int) (y - 12), "resources/sprites/player/shotLaser2.png");
                    game.getPlayerEntities().add(shot);
                    flashTimer = SystemTimer.getTime();
                    shooting = true;

                }

            } else if (shotLevel == 3) {
                if (game.getPlayerEntities().size() / 2 <= 10) {
                    ShotEntity shot1 = new ShotEntity(game, 4, LASERSPEED, -90, (int) (x + 6), (int) (y - 12), "resources/sprites/player/shotLaser2.png");
                    game.getPlayerEntities().add(shot1);
                    ShotEntity shot2 = new ShotEntity(game, 4, LASERSPEED, -90, (int) (x - 6), (int) (y - 12), "resources/sprites/player/shotLaser2.png");
                    game.getPlayerEntities().add(shot2);
                    flashTimer = SystemTimer.getTime();
                    shooting = true;
                }

            } else if (shotLevel == 4) {
                if (game.getPlayerEntities().size() / 3 <= 10) {
                    ShotEntity shot1 = new ShotEntity(game, 4, LASERSPEED, -90, (int) (x + 6), (int) (y - 12), "resources/sprites/player/shotLaser2.png");
                    game.getPlayerEntities().add(shot1);
                    ShotEntity shot2 = new ShotEntity(game, 4, LASERSPEED, -90, (int) (x - 6), (int) (y - 12), "resources/sprites/player/shotLaser2.png");
                    game.getPlayerEntities().add(shot2);
                    ShotEntity shot3 = new ShotEntity(game, 2, LASERSPEED, 270, (int) (x), (int) (y - 12), "resources/sprites/player/shotLaser1.png");
                    game.getPlayerEntities().add(shot3);
                    flashTimer = SystemTimer.getTime();
                    shooting = true;
                }

            } else if (shotLevel == 5) {
                if (game.getPlayerEntities().size() / 3 <= 10) {
                    ShotEntity shot1 = new ShotEntity(game, 6, LASERSPEED, -90, (int) (x + 6), (int) (y - 12), "resources/sprites/player/shotLaser3.png");
                    game.getPlayerEntities().add(shot1);
                    ShotEntity shot2 = new ShotEntity(game, 6, LASERSPEED, -90, (int) (x - 6), (int) (y - 12), "resources/sprites/player/shotLaser3.png");
                    game.getPlayerEntities().add(shot2);
                    ShotEntity shot3 = new ShotEntity(game, 4, LASERSPEED, 270, (int) (x), (int) (y - 12), "resources/sprites/player/shotLaser2.png");
                    game.getPlayerEntities().add(shot3);
                    flashTimer = SystemTimer.getTime();
                    shooting = true;
                }
            } else if (shotLevel == 6) {
                if (game.getPlayerEntities().size() / 3 <= 10) {
                    ShotEntity shot1 = new ShotEntity(game, 8, LASERSPEED, -90, (int) (x + 8), (int) (y - 12), "resources/sprites/player/shotLaser4.png");
                    game.getPlayerEntities().add(shot1);
                    ShotEntity shot2 = new ShotEntity(game, 8, LASERSPEED, -90, (int) (x - 8), (int) (y - 12), "resources/sprites/player/shotLaser4.png");
                    game.getPlayerEntities().add(shot2);
                    ShotEntity shot3 = new ShotEntity(game, 6, LASERSPEED, 270, (int) (x), (int) (y - 12), "resources/sprites/player/shotLaser3.png");
                    game.getPlayerEntities().add(shot3);
                    flashTimer = SystemTimer.getTime();
                    shooting = true;
                }
            } else if (shotLevel == 7) {
                if (game.getPlayerEntities().size() / 3 <= 10) {
                    ShotEntity shot1 = new ShotEntity(game, 6, LASERSPEED, -90, (int) (x + 12), (int) (y - 12), "resources/sprites/player/shotLaser3.png");
                    game.getPlayerEntities().add(shot1);
                    ShotEntity shot2 = new ShotEntity(game, 6, LASERSPEED, -90, (int) (x - 12), (int) (y - 12), "resources/sprites/player/shotLaser3.png");
                    game.getPlayerEntities().add(shot2);
                    ShotEntity shot3 = new ShotEntity(game, 10, LASERSPEED, 270, (int) (x), (int) (y - 12), "resources/sprites/player/shotLaser5.png");
                    game.getPlayerEntities().add(shot3);
                    flashTimer = SystemTimer.getTime();
                    shooting = true;
                }
            } else if (shotLevel == 8) {
                if (game.getPlayerEntities().size() / 3 <= 10) {
                    ShotEntity shot1 = new ShotEntity(game, 8, LASERSPEED, -90, (int) (x + 18), (int) (y - 12), "resources/sprites/player/shotLaser4.png");
                    game.getPlayerEntities().add(shot1);
                    ShotEntity shot2 = new ShotEntity(game, 8, LASERSPEED, -90, (int) (x - 18), (int) (y - 12), "resources/sprites/player/shotLaser4.png");
                    game.getPlayerEntities().add(shot2);
                    ShotEntity shot3 = new ShotEntity(game, 12, LASERSPEED, 270, (int) (x), (int) (y - 12), "resources/sprites/player/shotLaser6.png");
                    game.getPlayerEntities().add(shot3);
                    flashTimer = SystemTimer.getTime();
                    shooting = true;
                }
            }
        }

    }

    @Override
    public void draw() {
        if (!(flinching && ((int) ((flinchTimer - SystemTimer.getTime()) * 16)) % 2 == 0)) {//blink when flinching
            //draw shadow image under ship
            shadowFrames[0][shipRollFrame].draw((int) x - (shadowFrames[0][shipRollFrame].getWidth() / 2) + 20,
                    (int) y - (shadowFrames[0][shipRollFrame].getHeight() / 2) + 20);

            //draw thruster frames
            if (throttle != THROTTLE_OFF) {
                if (throttle == THROTTLE_TURBO) {
                    thrusterFrames[2][thrusterCol].draw(thrusterPos[0], thrusterPos[1]);
                    thrusterFrames[2][thrusterCol].draw(thrusterPos[2], thrusterPos[3]);
                } else {
                    thrusterFrames[thrusterRow][thrusterCol].draw(thrusterPos[0], thrusterPos[1]);
                    thrusterFrames[thrusterRow][thrusterCol].draw(thrusterPos[2], thrusterPos[3]);
                }
            }
            //draw ship
            super.draw();

            if (shooting) {
                if (shotType == BULLET) {
                    if (shotLevel >= 1 && shotLevel <= 6) {
                        if (SystemTimer.getTime() - flashTimer < 0.04) {
                            bulletFlash[0][shipRollFrame].draw((int) (x - 20), (int) (y - 20));
                        } else if (SystemTimer.getTime() - flashTimer > 0.04 && SystemTimer.getTime() - flashTimer < 0.12) {
                            bulletFlash[1][shipRollFrame].draw((int) (x - 20), (int) (y - 20));
                        } else {
                            shooting = false;
                        }
                    } else if (shotLevel >= 7 && shotLevel <= 8) {
                        if (SystemTimer.getTime() - flashTimer < 0.04) {
                            bulletFlash[2][shipRollFrame].draw((int) (x - 20), (int) (y - 20));
                        } else if (SystemTimer.getTime() - flashTimer > 0.04 && SystemTimer.getTime() - flashTimer < 0.12) {
                            bulletFlash[3][shipRollFrame].draw((int) (x - 20), (int) (y - 20));
                        } else {
                            shooting = false;
                        }
                    }
                } else if (shotType == LASER) {
                    if (shotLevel >= 1 && shotLevel <= 7) {
                        if (SystemTimer.getTime() - flashTimer < 0.04) {
                            laserFlash[0][shipRollFrame].draw((int) (x - 20), (int) (y - 20));
                        } else if (SystemTimer.getTime() - flashTimer > 0.04 && SystemTimer.getTime() - flashTimer < 0.12) {
                            laserFlash[1][shipRollFrame].draw((int) (x - 20), (int) (y - 20));
                        } else {
                            shooting = false;
                        }
                    } else if (shotLevel == 8) {
                        if (SystemTimer.getTime() - flashTimer < 0.04) {
                            laserFlash[2][shipRollFrame].draw((int) (x - 20), (int) (y - 20));
                        } else if (SystemTimer.getTime() - flashTimer > 0.04 && SystemTimer.getTime() - flashTimer < 0.12) {
                            laserFlash[3][shipRollFrame].draw((int) (x - 20), (int) (y - 20));
                        } else {
                            shooting = false;
                        }
                    }
                }
            }
        }
        //draw hitbox for debugging
//        (ResourceFactory.get().getGameWindow()).fillRect(Color.BLUE, hitBox);
    }

    @Override
    public void move(Double delta) {
        //set bounds to prevent movement offscreen
        if ((dx < 0) && (x + (delta * dx) <= (this.sprite.getWidth() / 2))) {//right bound
            dx = 0;
            x = this.sprite.getWidth() / 2;
        }
        if ((dx > 0) && (x + (delta * dx) >= ResourceFactory.get().getGameWindow().getWidth() - this.sprite.getWidth() / 2)) {//left bound
            dx = 0;
            x = ResourceFactory.get().getGameWindow().getWidth() - this.sprite.getWidth() / 2;
        }
        if ((dy < 0) && (y + (delta * dy) <= this.sprite.getHeight() / 2)) {//upper bound
            dy = 0;
            y = this.sprite.getHeight() / 2;
        }
        if ((dy > 0) && (y + (delta * dy) >= ResourceFactory.get().getGameWindow().getHeight() - this.sprite.getHeight() / 2)) {//lower bound
            dy = 0;
            y = ResourceFactory.get().getGameWindow().getHeight() - this.sprite.getHeight() / 2;
        }

        //move and size hitbox according to ship roll
        int frameRef = Math.abs(5 - shipRollFrame);
        switch (frameRef) {
            case 5:
                hitBox = new Rectangle((int) ((x - 7) + ((delta * dx) / 1000)), (int) (y + (delta * dy) / 1000), 14, 20);
                break;
            case 4:
                hitBox = new Rectangle((int) ((x - 8) + ((delta * dx) / 1000)), (int) (y + (delta * dy) / 1000), 16, 20);
                break;
            case 3:
                hitBox = new Rectangle((int) ((x - 10) + ((delta * dx) / 1000)), (int) (y + (delta * dy) / 1000), 20, 20);
                break;
            case 2:
                hitBox = new Rectangle((int) ((x - 12) + ((delta * dx) / 1000)), (int) (y + (delta * dy) / 1000), 24, 20);
                break;
            case 1:
                hitBox = new Rectangle((int) ((x - 13) + ((delta * dx) / 1000)), (int) (y + (delta * dy) / 1000), 26, 20);
                break;
            default:
                hitBox = new Rectangle((int) ((x - 14) + ((delta * dx) / 1000)), (int) (y + (delta * dy) / 1000), 28, 20);

        }

        super.move(delta);
    }

    //Notification that the player's ship has collided with something
    // @param other The entity with which the ship has collided
    @Override
    public void collidedWith(Entity other) {
        // if its an alien, notify the game that the player is dead
        if (!flinching) {
            if (other instanceof EnemyEntity) {
                if (((EnemyEntity) other).isDead()) {
                    return;
                } else {
                    die();
                }

            } else if (other instanceof EnemyShotEntity) {
                if (((EnemyShotEntity) other).isHit()) {
                    return;
                } else {
                    die();
                }
            }
        }
        if (other instanceof Item) {
            if (other instanceof PowerUp) {
                powerUp(((Item) other).getType());
            }
        }
    }

    /**
     * kill player, notify gamestate, create shrapnel, decrement score, create
     * explosion
     */
    private void die() {
        hitBox.setLocation(-200, -200);
        if (shotLevel > 1 || shotType == LASER) {
            Item pu = new PowerUp(game, (int) x, (int) y, "");
            game.getItems().add(pu);
        }

        Score.addDeath();
        Score.addScore(Score.getDeaths() * -1000);

        MedExplosion me = new MedExplosion(game, (int) x, (int) y, "");
        game.getEffects().add(me);
        game.notifyPlayerDeath();
        game.getRemovePlayers().add(this);
        for (int i = 0; i < 8; i++) {
            PlayerShrapnel panel = new PlayerShrapnel(game, 30, 250, 45 * i, (int) x, (int) y, "");
            game.getPlayerEntities().add(panel);
        }

    }

    /**
     * upgrade or swap weapon, increment score class
     *
     * @param type shot type using Item.BULLET for BULLET and Item.LASER for
     * LASER
     */
    private void powerUp(int type) {
        Score.addPowerUp();
        if (type == Item.BULLET) {
            if (shotType == BULLET) {
                if (shotLevel < 8) {
                    shotLevel++;
                    Score.addScore(100 * shotLevel);
                } else {
                    Score.addScore(2000);
                }
            } else if (shotType == LASER) {
                shotType = BULLET;
                shotInterval = bulletInterval;
                Score.addScore(100 * shotLevel);
            }
        } else if (type == Item.LASER) {
            if (shotType == LASER) {
                if (shotLevel < 8) {
                    shotLevel++;
                    Score.addScore(100 * shotLevel);
                } else {
                    Score.addScore(2000);
                }
            } else if (shotType == BULLET) {
                shotType = LASER;
                shotInterval = laserInterval;
                Score.addScore(100 * shotLevel);
            }
        }
    }

}
