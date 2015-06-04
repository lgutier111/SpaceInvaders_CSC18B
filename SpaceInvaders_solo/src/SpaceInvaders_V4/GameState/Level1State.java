package SpaceInvaders_V4.GameState;

import SpaceInvaders_V4.Entity.ShipEntity;
import SpaceInvaders_V4.Entity.Entity;
import SpaceInvaders_V4.Entity.PlayerEntity;
import SpaceInvaders_V4.Main.GameWindow;
import SpaceInvaders_V4.Main.ResourceFactory;
import SpaceInvaders_V4.TileMap.EntityMap;
import SpaceInvaders_V4.TileMap.TileMap;
import SpaceInvaders_V4.Util.Font;
import SpaceInvaders_V4.Util.SystemTimer;
import java.awt.event.KeyEvent;
import java.util.Random;

//gamestate for level 1
public class Level1State extends GameState {

    //random number generator
    public static final Random random = new Random();

    //game window
    private final GameWindow window = ResourceFactory.get().getGameWindow();

    //text rendering
    private final Font debugText = new Font("resources/tilesets/ccRed.png", "/resources/tilesets/cc.dat");
    //timing variables
    private double timeStart;
    private boolean[] events;

    //keyevent vars
    public boolean enemyTrigger;//debug enemy targeting

    //Tile map for level
    private TileMap tileMap;

    //map of enemies
    private EntityMap entityMap;

    // The entity representing the player
//    private Entity ship1;
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
        tileMap = new TileMap("src/resources/maps/level_1.JSON");
        //set beginning position to bottom of map
        tileMap.setPosistion((window.getWidth() - tileMap.getWidth()) / 2, -tileMap.getHeight() + window.getHeight());

        //initialize entityMap
        entityMap = new EntityMap(this, tileMap, "src/resources/maps/level_1.1.enemies");

        // clear out any existing entities and intialize a new set
        getPlayerEntities().clear();
        getEnemyEntities().clear();
        getPlayers().clear();

        up = down = left = right = trigger = false;

        //add player
        PlayerEntity ship = new PlayerEntity(
                this,
                (window.getWidth()) / 2,
                (window.getHeight()) - 100,
                ""
        );
        ship.setThrottle(PlayerEntity.THROTTLE_OFF);
        getPlayers().add(ship);
        respawnTimer = 0;
        playerIsAlive = true;

        events = new boolean[32];

        for (int i = 0; i < events.length; i++) {
            events[i] = true;
        }

