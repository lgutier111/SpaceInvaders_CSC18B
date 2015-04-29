package edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.Entity;

import edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.GameState.GameState;
import edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.Main.GamePanel;
import edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.Main.ImageShader;

import edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.Main.Sprite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

//player controlled entity
public class PlayerEntity extends Entity {

    //the game in which the ship exists
    private GameState game;

    //animation frames
    private Sprite[][] frames;
    private long lastFrameChange = 0;
    private int currentFrame;

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
        BufferedImage tempImage = (BufferedImage) sprite.getImage();
        int cols = sprite.getWidth() / tileSize;
        int rows = sprite.getHeight() / tileSize;
        frames = new Sprite[rows][cols];

        for (int col = 0; col < cols; col++) {
            for (int row = 0; row < rows; row++) {
                frames[row][col] = new Sprite(
                        (Image) tempImage.getSubimage(
                                col * tileSize, row * tileSize, tileSize, tileSize
                        )
                );
            }

        }

        //set default frame
        sprite = frames[0][5];
        currentFrame = 5;

        //initialize key event vars
        up = down = left = right = trigger = false;

        shotType = BULLET;
        shotLevel = 8;
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
        if (System.currentTimeMillis() - lastFrameChange > 20) {
            if (dx < 0) {
                if (currentFrame > 0) {
                    currentFrame--;
                    lastFrameChange = System.currentTimeMillis();
                }

            } else if (dx == 0) {
                if (currentFrame > 5) {
                    currentFrame--;
                    lastFrameChange = System.currentTimeMillis();
                } else if (currentFrame < 5) {
                    currentFrame++;
                    lastFrameChange = System.currentTimeMillis();
                }
            } else {
                if (currentFrame < frames[0].length - 1) {
                    currentFrame++;
                    lastFrameChange = System.currentTimeMillis();
                }

            }
            sprite = frames[0][currentFrame];
        }

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
                if (ShotEntity.getShots() / 1 <= 5) {
                    ShotEntity shot = new ShotEntity(game, 2, 750, 270, (int) (x), (int) (y - 6), "resources/sprites/player/shotBullet1.png");
                    game.getPlayerEntities().add(shot);
                }
            } else if (shotLevel == 2) {
                if (ShotEntity.getShots() / 2 <= 5) {
                    ShotEntity shot1 = new ShotEntity(game, 2, 750, -90, (int) (x + 6), (int) (y - 6), "resources/sprites/player/shotBullet2.png");
                    game.getPlayerEntities().add(shot1);
                    ShotEntity shot2 = new ShotEntity(game, 2, 750, -90, (int) (x - 6), (int) (y - 6), "resources/sprites/player/shotBullet2.png");
                    game.getPlayerEntities().add(shot2);
                }

            } else if (shotLevel == 3) {
                if (ShotEntity.getShots() / 3 <= 5) {
                    ShotEntity shot1 = new ShotEntity(game, 2, 750, 280, (int) (x + 6), (int) (y - 6), "resources/sprites/player/shotBullet2.png");
                    game.getPlayerEntities().add(shot1);
                    ShotEntity shot2 = new ShotEntity(game, 2, 750, 270, (int) (x), (int) (y - 6), "resources/sprites/player/shotBullet2.png");
                    game.getPlayerEntities().add(shot2);
                    ShotEntity shot3 = new ShotEntity(game, 2, 750, 260, (int) (x - 6), (int) (y - 6), "resources/sprites/player/shotBullet2.png");
                    game.getPlayerEntities().add(shot3);
                }

            } else if (shotLevel == 4) {
                if (ShotEntity.getShots() / 3 <= 5) {
                    ShotEntity shot1 = new ShotEntity(game, 3, 750, 280, (int) (x + 6), (int) (y - 6), "resources/sprites/player/shotBullet3.png");
                    game.getPlayerEntities().add(shot1);
                    ShotEntity shot2 = new ShotEntity(game, 3, 750, 270, (int) (x), (int) (y - 6), "resources/sprites/player/shotBullet3.png");
                    game.getPlayerEntities().add(shot2);
                    ShotEntity shot3 = new ShotEntity(game, 3, 750, 260, (int) (x - 6), (int) (y - 6), "resources/sprites/player/shotBullet3.png");
                    game.getPlayerEntities().add(shot3);
                }

            } else if (shotLevel == 5) {
                if (ShotEntity.getShots() / 5 <= 5) {
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
                if (ShotEntity.getShots() / 5 <= 5) {
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
                if (ShotEntity.getShots() / 5 <= 5) {
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
            }else if (shotLevel == 8) {
                if (ShotEntity.getShots() / 7 <= 5) {
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
    public void draw(Graphics g) {

        //draw shadow image under ship
        BufferedImage shadow = ImageShader.generateMask((BufferedImage) sprite.getImage(), Color.BLACK, 0.5f);
        g.drawImage(shadow, (int) x, (int) y, null);

        //draw ship
        super.draw(g);

        //draw hitbox for debugging
//        g.setColor(Color.BLUE);
//        g.drawRect((int) hitBox.getX(), (int) hitBox.getY(), (int) hitBox.getWidth(), (int) hitBox.getHeight());
    }

    //move ship
    //@param delta time in miliseconds since last movement
    @Override
    public void move(long delta) {
        //set bounds to prevent movement offscreen
        if ((dx < 0) && (x + (delta * dx) / 1000 <= (this.sprite.getWidth() / 2))) {//right bound
            dx = 0;
            x = this.sprite.getWidth() / 2;
        }
        if ((dx > 0) && (x + (delta * dx) / 1000 >= GamePanel.G_WIDTH - this.sprite.getWidth() / 2)) {//left bound
            dx = 0;
            x = GamePanel.G_WIDTH - this.sprite.getWidth() / 2;
        }
        if ((dy < 0) && (y + (delta * dy) / 1000 <= this.sprite.getHeight() / 2)) {//upper bound
            dy = 0;
            y = this.sprite.getHeight() / 2;
        }
        if ((dy > 0) && (y + (delta * dy) / 1000 >= GamePanel.G_HEIGHT - this.sprite.getHeight() / 2)) {//lower bound
            dy = 0;
            y = GamePanel.G_HEIGHT - this.sprite.getHeight() / 2;
        }

        //move and size hitbox according to ship roll
        int frameRef = Math.abs(5 - currentFrame);
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
