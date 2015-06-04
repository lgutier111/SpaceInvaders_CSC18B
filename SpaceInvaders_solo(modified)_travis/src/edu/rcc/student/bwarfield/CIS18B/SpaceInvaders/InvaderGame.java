package edu.rcc.student.bwarfield.CIS18B.SpaceInvaders;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

public class InvaderGame extends Canvas {

    //random number generator
    private final Random random = new Random();
    //game dimentions
    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    //bufer for page flipping
    private final BufferStrategy strategy;
    // The entity representing the player
    private Entity ship;
    //ships speed in pixels/sec
    private float moveSpeed = 300;
    //Diagonal movement factor
    private final float DIAGONAL = (float) Math.sin(45);
    // True if the game is currently "running"
    private boolean gameRunning = true;
    // The list of all the entities
    private ArrayList entities = new ArrayList();
    //The list of entities that need to be removed from the game this loop
    private ArrayList removeList = new ArrayList();
    //keyboard imput variables
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean upPressed;
    private boolean downPressed;
    private boolean triggerPull;//firing mechanism
    private boolean enemyTrigger;//debug enemy targeting
    //Time since players last shot
    private long lastShot = 0;
    //player shootrate in 1/ms
    private long shotInterval = 225;
    //True if game is paused until a key has been pressed
    private boolean waitingForKeyPress = true;
    //Message displayed when game is paused
    private String message = "";

//counting system
    private final int FPS = 60;//frames per second
    private final long TARGETTIME = 1000 / FPS;

    public InvaderGame() {
        //create game frame
        JFrame container = new JFrame("Invaders From Space");

        //Set Game Area
        JPanel panel = (JPanel) container.getContentPane();
        panel.setPreferredSize(new Dimension(WIDTH - 10, HEIGHT - 10));//jPanel keeps adding a 10px border for some reason
        panel.setLayout(new SpringLayout());
        //set canvas size and add to frame
        setBounds(0, 0, WIDTH, HEIGHT);
        panel.add(this);

        // prevent awt repaint
        setIgnoreRepaint(true);

        //activate frame 
        container.pack();
        container.setResizable(false);
        container.setVisible(true);
        //debugging
//        System.out.println(panel.getHeight() + ", " + panel.getWidth());
//        System.out.println(panel.getPreferredSize());
//        System.out.println(container.getWidth() + ", " + container.getHeight());
//        System.out.println(container.getPreferredSize());

        // add a listener to respond to exit on close
        container.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        //add listener for keyboard events
        addKeyListener(new KeyInputHandler());
        //request the focus so key events come to canvas
        requestFocus();

        //create the buffering strategy 
        createBufferStrategy(2);
        strategy = getBufferStrategy();

        //initialize entities
        initEntities();

    }

    //Dimention Getters
    public int getWIDTH() {
        return WIDTH;
    }

    public int getHEIGHT() {
        return HEIGHT;
    }

    //initialize entities to starting state
    private void initEntities() {
        //create player ship
        ship = new PlayerEntity(this, (WIDTH) / 2, (HEIGHT) - 100, "sprites/whiteSprite.png");
        entities.add(ship);
        EnemyEntity enemyShip = new EnemyEntity(this, (WIDTH) / 2, (HEIGHT) / 2, "sprites/enemyship1a.png");
        entities.add(enemyShip);
    }

    // Remove an entity from the game. The entity removed will no longer move or be drawn
    //@param entity The entity that should be removed
    public void removeEntity(Entity entity) {
        removeList.add(entity);
    }

    //Notification that the player has died.
    public void notifyDeath() {
        message = "You have died!";
        ship.setHorizontalMovement(0);
        ship.setVerticalMovement(0);
        waitingForKeyPress = true;
//        gameRunning = false;
    }

    //Shooting method
    public void trigger() {
        //check firing interval
        if (System.currentTimeMillis() - lastShot < shotInterval) {
            return;
        }
        //shoot if interval has passed
        lastShot = System.currentTimeMillis();
        ShotEntity shot = new ShotEntity(this, ship.getX() + 11, ship.getY() + 10, "sprites/shotBullet1.png");
        entities.add(shot);
    }

