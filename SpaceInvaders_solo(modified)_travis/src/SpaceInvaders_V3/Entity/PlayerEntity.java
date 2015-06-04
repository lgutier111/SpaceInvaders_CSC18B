package SpaceInvaders_V3.Entity;

import SpaceInvaders_V3.GameState.GameState;
import SpaceInvaders_V3.Main.ResourceFactory;

import SpaceInvaders_V3.Util.Sprite;
import java.awt.Color;
import java.awt.Rectangle;

//player controlled entity
public class PlayerEntity extends Entity {

    //the game in which the ship exists
    private GameState game;

    //animation frames
    private Sprite[][] frames;
    private Sprite[][] shadowFrames;
    private Sprite[][] thrusterFrames;
    private long lastFrameChange = 0;
    private int shipRollFrame = 0;
    private int thrusterRow = 0;
    private int thrusterCol = 0;
    private int[] thrusterPos = new int[4];

    //Time since players last shot
    private long lastShot = 0;
    //player shootrate in 1/ms
    private long shotInterval = 80;

    //shot vars
    private int shotType;
    private int shotLevel;

    public static final int BULLET = 1;
    public static final int LASER = 2;

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

    //Create a new entity to represent the players ship
    // @param game The game in which the ship is being created
    // @param x The initial x location of the player's ship
    // @param y The initial y location of the player's ship
    // @param ref The reference to the sprite to show for the ship
    // @param tileSize sixe in pixels in which to split image into frames
    public PlayerEntity(GameState game, int x, int y, String ref, int tileSize) {
        super(x, y, ref);
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

        thrusterFrames = new Sprite[2][4];
        for (int col = 0; col < 4; col++) {
            for (int row = 0; row < 2; row++) {
                thrusterFrames[row][col] = ResourceFactory.get().getSubSprite(
                        "resources/sprites/player/thrusters.png",
                        col * 10,
                        row * 15,
                        10,
                        15
                );
            }
        }

        //set default frame
        shipRollFrame = 5;
        sprite = frames[0][shipRollFrame];

        //initialize key event vars
        up = down = left = right = trigger = false;

        shotType = BULLET;
        shotLevel = 1;
    }

