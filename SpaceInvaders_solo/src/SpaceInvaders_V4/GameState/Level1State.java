package SpaceInvaders_V4.GameState;

import SpaceInvaders_V4.Entity.ShipEntity;
import SpaceInvaders_V4.Entity.Entity;
import SpaceInvaders_V4.Entity.PlayerEntity;
import SpaceInvaders_V4.Entity.PowerUp;
import SpaceInvaders_V4.Main.GameWindow;
import SpaceInvaders_V4.Main.ResourceFactory;
import SpaceInvaders_V4.TileMap.EntityMap;
import SpaceInvaders_V4.TileMap.TileMap;
import SpaceInvaders_V4.Users.Score;
import SpaceInvaders_V4.Util.Font;
import SpaceInvaders_V4.Util.SystemTimer;
import java.awt.Point;
import java.awt.event.KeyEvent;
import static java.lang.Math.*;
import java.util.Random;

//gamestate for level 1
public class Level1State extends GameState {

    //random number generator
    public static final Random random = new Random();

    //game window
    private final GameWindow window = ResourceFactory.get().getGameWindow();

    //text rendering
    private final Font debugText = new Font("resources/tilesets/ccRed.png", "/resources/tilesets/cc.dat");
    private final Font ScoreBoard = new Font("resources/tilesets/PFArmaFive36Gray.png", "/resources/tilesets/PFArmaFive36.dat");
    //timing variables
    private double timeStart;
    private boolean[] events;
    private boolean levelEnd;
    private double eventTimer = 0;

    //keyevent vars
    public boolean enemyTrigger;//debug enemy targeting

    //Tile map for level
    private TileMap tileMap;

    //map of enemies
    private EntityMap entityMap;

    // The entity representing the player
//    private Entity ship1;
    private Entity carrier;

    private Thread submitScore;

    /**
     * constructor method
     *
     * @param gsm GameStateManager for game
     */
    public Level1State(GameStateManager gsm) {
        this.gsm = gsm;
        init();//initialize gamestate
    }

    /**
     * initialize GameState
     */
    @Override
    public void init() {

        //initialize tilemap
        tileMap = new TileMap("src/resources/maps/level_1.JSON");
        //set beginning position to bottom of map
        tileMap.setPosistion((window.getWidth() - tileMap.getWidth()) / 2, -tileMap.getHeight() + window.getHeight());
//        tileMap.setPosistion((window.getWidth() - tileMap.getWidth()) / 2, -3000);

        //initialize entityMap
        entityMap = new EntityMap(this, tileMap, "src/resources/maps/level_1.1.enemies");

        // clear out any existing entities and intialize a new set
        getPlayerEntities().clear();
        getEnemyEntities().clear();
        getPlayers().clear();

        up = down = left = right = trigger = false;

        submitScore = new Thread() {
            @Override
            public void run() {
                Score.submit();
            }
        };

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

        carrier = new ShipEntity(310, 600, "resources/sprites/misc/carrier.png");
        getPlayerEntities().add(carrier);
        //start a clock
        timeStart = SystemTimer.getTime();
        playerControl = false;
        controlTimer = SystemTimer.getTime() + 8;
        levelEnd = false;
    }

