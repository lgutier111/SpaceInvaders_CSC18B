package edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.GameState;

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
    private ArrayList entities = new ArrayList();

    public ArrayList getEntities() {
        return entities;
    }
    //The list of entities that need to be removed from the game this loop
    private ArrayList removeList = new ArrayList();

    public ArrayList getRemoveList() {
        return removeList;
    }

}