    //do logic associated with this player
    @Override
    public void doLogic() {

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

        //ship tilt animation
        if (System.currentTimeMillis() - lastFrameChange > 40) {
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
            lastFrameChange = System.currentTimeMillis();

        }

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

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public void setTrigger(boolean trigger) {
        this.trigger = trigger;
    }

    //Shooting method
    public void shoot() {
        //check firing interval
        if (System.currentTimeMillis() - lastShot < shotInterval) {
            return;
        }
        //shoot if interval has passed
        lastShot = System.currentTimeMillis();
        if (shotType == BULLET) {
            if (shotLevel == 1) {
                if (game.getPlayerEntities().size() / 1 <= 5) {
                    ShotEntity shot = new ShotEntity(game, 2, 750, 270, (int) (x), (int) (y - 6), "resources/sprites/player/shotBullet1.png");
                    game.getPlayerEntities().add(shot);
                }
            } else if (shotLevel == 2) {
                if (game.getPlayerEntities().size() / 2 <= 5) {
                    ShotEntity shot1 = new ShotEntity(game, 2, 750, -90, (int) (x + 6), (int) (y - 6), "resources/sprites/player/shotBullet2.png");
                    game.getPlayerEntities().add(shot1);
                    ShotEntity shot2 = new ShotEntity(game, 2, 750, -90, (int) (x - 6), (int) (y - 6), "resources/sprites/player/shotBullet2.png");
                    game.getPlayerEntities().add(shot2);
                }

            } else if (shotLevel == 3) {
                if (game.getPlayerEntities().size() / 3 <= 5) {
                    ShotEntity shot1 = new ShotEntity(game, 2, 750, 280, (int) (x + 6), (int) (y - 6), "resources/sprites/player/shotBullet2.png");
                    game.getPlayerEntities().add(shot1);
                    ShotEntity shot2 = new ShotEntity(game, 2, 750, 270, (int) (x), (int) (y - 6), "resources/sprites/player/shotBullet2.png");
                    game.getPlayerEntities().add(shot2);
                    ShotEntity shot3 = new ShotEntity(game, 2, 750, 260, (int) (x - 6), (int) (y - 6), "resources/sprites/player/shotBullet2.png");
                    game.getPlayerEntities().add(shot3);
                }

            } else if (shotLevel == 4) {
                if (game.getPlayerEntities().size() / 3 <= 5) {
                    ShotEntity shot1 = new ShotEntity(game, 3, 750, 280, (int) (x + 6), (int) (y - 6), "resources/sprites/player/shotBullet3.png");
                    game.getPlayerEntities().add(shot1);
                    ShotEntity shot2 = new ShotEntity(game, 3, 750, 270, (int) (x), (int) (y - 6), "resources/sprites/player/shotBullet3.png");
                    game.getPlayerEntities().add(shot2);
                    ShotEntity shot3 = new ShotEntity(game, 3, 750, 260, (int) (x - 6), (int) (y - 6), "resources/sprites/player/shotBullet3.png");
                    game.getPlayerEntities().add(shot3);
                }

            } else if (shotLevel == 5) {
                if (game.getPlayerEntities().size() / 5 <= 5) {
                    ShotEntity shot1 = new ShotEntity(game, 2, 750, 290, (int) (x + 6), (int) (y - 6), "resources/sprites/player/shotBullet2.png");
                    game.getPlayerEntities().add(shot1);
                    ShotEntity shot2 = new ShotEntity(game, 2, 750, 280, (int) (x + 6), (int) (y - 6), "resources/sprites/player/shotBullet2.png");
                    game.getPlayerEntities().add(shot2);
                    ShotEntity shot3 = new ShotEntity(game, 2, 750, 270, (int) (x), (int) (y - 6), "resources/sprites/player/shotBullet2.png");
                    game.getPlayerEntities().add(shot3);
                    ShotEntity shot4 = new ShotEntity(game, 2, 750, 260, (int) (x - 6), (int) (y - 6), "resources/sprites/player/shotBullet2.png");
                    game.getPlayerEntities().add(shot4);
                    ShotEntity shot5 = new ShotEntity(game, 2, 750, 250, (int) (x - 6), (int) (y - 6), "resources/sprites/player/shotBullet2.png");
                    game.getPlayerEntities().add(shot5);
                }
            } else if (shotLevel == 6) {
                if (game.getPlayerEntities().size() / 5 <= 5) {
                    ShotEntity shot1 = new ShotEntity(game, 3, 750, 290, (int) (x + 6), (int) (y - 6), "resources/sprites/player/shotBullet3.png");
                    game.getPlayerEntities().add(shot1);
                    ShotEntity shot2 = new ShotEntity(game, 3, 750, 280, (int) (x + 6), (int) (y - 6), "resources/sprites/player/shotBullet3.png");
                    game.getPlayerEntities().add(shot2);
                    ShotEntity shot3 = new ShotEntity(game, 3, 750, 270, (int) (x), (int) (y - 6), "resources/sprites/player/shotBullet3.png");
                    game.getPlayerEntities().add(shot3);
                    ShotEntity shot4 = new ShotEntity(game, 3, 750, 260, (int) (x - 6), (int) (y - 6), "resources/sprites/player/shotBullet3.png");
                    game.getPlayerEntities().add(shot4);
                    ShotEntity shot5 = new ShotEntity(game, 3, 750, 250, (int) (x - 6), (int) (y - 6), "resources/sprites/player/shotBullet3.png");
                    game.getPlayerEntities().add(shot5);
                }
            } else if (shotLevel == 7) {
                if (game.getPlayerEntities().size() / 5 <= 5) {
                    ShotEntity shot1 = new ShotEntity(game, 4, 750, 290, (int) (x + 6), (int) (y - 6), "resources/sprites/player/shotBullet4.png");
                    game.getPlayerEntities().add(shot1);
                    ShotEntity shot2 = new ShotEntity(game, 4, 750, 280, (int) (x + 6), (int) (y - 6), "resources/sprites/player/shotBullet4.png");
                    game.getPlayerEntities().add(shot2);
                    ShotEntity shot3 = new ShotEntity(game, 4, 750, 270, (int) (x), (int) (y - 6), "resources/sprites/player/shotBullet4.png");
                    game.getPlayerEntities().add(shot3);
                    ShotEntity shot4 = new ShotEntity(game, 4, 750, 260, (int) (x - 6), (int) (y - 6), "resources/sprites/player/shotBullet4.png");
                    game.getPlayerEntities().add(shot4);
                    ShotEntity shot5 = new ShotEntity(game, 4, 750, 250, (int) (x - 6), (int) (y - 6), "resources/sprites/player/shotBullet4.png");
                    game.getPlayerEntities().add(shot5);
                }
            } else if (shotLevel == 8) {
                if (game.getPlayerEntities().size() / 7 <= 5) {
                    ShotEntity shot1 = new ShotEntity(game, 4, 750, 315, (int) (x + 6), (int) (y - 6), "resources/sprites/player/shotBullet4R.png");
                    game.getPlayerEntities().add(shot1);
                    ShotEntity shot2 = new ShotEntity(game, 4, 750, 290, (int) (x + 6), (int) (y - 6), "resources/sprites/player/shotBullet4.png");
                    game.getPlayerEntities().add(shot2);
                    ShotEntity shot3 = new ShotEntity(game, 4, 750, 280, (int) (x + 6), (int) (y - 6), "resources/sprites/player/shotBullet4.png");
                    game.getPlayerEntities().add(shot3);
                    ShotEntity shot4 = new ShotEntity(game, 4, 750, 270, (int) (x), (int) (y - 6), "resources/sprites/player/shotBullet4.png");
                    game.getPlayerEntities().add(shot4);
                    ShotEntity shot5 = new ShotEntity(game, 4, 750, 260, (int) (x - 6), (int) (y - 6), "resources/sprites/player/shotBullet4.png");
                    game.getPlayerEntities().add(shot5);
                    ShotEntity shot6 = new ShotEntity(game, 4, 750, 250, (int) (x - 6), (int) (y - 6), "resources/sprites/player/shotBullet4.png");
                    game.getPlayerEntities().add(shot6);
                    ShotEntity shot7 = new ShotEntity(game, 4, 750, 225, (int) (x - 6), (int) (y - 6), "resources/sprites/player/shotBullet4L.png");
                    game.getPlayerEntities().add(shot7);
                }
            }
        }

    }

    //override draw method
    @Override
    public void draw() {
        //draw thruster frames
        thrusterFrames[thrusterRow][thrusterCol].draw(thrusterPos[0], thrusterPos[1]);
        thrusterFrames[thrusterRow][thrusterCol].draw(thrusterPos[2], thrusterPos[3]);

        //draw shadow image under ship
        shadowFrames[0][shipRollFrame].draw((int) x - (shadowFrames[0][shipRollFrame].getWidth() / 2) + 20,
                (int) y - (shadowFrames[0][shipRollFrame].getHeight() / 2) + 20);

        //draw ship
        super.draw();

        //draw hitbox for debugging
//        (ResourceFactory.get().getGameWindow()).fillRect(Color.BLUE, hitBox);
    }

    //move ship
    //@param delta time in miliseconds since last movement
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
    public void collidedWith(Entity other) {
        // if its an alien, notify the game that the player is dead
//        if (other instanceof EnemyEntity) {
//
//
//        }
    }

}