    /**
     * update GameState
     *
     * @param delta timing factor
     */
    @Override
    public void gameUpdate(double delta) {
        //measure time
        double clockTime = SystemTimer.getTime() - timeStart;

        entityMap.update(delta);

        //map parrallax movement
        float xShift = 0;
        if (getPlayers().size() > 0) {
            int num = 0;

            for (int i = 0; i < getPlayers().size(); i++) {
                num += getPlayers().get(i).getHorizontalMovement();
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
        if (!playerControl && SystemTimer.getTime() > controlTimer && !levelEnd) {
            playerControl = true;
            for (Entity player : getPlayers()) {
                ((PlayerEntity) player).setFlinching(2);
            }
        }

        //level scripts
        //eventsArray and/or clock time to trigger events
        if (clockTime > 2 && events[0]) {//turn on turbo thruster animation
            for (Entity player : getPlayers()) {
                ((PlayerEntity) player).setThrottle(PlayerEntity.THROTTLE_TURBO);
            }
            events[0] = false;
        }
        if (clockTime > 3 && events[1]) {//start map movement, sync player movement
            tileMap.setVerticalMovement(70);
            for (Entity player : getPlayers()) {
                player.setVerticalMovement(70);
            }
            events[1] = false;
        }
        if (clockTime > 3.5 && events[2]) {//animate player takeoff
            for (Entity player : getPlayers()) {
                player.setVerticalMovement(-50);
            }
            events[2] = false;
        }
        if (clockTime > 8 && events[3]) {//set throttle to full. Player controll will resume
            for (Entity player : getPlayers()) {
                ((PlayerEntity) player).setThrottle(PlayerEntity.THROTTLE_FULL);

            }
            events[3] = false;
        }
        if (tileMap.getY() == 0 && !getEnemyEntities().isEmpty() && events[4]) {//spawn a powerup if end of map is reached and enemies still exist
            if (events[5]) {
                eventTimer = SystemTimer.getTime() + 20;
                events[5] = false;
            }
            if (SystemTimer.getTime() > eventTimer) {
                PowerUp pu = new PowerUp(this, 400, -50, "");
                getItems().add(pu);
                events[4] = false;
            }
        }
        if (tileMap.getY() == 0 && getEnemyEntities().isEmpty() && events[6]) {//initialize end of level sequence when end of map reached and all enemies are dead
            levelEnd = true;
            getItems().clear();
            playerControl = false;
            carrier.setY(1600);
            getPlayerEntities().add(carrier);
            events[6] = false;
            eventTimer = SystemTimer.getTime() + 20;
        }
        if (levelEnd && events[7]) {
            if (tileMap.getX() > window.getWidth() - tileMap.getWidth() / 2) {
                tileMap.setPosistion(tileMap.getX() - 1, tileMap.getY());
            } else if (tileMap.getX() < window.getWidth() - tileMap.getWidth() / 2) {
                tileMap.setPosistion(tileMap.getX() + 1, tileMap.getY());
            }

            Point carrierDest = new Point(310, 600);
            double radsToCarrierDest = atan2(carrierDest.x - carrier.getX(), carrierDest.y - carrier.getY());
            if ((carrierDest.x - carrier.getX()) * (carrierDest.x - carrier.getX()) + (carrierDest.y - carrier.getY()) * (carrierDest.y - carrier.getY()) > 200) {

                carrier.setHorizontalMovement((float) (sin(radsToCarrierDest) * 100));
                carrier.setVerticalMovement((float) (cos(radsToCarrierDest) * 100));
            } else if ((carrierDest.x - carrier.getX()) * (carrierDest.x - carrier.getX()) + (carrierDest.y - carrier.getY()) * (carrierDest.y - carrier.getY()) > 10) {

                carrier.setHorizontalMovement((float) (sin(radsToCarrierDest) * 50));
                carrier.setVerticalMovement((float) (cos(radsToCarrierDest) * 50));
            } else {
                carrier.setHorizontalMovement(0);
                carrier.setVerticalMovement(0);
                carrier.setX(carrierDest.x);
                carrier.setY(carrierDest.y);
            }

            Point dest = new Point(300, 500);
            for (Entity player : getPlayers()) {
                if ((dest.x - player.getX()) * (dest.x - player.getX()) + (dest.y - player.getY()) * (dest.y - player.getY()) > 200) {
                    double radsToDest = atan2(dest.x - player.getX(), dest.y - player.getY());
                    player.setHorizontalMovement((float) (sin(radsToDest) * 30));
                    player.setVerticalMovement((float) (cos(radsToDest) * 30));
                } else if ((dest.x - player.getX()) * (dest.x - player.getX()) + (dest.y - player.getY()) * (dest.y - player.getY()) > 10) {
                    double radsToDest = atan2(dest.x - player.getX(), dest.y - player.getY());
                    player.setHorizontalMovement((float) (sin(radsToDest) * 10));
                    player.setVerticalMovement((float) (cos(radsToDest) * 10));
                } else {
                    player.setHorizontalMovement(0);
                    player.setVerticalMovement(0);
                    player.setX(dest.x);
                    player.setY(dest.y);
                    if (carrier.getX() == carrierDest.x && carrier.getY() == carrierDest.y) {
                        ((PlayerEntity) player).setThrottle(PlayerEntity.THROTTLE_OFF);
                        events[7] = false;
                    }
                }

            }

        }
        if (levelEnd && SystemTimer.getTime() > eventTimer) {
            submitScore.start();
            gsm.setState(GameStateManager.MENUSTATE);
        }

        checkCollisoins();

    }

    /**
     * render image buffer (bottom later first, top layer last)
     */
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

        //draw player score
        GameStateManager.armaYel.draw(String.valueOf(Score.getScore()), 5, 18);

        //debugging
//        debugText.draw("AverageFPS: " + window.getFPS(), 15, 540);
//        debugText.draw("Enemy Entity Count: " + getEnemyEntities().size(), 15, 555);
//        debugText.draw("Player Entity Count: " + getPlayerEntities().size(), 15, 570);
//        debugText.draw("MapPos: x=" + tileMap.getX() + " y=" + tileMap.getY(), 15, 585);

        if (levelEnd && SystemTimer.getTime() < eventTimer) {
            ScoreBoard.draw("Kills: " + Score.getKills(), (window.getWidth() - ScoreBoard.getStringWidth("Kills: " + Score.getKills())) / 2, 220);
            ScoreBoard.draw("PowerUps: " + Score.getPowerUps(), (window.getWidth() - ScoreBoard.getStringWidth("PowerUps: " + Score.getPowerUps())) / 2, 260);
            ScoreBoard.draw("Deaths: " + Score.getDeaths(), (window.getWidth() - ScoreBoard.getStringWidth("Deaths: " + Score.getDeaths())) / 2, 300);
            ScoreBoard.draw("Score: " + Score.getScore(), (window.getWidth() - ScoreBoard.getStringWidth("Score: " + Score.getScore())) / 2, 340);
        }
    }

    //keypress functions
    //@param keyevent keycode
    @Override
    public void keyPressed(int k) {

        if (playerControl) {
            if (k == KeyEvent.VK_A) {
                left = true;
            }
            if (k == KeyEvent.VK_D) {
                right = true;
            }
            if (k == KeyEvent.VK_W) {
                up = true;
            }
            if (k == KeyEvent.VK_S) {
                down = true;
            }
            if (k == KeyEvent.VK_SPACE) {
                trigger = true;
            }
        }
    }

    //keyrelease functions
    //@param keyevent keycode
    @Override
    public void keyReleased(int k) {
        if (k == KeyEvent.VK_A) {
            left = false;
        }
        if (k == KeyEvent.VK_D) {
            right = false;
        }
        if (k == KeyEvent.VK_W) {
            up = false;
        }
        if (k == KeyEvent.VK_S) {
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

        // enemy -> player bullet collisions
        for (int p = 0; p < this.getPlayerEntities().size(); p++) {
            Entity pe = (Entity) this.getPlayerEntities().get(p);
            for (int s = 0; s < this.getEnemyEntities().size(); s++) {

                Entity ee = (Entity) this.getEnemyEntities().get(s);

                if (ee.collidesWith(pe)) {
                    pe.collidedWith(ee);
                    ee.collidedWith(pe);
                }
            }
        }

        for (int p = 0; p < this.getPlayers().size(); p++) {

            Entity pe = (Entity) this.getPlayers().get(p);
            //enemy -> player collisions
            for (int e = 0; e < this.getEnemyEntities().size(); e++) {
                Entity ee = (Entity) this.getEnemyEntities().get(e);
                if (ee.collidesWith(pe)) {
                    pe.collidedWith(ee);
                    ee.collidedWith(pe);
                }
            }

            //player -> item collisions
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
