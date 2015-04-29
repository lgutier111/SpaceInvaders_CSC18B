package edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.GameState;

import edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.Entity.Entity;
import java.awt.Graphics2D;
import java.util.ArrayList;

//abstract class for gamestates
public abstract class GameState {

    protected GameStateManager gsm;

    public abstract void init();

    public abstract void gameUpdate(long delta);

    public abstract void gameRender(Graphics2D g);

    public abstract void keyPressed(int k);

    public abstract void keyReleased(int k);

    public abstract void keyTyped(int k);

    // The list of all the entities
    private ArrayList<Entity> playerEntities = new ArrayList();
    private ArrayList<Entity> enemyEntities = new ArrayList();

    public ArrayList getPlayerEntities() {
        return playerEntities;
    }
    public ArrayList getEnemyEntities() {
        return enemyEntities;
    }
    
    //The list of entities that need to be removed from the game this loop
    private ArrayList removeEnemyList = new ArrayList();
    private ArrayList removePlayerList = new ArrayList();

    public ArrayList getRemoveEnemyList() {
        return removeEnemyList;
    }
    public ArrayList getRemovePlayerList() {
        return removePlayerList;
    }

}
