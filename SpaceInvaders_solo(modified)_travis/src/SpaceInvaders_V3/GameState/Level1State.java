package SpaceInvaders_V3.GameState;

import SpaceInvaders_V3.Entity.Entity;
import SpaceInvaders_V3.Entity.PlayerEntity;
import SpaceInvaders_V3.Entity.ShotEntity;
import SpaceInvaders_V3.Entity.EnemyEntity;
import SpaceInvaders_V3.Main.GameWindow;
import SpaceInvaders_V3.Main.ResourceFactory;
import SpaceInvaders_V3.TileMap.EntityMap;
import SpaceInvaders_V3.TileMap.TileMap;
import SpaceInvaders_V3.Util.Font;
import SpaceInvaders_V3.Util.SystemTimer;
import java.awt.event.KeyEvent;
import java.util.Random;

//gamestate for level 1
public class Level1State extends GameState {

    //random number generator
    public static final Random random = new Random();

    //game window
    private GameWindow window = ResourceFactory.get().getGameWindow();

    //text rendering
    private Font debugText = new Font("resources/tilesets/ccRed.png", "/resources/tilesets/cc.dat");
    //timing variables
    private double timeStart;

    //keyevent vars
    public boolean enemyTrigger;//debug enemy targeting

    //Tile map for level
    private TileMap tileMap;
    private float mapShift;
    private float posShift;

    
    
    //map of enemies
    private EntityMap entityMap;

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
        tileMap.setPosistion((window.getWidth() - tileMap.getWidth()) / 2, -tileMap.getHeight() + window.getHeight());//set beginning position to bottom of map

        //initialize entityMap
        entityMap = new EntityMap(this, tileMap, "src/resources/maps/level_1.enemies");

        // clear out any existing entities and intialise a new set
        getPlayerEntities().clear();
        getEnemyEntities().clear();
        getPlayers().clear();

        //add player
        ship1 = new PlayerEntity(
                this,
                (window.getWidth()) / 2,
                (window.getHeight()) - 100,
                "resources/sprites/player/Player1Sprite.png",
                40
        );
        getPlayers().add(ship1);

        //start a clock
        timeStart = SystemTimer.getTime();
    }

//    //create enemyship at random location at
//    //random intervals between 1 and 5 secs
//    //for testing purposes mostly
//    private long lastSpawn = 0;
//    private long waitSpawn = random.nextInt(20);
//
//    private void spawnEnemy() {
//        int xPop = random.nextInt((int) (0.9 * window.getWidth() )) + (int) (0.05 * window.getWidth() );//random point within center 90% of screen
//        if (System.currentTimeMillis() - lastSpawn > waitSpawn) {
//
//            EnemyEntity enemyShip = new EnemyEntity(this, xPop, -100, "resources/sprites/enemy/enemyship1a.png");
//            enemyShip.setVerticalMovement(random.nextInt(50) + 50);
//            this.getEnemyEntities().add(enemyShip);
//            waitSpawn = random.nextInt(20);
//            lastSpawn = System.currentTimeMillis();
////            System.out.println("LastSpawn = "+lastSpawn+" WaitSpawn = "+waitSpawn);
////            System.out.println("LastTimer = "+(System.currentTimeMillis() - lastSpawn));
//
//        }
//        
//
//    }
    //update gamestate
    //@param delta timing vector
    @Override
    public void gameUpdate(double delta) {
        //measure time
        double clockTime = SystemTimer.getTime() - timeStart;

        entityMap.update(delta);

        //map parrallax movement
//        tileMap.setPosistion((float) (tileMap.getX() - ((ship1.getHorizontalMovement() * delta) * 0.6f)), tileMap.getY());
        tileMap.setHorizontalMovement(ship1.getHorizontalMovement() * -0.6f);
        tileMap.move(delta);

        moveEnemyEntities(delta);
        movePlayerEntities(delta);
        movePlayers(delta);

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
        if (clockTime > 3) {
            tileMap.setVerticalMovement(25);
//            spawnEnemy();
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
    public void gameRender() {

        tileMap.draw();

//        // cycle round drawing all the enemy entities we have in the game
//        for (int i = 0; i < getEnemyEntities().size(); i++) {
//            Entity entity = (Entity) getEnemyEntities().get(i);
//
//            entity.draw();
//        }
        entityMap.draw();

        // cycle round drawing all the player entities we have in the game
        for (int i = getPlayerEntities().size() - 1; i >= 0; i--) {
            Entity entity = (Entity) getPlayerEntities().get(i);

            entity.draw();
        }
        
        for(int i = 0; i< getPlayers().size(); i++){
            PlayerEntity player = (PlayerEntity) getPlayers().get(i);
            player.draw();
        }

        //debugging
//        g.setColor(Color.RED);
//        g.setFont(new Font("Arial", Font.PLAIN, 12));
        debugText.draw("AverageFPS: " + window.getFPS(), 15, 15);
        debugText.draw("Player: x=" + ship1.getX() + ", y=" + ship1.getY(), 15, 30);
        debugText.draw("Enemy Entity Count: " + getEnemyEntities().size(), 15, 45);
        debugText.draw("Player Entity Count: " + getPlayerEntities().size(), 15, 60);
        debugText.draw("MapPos: x=" + tileMap.getX() + " y=" + tileMap.getY(), 15, 75);
//        debugText.draw("MapShift: " + mapShift, 15, 90);
//        debugText.draw("PosShift: " + posShift, 15, 105);
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

    private void moveEnemyEntities(double delta) {

        // cycle round asking each enemyEntity to move itself
        if (getEnemyEntities().size() > 0) {
            for (int i = 0; i < getEnemyEntities().size(); i++) {
                Entity entity = (Entity) getEnemyEntities().get(i);
                entity.move(delta, tileMap.getHorizontalMovement());
                entity.doLogic();

                //boud check. Remove entity if it strays too far off entity map
                if (entity.getX() < tileMap.getX() - 50 || entity.getX() > tileMap.getX() + tileMap.getWidth() + 50 || entity.getY() > ResourceFactory.get().getGameWindow().getHeight() + 50 || entity.getY() < - 500) {
                    getRemoveEnemyList().add(entity);
                }
            }
        }
    }

    private void movePlayerEntities(double delta) {
        // cycle round asking each playerEntity to move itself
        if (getPlayerEntities().size() > 0) {
            for (int i = 0; i < getPlayerEntities().size(); i++) {
                Entity entity = (Entity) getPlayerEntities().get(i);
                entity.move(delta, tileMap.getHorizontalMovement());

                entity.doLogic();

                //boud check. Remove entity if it strays too far off entity map
                if (entity.getX() < tileMap.getX() - 50 || entity.getX() > tileMap.getX() + tileMap.getWidth() + 50 || entity.getY() > ResourceFactory.get().getGameWindow().getHeight() || entity.getY() < - 20) {
                    getRemovePlayerList().add(entity);
  
                }
            }
        }
    }
    
    private void movePlayers(double delta){
        if(getPlayers().size() > 0){
            for(int i=0; i<getPlayers().size(); i++){
                Entity ship = (Entity) getPlayers().get(i);
                ship.move(delta);
                ship.doLogic();
            }
        }
    }
}
