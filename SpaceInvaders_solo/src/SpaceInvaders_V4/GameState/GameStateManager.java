package SpaceInvaders_V4.GameState;

import java.util.ArrayList;

public class GameStateManager {

    //array list of possible gamestates
    private ArrayList<GameState> gameStates;
    //index of current state
    private int currentState;

    //Gamestate index constants
    public static final int MENUSTATE = 0;
    public static final int LEVEL1STATE = 1;

    //constructor method
    public GameStateManager() {
        gameStates = new ArrayList<GameState>();
        currentState = MENUSTATE;
        //add gamestates to arrayList in orcer of index constant
        gameStates.add(new MenuState(this));
        gameStates.add(new Level1State(this));

    }

    //change gamestate
    //@param index of gamestate in array list (use class constants)
    public void setState(int state) {
        currentState = state;//set current state
        gameStates.get(currentState).init();//initiallize new state
    }

    //get gameState array List
    //@return gamestate array 
    public ArrayList<GameState> getGameStates() {
        return gameStates;
    }

    
    
    //pass update call to current gamestate
    //@param delta timing vector
    public void gameUpdate(double delta) {
        gameStates.get(currentState).gameUpdate(delta);
    }

    //pass render call to current gamestate
    //@param game graphics context
    public void gameRender() {
        gameStates.get(currentState).gameRender();
    }

    //pass keypress to curent gamestate
    //@param keypress keycode
    public void keyPressed(int k) {
        gameStates.get(currentState).keyPressed(k);
    }

    //pass keyrelease to curent gamestate
    //@param keyrelease keycode
    public void keyReleased(int k) {
        gameStates.get(currentState).keyReleased(k);
    }

    //pass keytyped to curent gamestate
    //@param keytyped keycode
    public void keyTyped(int k) {
        gameStates.get(currentState).keyTyped(k);
    }
}
