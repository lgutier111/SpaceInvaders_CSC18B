package edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.GameState;

import edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.Entity.BossEntity;
import edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.Entity.EnemyEntity;
import edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.Entity.Entity;
import edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.Entity.PlayerEntity;
import edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.Entity.ShotEntity;
import edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.Main.GamePanel;
import edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.TileMap.TileMap;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.Random;

//gamestate for level 1
public class Level1State extends GameState {

    //random number generator
    private final Random random = new Random();

    //timing variables
    private long timeStart;

    //keyevent vars
    public boolean enemyTrigger;//debug enemy targeting

    //Tile map for level
    private TileMap tileMap;
    private float mapShift;
    private float posShift;

    // The entity representing the player
    private Entity ship1;

    //constructor metod
    //@param GameStateManager for game
    public Level1State(GameStateManager gsm) {
        this.gsm = gsm;
        init();//initialize gamestate
    }

    //initialize gamestate
    @Override
    public void init() {

        //initialize tilemap
        tileMap = new TileMap(40);
        tileMap.loadTiles("resources/tilesets/SpaceTileSet.png");
        tileMap.loadMap("/resources/maps/level_1.map");
        tileMap.setPosistion((GamePanel.G_WIDTH - tileMap.getWidth()) / 2, -tileMap.getHeight() + GamePanel.G_HEIGHT);//set beginning position to bottom of map

        // clear out any existing entities and intialise a new set
        getPlayerEntities().clear();
        getEnemyEntities().clear();

        //add player
        ship1 = new PlayerEntity(
                this,
                (GamePanel.G_WIDTH) / 2,
                (GamePanel.G_HEIGHT) - 100,
                "resources/sprites/player/Player1Sprite.png",
                40
        );
        getPlayerEntities().add(ship1);

        //start a clock
        timeStart = System.currentTimeMillis();
    }

    //create enemyship at random location at
    //random intervals between 1 and 5 secs
    //for testing purposes mostly
    private long lastSpawn = 0;
    private long waitSpawn = random.nextInt(20);

    private void spawnEnemy() {
        int xPop = random.nextInt((int) (0.9 * GamePanel.G_WIDTH)) + (int) (0.05 * GamePanel.G_WIDTH);//random point within center 90% of screen
        if (System.currentTimeMillis() - lastSpawn > waitSpawn) {

            EnemyEntity enemyShip = new EnemyEntity(this, xPop, -100, "resources/sprites/enemy/enemyship1a.png");
            enemyShip.setVerticalMovement(random.nextInt(50) + 50);
            this.getEnemyEntities().add(enemyShip);
            waitSpawn = random.nextInt(20);
            lastSpawn = System.currentTimeMillis();
            System.out.println("LastSpawn = "+lastSpawn+" WaitSpawn = "+waitSpawn);
            System.out.println("LastTimer = "+(System.currentTimeMillis() - lastSpawn));

        }
        BossEntity bossShip = new BossEntity(this, GamePanel.G_WIDTH/3, -300, "resources/sprites/enemy/Boss_ship.png" );
        bossShip.setVerticalMovement(random.nextInt(50)+50);
        this.getEnemyEntities().add(bossShip);

    }