    //main game loop. Redraws frames game at FPS constant, manages etity movement
    //precesses keyboard input
    @SuppressWarnings("SleepWhileInLoop")
    public void gameLoop() {
        long lastLoopTime;
        lastLoopTime = System.currentTimeMillis();
        long delta;//time reference for loop duration

        while (gameRunning) {
            delta = System.currentTimeMillis() - lastLoopTime;
            lastLoopTime = System.currentTimeMillis();

            //get and initialize graphics context
            Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
            g.setColor(Color.black);
            g.fillRect(0, 0, WIDTH, HEIGHT);

            //debugging
            g.setColor(Color.GREEN);
            g.drawString("Player: x=" + ship.getX() + ", y=" + ship.getY(), 15, 15);
            g.drawString("Entity Count: " + entities.size(), 15, 30);

            if (!waitingForKeyPress) {
                //spawn enemy
//                spawnEnemy();

                // cycle round asking each entity to move itself
                if (true) {
                    for (int i = 0; i < entities.size(); i++) {
                        Entity entity = (Entity) entities.get(i);

                        entity.move(delta);
                    }
                }
                // cycle round drawing all the entities we have in the game
                for (int i = 0; i < entities.size(); i++) {
                    Entity entity = (Entity) entities.get(i);

                    entity.draw(g);
                }

                // brute force collisions, compare every entity against
                // every other entity. If any of them collide notify 
                // both entities that the collision has occurred
                for (int p = 0; p < entities.size(); p++) {
                    for (int s = p + 1; s < entities.size(); s++) {
                        Entity me = (Entity) entities.get(p);
                        Entity him = (Entity) entities.get(s);

                        if (me.collidesWith(him)) {
                            me.collidedWith(him);
                            him.collidedWith(me);
                        }
                    }
                }

                // remove any entity that has been marked for clear up
                entities.removeAll(removeList);
                if (removeList.size() > 0) {
//                System.out.println("Removed: "+removeList);//for debugging
//                System.out.println("Entities: "+entities);
                }
                removeList.clear();
            }
            // if we're waiting for an "any key" press then draw the 
            // current message 
            if (waitingForKeyPress) {
                g.setColor(Color.white);
                g.drawString(message, (WIDTH - g.getFontMetrics().stringWidth(message)) / 2, (int) (HEIGHT * 0.45));
                g.drawString("Press any key", (WIDTH - g.getFontMetrics().stringWidth("Press any key")) / 2, (int) (HEIGHT * 0.5));
                g.drawString("W,A,S,D for controls. Space to Fire", (WIDTH - g.getFontMetrics().stringWidth("W,A,S,D for controls. Space to Fire")) / 2, (int) (HEIGHT * 0.55));
            }
            //clear graphics and flip buffer
            g.dispose();
            strategy.show();
            //firing mechanism
            if (triggerPull) {
                trigger();
            }

            //testing enemy targetting
            if (enemyTrigger) {
                for (int k = 0; k < entities.size(); k++) {
                    if (entities.get(k) instanceof EnemyEntity) {
//                        ((EnemyEntity)entities.get(k)).shoot(300,ship, 15);//shoot at player
                        ((EnemyEntity) entities.get(k)).shoot(300, ship, true);//shoot at player
//                        ((EnemyEntity) entities.get(k)).shoot(1500);//shoot straight down
//                        for(int j = 0; j<=360; j+=45){
//                            ((EnemyEntity) entities.get(k)).shoot(500,j);//shoot at angle
//                        }

                    }
                }
            }

            // resolve the movement of the ship. First assume the ship 
            // isn't moving. If either cursor key is pressed then
            // update the movement appropraitely
            ship.setHorizontalMovement(0);
            ship.setVerticalMovement(0);

            if ((leftPressed) && (!rightPressed)) {//left
                if ((upPressed) && (!downPressed)) {//upLeft
                    //factor for diagonnal movement speed
                    ship.setVerticalMovement(DIAGONAL * -moveSpeed);
                    ship.setHorizontalMovement(DIAGONAL * -moveSpeed);
                } else if ((downPressed) && (!upPressed)) {//downLeft
                    ship.setVerticalMovement(DIAGONAL * moveSpeed);
                    ship.setHorizontalMovement(DIAGONAL * -moveSpeed);
                } else {
                    ship.setHorizontalMovement(-moveSpeed);//left only
                }
            } else if ((rightPressed) && (!leftPressed)) {//right
                if ((upPressed) && (!downPressed)) {//up
                    ship.setVerticalMovement(DIAGONAL * -moveSpeed);
                    ship.setHorizontalMovement(DIAGONAL * moveSpeed);
                } else if ((downPressed) && (!upPressed)) {//down
                    ship.setVerticalMovement(DIAGONAL * moveSpeed);
                    ship.setHorizontalMovement(DIAGONAL * moveSpeed);

                } else {
                    ship.setHorizontalMovement(moveSpeed);//right only
                }
            } else if ((upPressed) && (!downPressed) && (!rightPressed) && (!leftPressed)) {//up
                ship.setVerticalMovement(-moveSpeed);
            } else if ((downPressed) && (!upPressed) && (!rightPressed) && (!leftPressed)) {//down
                ship.setVerticalMovement(moveSpeed);
            }

            //wait the remaining time to get desited fps
            try {
                Thread.sleep(TARGETTIME - (System.currentTimeMillis() - lastLoopTime) / 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Start a fresh game, this should clear out any old data and create a new
     * set.
     */
    private void startGame() {
        // clear out any existing entities and intialise a new set
        entities.clear();
//        gameRunning = true;
        initEntities();

        // blank out any keyboard settings we might currently have
        leftPressed = false;
        rightPressed = false;
        upPressed = false;
        downPressed = false;
        triggerPull = false;
    }

    //create enemyship at random location at
    //random intervals between 1 and 5 secs
    private long lastSpawn = 0;

    private void spawnEnemy() {
        lastSpawn++;
        int willSpawn = random.nextInt(FPS * 4) + FPS;
        int xPop = random.nextInt((int) (0.9 * WIDTH)) + (int) (0.05 * WIDTH);//random point within center 90% of screen
        if (lastSpawn > 60) {
            if (lastSpawn > 300 || willSpawn > FPS * 4.5) {
                EnemyEntity enemyShip = new EnemyEntity(this, xPop, -100, "sprites/enemyship1a.png");
                enemyShip.setVerticalMovement(random.nextInt(10) + 175);
                entities.add(enemyShip);
                lastSpawn = 0;
            }
        }

    }

    //add enemy shot entity to entity list
    //@param EnemyEntityShot object
    public void addShot(EnemyShotEntity shot) {
        entities.add(shot);
    }

    //Keyboard input handler
    private class KeyInputHandler extends KeyAdapter {

        //The number of key presses we've had while waiting for an "any key" press
        private int pressCount = 1;

        //KeyEvent override: associate key events with game variables
        //@param e keyEvent object
        @Override
        public void keyPressed(KeyEvent e) {
            // if waiting for an "any key" typed, don't do anything
            if (waitingForKeyPress) {
                return;
            }
            if (e.getKeyCode() == KeyEvent.VK_NUMPAD0) {
                enemyTrigger = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_A) {
                leftPressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_D) {
                rightPressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_W) {
                upPressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_S) {
                downPressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                triggerPull = true;
            }

        }

        @Override
        //KeyEvent override
        //@param e keyEvent object
        public void keyReleased(KeyEvent e) {
            // if waiting for "any key", don't do anything
            if (waitingForKeyPress) {
                return;
            }
            if (e.getKeyCode() == KeyEvent.VK_A) {
                leftPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_D) {
                rightPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_W) {
                upPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_S) {
                downPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_NUMPAD0) {
                enemyTrigger = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                triggerPull = false;
                lastShot = 0;
            }

        }

        // Any Key notification
        //@param e key event object
        public void keyTyped(KeyEvent e) {
            // if waiting for a "any key" check if recieved any recently. 
            if (waitingForKeyPress) {
                if (pressCount == 1) {
                    // since we've now recieved our key typed
                    // event we can mark it as such and start 
                    // our new game
                    waitingForKeyPress = false;
                    startGame();
                    pressCount = 0;
                } else {
                    pressCount++;
                }
            }

            // if we hit escape, then quit the game
            if (e.getKeyChar() == 27) {
                System.exit(0);
            }
        }

    }

    //Main method
    public static void main(String[] args) {
        InvaderGame g = new InvaderGame();
        g.gameLoop();
    }
}
