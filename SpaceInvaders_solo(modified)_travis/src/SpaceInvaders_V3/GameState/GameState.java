package SpaceInvaders_V3.GameState;

import SpaceInvaders_V3.Entity.Entity;
import java.util.ArrayList;

//abstract class for gamestates
public abstract class GameState {
    
    protected GameStateManager gsm;

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

    public ArrayList getPlayerEntities() {
        return playerEntities;
    }
    public ArrayList getEnemyEntities() {
        return enemyEntities;
    }
    public ArrayList getPlayers(){
        return players;
    }
    
    
    //The list of entities that need to be removed from the game this loop
    private final ArrayList removeEnemyList = new ArrayList();
    private final ArrayList removePlayerList = new ArrayList();
    private final ArrayList removePlayers = new ArrayList();

    public ArrayList getRemoveEnemyList() {
        return removeEnemyList;
    }
    public ArrayList getRemovePlayerList() {
        return removePlayerList;
    }
    public ArrayList getRomovePlayers(){
        return removePlayers;
    }

}