    //update gamestate
    //@param delta timing vector
    @Override
    public void gameUpdate(long delta) {
        //measure time
        long clockTime = System.currentTimeMillis() - timeStart;

        //execute player class logic
        ship1.doLogic();

        //map parrallax movement
//        posShift = ((delta * ship1.getHorizontalMovement()) / 1000) * -0.4f;
//        tileMap.setPosistion(tileMap.getX() + posShift, tileMap.getY());
//        mapShift = tileMap.getX() - (GamePanel.G_WIDTH - tileMap.getWidth()) / 2;
        mapShift = tileMap.getX();
        tileMap.setPosistion((float) (-0.5 * ship1.getX() + 50), tileMap.getY());
        posShift = mapShift - tileMap.getX();

        // cycle round asking each enemyEntity to move itself
        if (getEnemyEntities().size() > 0) {
            for (int i = 0; i < getEnemyEntities().size(); i++) {
                Entity entity = (Entity) getEnemyEntities().get(i);

//                if (mapShift > -100 && mapShift < 100) {
                entity.setX(entity.getX() - posShift);
//                     System.out.println(posShift);
//                }
                entity.move(delta);
            }
        }
        // cycle round asking each playerEntity to move itself
        if (getPlayerEntities().size() > 0) {
            for (int i = 0; i < getPlayerEntities().size(); i++) {
                Entity entity = (Entity) getPlayerEntities().get(i);
                if (entity instanceof ShotEntity) {
                    entity.setX(entity.getX() - posShift);
                }
                entity.move(delta);
            }
        }

        //testing enemy targetting
        if (enemyTrigger) {
            for (int k = 0; k < this.getEnemyEntities().size(); k++) {
                if (this.getEnemyEntities().get(k) instanceof EnemyEntity) {
                    EnemyEntity enemy = (EnemyEntity) getEnemyEntities().get(k);
                    enemy.shoot(450, ship1, 0);
                }
            }
        }

        //level scripts
        //use map y position or clock time to trigger events
        if (clockTime / 1000.0f > 1) {
            tileMap.setPosistion(tileMap.getX(), (float) (tileMap.getY() -(delta * 50) ));
            spawnEnemy();
//            System.out.println("yshift: "+ (float) ((delta * 1) / 1000.0));
        }

        // brute force collisions, compare every player entity against
        // every enemy entity. If any of them collide notify 
        // both entities that the collision has occurred
        for (int p = 0; p < this.getPlayerEntities().size(); p++) {
            for (int s = 0; s < this.getEnemyEntities().size(); s++) {
                Entity pe = (Entity) this.getPlayerEntities().get(p);
                Entity ee = (Entity) this.getEnemyEntities().get(s);

                if (pe.collidesWith(ee)) {
                    pe.collidedWith(ee);
                    ee.collidedWith(pe);
                }
            }
        }
        // remove any entity that has been marked for clear up
        getEnemyEntities().removeAll(getRemoveEnemyList());
        getRemoveEnemyList().clear();
        getPlayerEntities().removeAll(getRemovePlayerList());
        getRemovePlayerList().clear();

    }

    //render image buffer (bottom later first, top layer last)
    //@param game graphics context
    @Override
    public void gameRender(Graphics2D g) {
        //clear screen
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GamePanel.G_WIDTH, GamePanel.G_HEIGHT);//clear image buffer
        //draw tilemap
        tileMap.draw(g);

        // cycle round drawing all the enemy entities we have in the game
        for (int i = 0; i < getEnemyEntities().size(); i++) {
            Entity entity = (Entity) getEnemyEntities().get(i);

            entity.draw(g);
        }
        // cycle round drawing all the player entities we have in the game
        for (int i = getPlayerEntities().size() - 1; i >= 0; i--) {
            Entity entity = (Entity) getPlayerEntities().get(i);

            entity.draw(g);
        }

        //debugging
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.drawString("AverageFPS: " + GamePanel.averageFPS, 15, 15);
        g.drawString("Player: x=" + ship1.getX() + ", y=" + ship1.getY(), 15, 30);
        g.drawString("Enemy Entity Count: " + getEnemyEntities().size(), 15, 45);
        g.drawString("Player Entity Count: " + getPlayerEntities().size(), 15, 60);
        g.drawString("MapPos: x=" + tileMap.getX() + " y=" + tileMap.getY(), 15, 75);
        g.drawString("MapShift: " + mapShift, 15, 90);
        g.drawString("PosShift: " + posShift, 15, 105);
//        System.out.println(getEntities());
    }

    //keypress functions
    //@param keyevent keycode
    @Override
    public void keyPressed(int k) {
        if (k == KeyEvent.VK_NUMPAD0) {
            enemyTrigger = true;
        }
        if (k == KeyEvent.VK_A) {
            ((PlayerEntity) ship1).setLeft(true);
        }
        if (k == KeyEvent.VK_D) {
            ((PlayerEntity) ship1).setRight(true);
        }
        if (k == KeyEvent.VK_W) {
            ((PlayerEntity) ship1).setUp(true);
        }
        if (k == KeyEvent.VK_S) {
            ((PlayerEntity) ship1).setDown(true);
        }
        if (k == KeyEvent.VK_SPACE) {
            ((PlayerEntity) ship1).setTrigger(true);
        }
    }

    //keyrelease functions
    //@param keyevent keycode
    @Override
    public void keyReleased(int k) {
        if (k == KeyEvent.VK_NUMPAD0) {
            enemyTrigger = false;
        }
        if (k == KeyEvent.VK_A) {
            ((PlayerEntity) ship1).setLeft(false);
        }
        if (k == KeyEvent.VK_D) {
            ((PlayerEntity) ship1).setRight(false);
        }
        if (k == KeyEvent.VK_W) {
            ((PlayerEntity) ship1).setUp(false);
        }
        if (k == KeyEvent.VK_S) {
            ((PlayerEntity) ship1).setDown(false);
        }
        if (k == KeyEvent.VK_SPACE) {
            ((PlayerEntity) ship1).setTrigger(false);
        }
    }

    //keytyped functions
    //@param keyevent keycode
    @Override
    public void keyTyped(int k) {

    }
}
