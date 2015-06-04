package SpaceInvaders_V4.GameState;

import SpaceInvaders_V4.Entity.Entity;
import SpaceInvaders_V4.Util.SystemTimer;
import java.util.ArrayList;

//abstract class for gamestates
public abstract class GameState {

    protected GameStateManager gsm;
    protected boolean playerIsAlive;
    protected double respawnTimer;
    protected boolean playerControl;
    protected double controlTimer;
    
    protected boolean up, down, left, right, trigger;

    public abstract void init();

    public abstract void gameUpdate(double delta);

    public abstract void gameRender();

    public abstract void keyPressed(int k);

    public abstract void keyReleased(int k);

    public abstract void keyTyped(int k);

    // The list of all the entities
    private final ArrayList<Entity> playerEntities = new ArrayList();
    private final ArrayList<Entity> enemyEntities = new ArrayList();
    private final ArrayList<Entity> players = new ArrayList();
    private final ArrayList<Entity> items = new ArrayList();
    private final ArrayList<Entity> effects = new ArrayList();

    public ArrayList<Entity> getPlayerEntities() {
        return playerEntities;
    }

    public ArrayList<Entity> getEnemyEntities() {
        return enemyEntities;
    }

    public ArrayList<Entity> getPlayers() {
        return players;
    }

    public ArrayList<Entity> getItems() {
        return items;
    }

    public ArrayList<Entity> getEffects() {
        return effects;
    }

    //The list of entities that need to be removed from the game this loop
    private final ArrayList removeEnemyList = new ArrayList();
    private final ArrayList removePlayerList = new ArrayList();
    private final ArrayList removePlayers = new ArrayList();
    private final ArrayList removeItems = new ArrayList();
    private final ArrayList removeEffects = new ArrayList();

    public ArrayList getRemoveEnemyList() {
        return removeEnemyList;
    }

    public ArrayList getRemovePlayerList() {
        return removePlayerList;
    }

    public ArrayList getRemovePlayers() {
        return removePlayers;
    }

    public ArrayList getRemoveItems() {
        return removeItems;
    }

    public ArrayList getRemoveEffects() {
        return removeEffects;
    }

    public void notifyPlayerDeath(){
       playerControl = false;
       playerIsAlive = false;
       respawnTimer = SystemTimer.getTime()+2;
       controlTimer = SystemTimer.getTime()+4;
       
    }
    
    public boolean[] getKeys(){
        boolean[] keys ={up, down, left, right, trigger};
        return keys;
    }
    
    public boolean getPlayerControl(){
        return playerControl;
    }

}
