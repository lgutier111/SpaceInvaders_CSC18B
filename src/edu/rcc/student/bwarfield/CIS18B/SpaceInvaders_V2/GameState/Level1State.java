package edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.GameState;

import edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.Entity.EnemyEntity;
import edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.Entity.Entity;
import edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.Entity.PlayerEntity;
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
    private int mapShift;

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
        tileMap.setPosistion((GamePanel.G_WIDTH-tileMap.getWidth())/2, -tileMap.getHeight() + GamePanel.G_HEIGHT);//set beginning position to bottom of map

        // clear out any existing entities and intialise a new set
        getEntities().clear();

        //add player
        ship1 = new PlayerEntity(
                this,
                (GamePanel.G_WIDTH) / 2,
                (GamePanel.G_HEIGHT) - 100,
                "resources/sprites/player/Player1Sprite.png",
                40
        );
        getEntities().add(ship1);

        //start a clock
        timeStart = System.currentTimeMillis();
    }

    //create enemyship at random location at
    //random intervals between 1 and 5 secs
    private long lastSpawn = 0;

    private void spawnEnemy() {
        lastSpawn++;
        int willSpawn = random.nextInt(GamePanel.FPS * 4) + GamePanel.FPS;
        int xPop = random.nextInt((int) (0.9 * GamePanel.G_WIDTH)) + (int) (0.05 * GamePanel.G_WIDTH);//random point within center 90% of screen
        if (lastSpawn > 60) {
            if (lastSpawn > 300 || willSpawn > GamePanel.FPS * 4.5) {
                EnemyEntity enemyShip = new EnemyEntity(this, xPop, -100, "resources/sprites/enemy/enemyship1a.png");
                enemyShip.setVerticalMovement(random.nextInt(10) + 175);
                this.getEntities().add(enemyShip);
                lastSpawn = 0;
            }
        }

    }

    //update gamestate
    //@param delta timing vector
    @Override
    public void gameUpdate(long delta) {
        //measure time
        long clockTime = System.currentTimeMillis() - timeStart;

        //execute player clss logic
        ship1.doLogic();

        

        //map movement
        float posShift = ((delta * ship1.getHorizontalMovement()) / 1000) * -0.6f;
        tileMap.setPosistion(tileMap.getX() + posShift, tileMap.getY());
        mapShift = tileMap.getX()-(GamePanel.G_WIDTH-tileMap.getWidth())/2;
        
        // cycle round asking each entity to move itself
        if (true) {
            for (int i = 0; i < getEntities().size(); i++) {
                Entity entity = (Entity) getEntities().get(i);

                if (entity instanceof EnemyEntity && mapShift >-100 && mapShift <100) {
                     ((EnemyEntity)entity).setX(entity.getX() + posShift);
//                     System.out.println(posShift);
                }
                entity.move(delta);
            }
        }
        
        //testing enemy targetting
            if (enemyTrigger) {
                for (int k = 0; k < this.getEntities().size(); k++) {
                    if (this.getEntities().get(k) instanceof EnemyEntity) {
//                        ((EnemyEntity)this.getEntities().get(k)).shoot(300,ship1, 15);//shoot at angle to player 
                        ((EnemyEntity)this.getEntities().get(k)).shoot(300,ship1, 0);//shoot at angle to player 
//                        ((EnemyEntity) this.getEntities().get(k)).shoot(300, ship1, true);//shoot at player to neaser 45deg
//                        ((EnemyEntity) this.getEntities().get(k)).shoot(1500);//shoot straight down
                        
//                        for(int j = 0; j<=360; j+=45){
//                            ((EnemyEntity) this.getEntities().get(k)).shoot(500,j);//shoot at angle
//                        }

                    }
                }
            }

        //level scripts
        //use map y position or clock time to trigger events
        if (clockTime / 1000.0f > 3) {
            tileMap.setPosistion(tileMap.getX(), (float) (tileMap.getY() + (delta * 1) / 1000.0));
            spawnEnemy();
//            System.out.println("yshift: "+ (float) ((delta * 1) / 1000.0));
        }

        // brute force collisions, compare every entity against
        // every other entity. If any of them collide notify 
        // both entities that the collision has occurred
        for (int p = 0; p < this.getEntities().size(); p++) {
            for (int s = p + 1; s < this.getEntities().size(); s++) {
                Entity e1 = (Entity) this.getEntities().get(p);
                Entity e2 = (Entity) this.getEntities().get(s);

                if (e1.collidesWith(e2)) {
                    e1.collidedWith(e2);
                    e2.collidedWith(e1);
                }
            }
        }
        // remove any entity that has been marked for clear up
        getEntities().removeAll(getRemoveList());
        if (getRemoveList().size() > 0) {
//           System.out.println("Removed: "+removeList);//for debugging
//           System.out.println("Entities: "+entities);
        }
        getRemoveList().clear();

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

        // cycle round drawing all the entities we have in the game
        for (int i = 0; i < getEntities().size(); i++) {
            Entity entity = (Entity) getEntities().get(i);

            entity.draw(g);
        }

        //debugging
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.drawString("AverageFPS: " + GamePanel.averageFPS, 15, 15);
        g.drawString("Player: x=" + ship1.getX() + ", y=" + ship1.getY(), 15, 30);
        g.drawString("Entity Count: " + getEntities().size(), 15, 45);
        g.drawString("MapPos: x=" + tileMap.getX() + " y=" + tileMap.getY(), 15, 60);
        g.drawString("MapShift: " + mapShift, 15, 75);
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
