package SpaceInvaders_V4.GameState;

import SpaceInvaders_V4.Entity.Entity;
import SpaceInvaders_V4.Util.SystemTimer;
import java.util.ArrayList;

//abstract class for gamestates

/**
 *
 * @author Bee-Jay
 */
public abstract class GameState {

    /**
     *
     */
    protected GameStateManager gsm;

    /**
     *
     */
    protected boolean playerIsAlive;

    /**
     *
     */
    protected double respawnTimer;

    /**
     *
     */
    protected boolean playerControl;

    /**
     *
     */
    protected double controlTimer;
    
    protected boolean up,

    /**
     *
     */
    down,

    /**
     *
     */
    left,

    /**
     *
     */
    right,

    /**
     *
     */
    trigger;

    /**
     *
     */
    public abstract void init();

    /**
     *
     * @param delta
     */
    public abstract void gameUpdate(double delta);

    /**
     *
     */
    public abstract void gameRender();

    /**
     *
     * @param k
     */
    public abstract void keyPressed(int k);

    /**
     *
     * @param k
     */
    public abstract void keyReleased(int k);

    /**
     *
     * @param k
     */
    public abstract void keyTyped(int k);

    // The list of all the entities
    private final ArrayList<Entity> playerEntities = new ArrayList();
    private final ArrayList<Entity> enemyEntities = new ArrayList();
    private final ArrayList<Entity> players = new ArrayList();
    private final ArrayList<Entity> items = new ArrayList();
    private final ArrayList<Entity> effects = new ArrayList();

    /**
     *
     * @return
     */
    public ArrayList<Entity> getPlayerEntities() {
        return playerEntities;
    }

    /**
     *
     * @return
     */
    public ArrayList<Entity> getEnemyEntities() {
        return enemyEntities;
    }

    /**
     *
     * @return
     */
    public ArrayList<Entity> getPlayers() {
        return players;
    }

    /**
     *
     * @return
     */
    public ArrayList<Entity> getItems() {
        return items;
    }

    /**
     *
     * @return
     */
    public ArrayList<Entity> getEffects() {
        return effects;
    }

    //The list of entities that need to be removed from the game this loop
    private final ArrayList removeEnemyList = new ArrayList();
    private final ArrayList removePlayerList = new ArrayList();
    private final ArrayList removePlayers = new ArrayList();
    private final ArrayList removeItems = new ArrayList();
    private final ArrayList removeEffects = new ArrayList();

    /**
     *
     * @return
     */
    public ArrayList getRemoveEnemyList() {
        return removeEnemyList;
    }

    /**
     *
     * @return
     */
    public ArrayList getRemovePlayerList() {
        return removePlayerList;
    }

    /**
     *
     * @return
     */
    public ArrayList getRemovePlayers() {
        return removePlayers;
    }

    /**
     *
     * @return
     */
    public ArrayList getRemoveItems() {
        return removeItems;
    }

    /**
     *
     * @return
     */
    public ArrayList getRemoveEffects() {
        return removeEffects;
    }

    /**
     *
     */
    public void notifyPlayerDeath(){
       playerControl = false;
       playerIsAlive = false;
       respawnTimer = SystemTimer.getTime()+2;
       controlTimer = SystemTimer.getTime()+4;
       
    }
    
    /**
     *
     * @return
     */
    public boolean[] getKeys(){
        boolean[] keys ={up, down, left, right, trigger};
        return keys;
    }
    
    /**
     *
     * @return
     */
    public boolean getPlayerControl(){
        return playerControl;
    }

}