        Entity carrier = new ShipEntity(310, 600, "resources/sprites/misc/carrier.png");
        getPlayerEntities().add(carrier);
        //start a clock
        timeStart = SystemTimer.getTime();
        playerControl = false;
        controlTimer = SystemTimer.getTime() + 8;
    }

    //update gamestate
    //@param delta timing vector
    @Override
    public void gameUpdate(double delta) {
        //measure time
        double clockTime = SystemTimer.getTime() - timeStart;

        entityMap.update(delta);

        //map parrallax movement
        float xShift = 0;
        if (getPlayers().size() > 0) {
            int num = 0;
            for (Entity player : getPlayers()) {
                num += player.getHorizontalMovement();
            }
            xShift = num / (float) getPlayers().size();
        }
        tileMap.setHorizontalMovement(xShift * -0.6f);
        tileMap.move(delta);

        moveEntities(delta);

        if (!playerIsAlive) {
            if (SystemTimer.getTime() > respawnTimer) {
                PlayerEntity ship = new PlayerEntity(this,
                        (window.getWidth()) / 2,
                        (window.getHeight()) + 100,
                        "");

                ship.setVerticalMovement(-100);
                ship.setFlinching(6);
                getPlayers().add(ship);
                playerIsAlive = true;
            }
        }
        if (!playerControl && SystemTimer.getTime() > controlTimer) {
            playerControl = true;
            for (Entity player : getPlayers()) {
                ((PlayerEntity) player).setFlinching(2);
            }
        }

        //testing enemy targetting
//        if (enemyTrigger) {
//            for (int k = 0; k < this.getEnemyEntities().size(); k++) {
//                if (this.getEnemyEntities().get(k) instanceof EnemyEntity) {
//                    EnemyEntity enemy = (EnemyEntity) getEnemyEntities().get(k);
//                    enemy.shoot(450, ship1, 0);
//                }
//            }
//        }
        //level scripts
        //eventsArray and/or clock time to trigger events
        if (clockTime > 2 && events[0]) {
            for (Entity player : getPlayers()) {
                ((PlayerEntity) player).setThrottle(PlayerEntity.THROTTLE_TURBO);
            }
            events[0] = false;
        }
        if (clockTime > 3 && events[1]) {
            tileMap.setVerticalMovement(75);
            for (Entity player : getPlayers()) {
                player.setVerticalMovement(75);
            }
            events[1] = false;
        }
        if (clockTime > 3.5 && events[2]) {
            for (Entity player : getPlayers()) {
                player.setVerticalMovement(-65);
            }
            events[2] = false;
        }
        if (clockTime > 8 && events[3]) {
            for (Entity player : getPlayers()) {
                ((PlayerEntity) player).setThrottle(PlayerEntity.THROTTLE_FULL);

            }
            events[3] = false;
        }

        checkCollisoins();

    }

    //render image buffer (bottom later first, top layer last)
    //@param game graphics context
    @Override
    public void gameRender() {

        tileMap.draw();
        entityMap.draw();

        for (Entity item : getItems()) {
            item.draw();
        }

        // cycle round drawing all the player entities we have in the game
        for (int i = 0; i < getPlayerEntities().size(); i++) {
            Entity entity = getPlayerEntities().get(i);
            entity.draw();
        }
        for (Entity effect : getEffects()) {
            effect.draw();
        }

        for (int i = 0; i < getPlayers().size(); i++) {
            Entity player = getPlayers().get(i);
            player.draw();
        }

        //debugging
        debugText.draw("AverageFPS: " + window.getFPS(), 15, 15);
//        debugText.draw("Player: x=" + ship1.getX() + ", y=" + ship1.getY(), 15, 30);
        debugText.draw("Enemy Entity Count: " + getEnemyEntities().size(), 15, 45);
        debugText.draw("Player Entity Count: " + getPlayerEntities().size(), 15, 60);
        debugText.draw("MapPos: x=" + tileMap.getX() + " y=" + tileMap.getY(), 15, 75);
    }

    //keypress functions
    //@param keyevent keycode
    @Override
    public void keyPressed(int k) {
        if (k == KeyEvent.VK_NUMPAD0) {
            enemyTrigger = true;
        }
        if (playerControl) {
            if (k == KeyEvent.VK_A) {
//                ((PlayerEntity) ship1).setLeft(true);
                left = true;
            }
            if (k == KeyEvent.VK_D) {
//                ((PlayerEntity) ship1).setRight(true);;
                right = true;
            }
            if (k == KeyEvent.VK_W) {
//                ((PlayerEntity) ship1).setUp(true);
                up = true;
            }
            if (k == KeyEvent.VK_S) {
//                ((PlayerEntity) ship1).setDown(true);
                down = true;
            }
            if (k == KeyEvent.VK_SPACE) {
//                ((PlayerEntity) ship1).setTrigger(true);
                trigger = true;
            }
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
//            ((PlayerEntity) ship1).setLeft(false);
            left = false;
        }
        if (k == KeyEvent.VK_D) {
//            ((PlayerEntity) ship1).setRight(false);
            right = false;
        }
        if (k == KeyEvent.VK_W) {
//            ((PlayerEntity) ship1).setUp(false);
            up = false;
        }
        if (k == KeyEvent.VK_S) {
//            ((PlayerEntity) ship1).setDown(false);
            down = false;
        }
        if (k == KeyEvent.VK_SPACE) {
//            ((PlayerEntity) ship1).setTrigger(false);
            trigger = false;
        }
    }

    //keytyped functions
    //@param keyevent keycode
    @Override
    public void keyTyped(int k) {

    }

    private void moveEntities(double delta) {
        float xShift = tileMap.getHorizontalMovement();
        float yShift = tileMap.getVerticalMovement();

        // cycle round asking each enemyEntity to move itself
        for (int i = 0; i < getEnemyEntities().size(); i++) {
            Entity enemyEntity = getEnemyEntities().get(i);
            enemyEntity.move(delta, xShift);
            enemyEntity.doLogic();

            //bound check. Remove entity if it strays too far off entity map
            if (enemyEntity.getX() + (enemyEntity.getSprite().getWidth() / 2) < tileMap.getX() - 50
                    || enemyEntity.getX() - (enemyEntity.getSprite().getWidth() / 2) > tileMap.getX() + tileMap.getWidth() + 50
                    || enemyEntity.getY() - (enemyEntity.getSprite().getHeight() / 2) > ResourceFactory.get().getGameWindow().getHeight() + 50
                    || enemyEntity.getY() + (enemyEntity.getSprite().getHeight() / 2) < - 200) {
                getRemoveEnemyList().add(enemyEntity);
            }
        }

        // cycle round asking each playerEntity to move itself
        for (int i = 0; i < getPlayerEntities().size(); i++) {
            Entity playerEntity = getPlayerEntities().get(i);
            if (playerEntity instanceof ShipEntity) {
                playerEntity.move(delta, xShift, yShift);
            } else {
                playerEntity.move(delta, xShift);
            }

            playerEntity.doLogic();

            //bound check. Remove entity if it strays too far off entity map
            if (playerEntity.getX() + (playerEntity.getSprite().getWidth() / 2) < tileMap.getX() - 50
                    || playerEntity.getX() - (playerEntity.getSprite().getWidth() / 2) > tileMap.getX() + tileMap.getWidth() + 50
                    || playerEntity.getY() - (playerEntity.getSprite().getHeight() / 2) > ResourceFactory.get().getGameWindow().getHeight()
                    || playerEntity.getY() + (playerEntity.getSprite().getHeight() / 2) < - 20) {
                getRemovePlayerList().add(playerEntity);

            }
        }

        //move items
        for (int i = 0; i < getItems().size(); i++) {
            Entity item = getItems().get(i);
            item.move(delta, xShift);
            item.doLogic();

            //bound check. Remove entity if it strays too far off entity map
            if (item.getX() < tileMap.getX() - 50
                    || item.getX() > tileMap.getX() + tileMap.getWidth() + 50
                    || item.getY() > ResourceFactory.get().getGameWindow().getHeight()
                    || item.getY() < - 20) {
                getRemoveItems().add(item);

            }
        }

        //move effects
        for (int i = 0; i < getEffects().size(); i++) {
            Entity effect = getEffects().get(i);
            effect.move(delta, xShift);
            effect.doLogic();
        }

        //movePlayers
        for (int i = 0; i < getPlayers().size(); i++) {
            Entity player = getPlayers().get(i);
            player.move(delta);
            player.doLogic();
        }
    }

    private void checkCollisoins() {
        // brute force collisions, compare every player entity against
        // every enemy entity. If any of them collide notify 
        // both entities that the collision has occurred

        //player bullet -> enemy collisions
        for (int p = 0; p < this.getPlayerEntities().size(); p++) {
            Entity pe = (Entity) this.getPlayerEntities().get(p);
            for (int s = 0; s < this.getEnemyEntities().size(); s++) {

                Entity ee = (Entity) this.getEnemyEntities().get(s);

                if (pe.collidesWith(ee)) {
                    pe.collidedWith(ee);
                    ee.collidedWith(pe);
                }
            }
        }

        for (int p = 0; p < this.getPlayers().size(); p++) {

            Entity pe = (Entity) this.getPlayers().get(p);
            //player -> enemy collisions
            for (int e = 0; e < this.getEnemyEntities().size(); e++) {
                Entity ee = (Entity) this.getEnemyEntities().get(e);
                if (pe.collidesWith(ee)) {
                    pe.collidedWith(ee);
                    ee.collidedWith(pe);
                }
            }

            //player-> item collisions
            for (int i = 0; i < this.getItems().size(); i++) {
                Entity ie = (Entity) this.getItems().get(i);
                if (pe.collidesWith(ie)) {
                    pe.collidedWith(ie);
                    ie.collidedWith(pe);
                }
            }
        }

        // remove any entity that has been marked for clear up
        getEnemyEntities().removeAll(getRemoveEnemyList());
        getRemoveEnemyList().clear();
        getPlayerEntities().removeAll(getRemovePlayerList());
        getRemovePlayerList().clear();
        getItems().removeAll(getRemoveItems());
        getRemoveItems().clear();
        getEffects().removeAll(getRemoveEffects());
        getRemoveEffects().clear();
        getPlayers().removeAll(getRemovePlayers());
        getRemovePlayers().clear();
    }

}
