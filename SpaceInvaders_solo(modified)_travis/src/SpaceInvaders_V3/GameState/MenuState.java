package SpaceInvaders_V3.GameState;

import SpaceInvaders_V3.Main.Game;
import SpaceInvaders_V3.Main.GameWindow;
import SpaceInvaders_V3.Util.Sprite;
import SpaceInvaders_V3.Main.ResourceFactory;
import SpaceInvaders_V3.TileMap.Background;
import SpaceInvaders_V3.Util.Font;
import SpaceInvaders_V3.formwindow.Registration;
import java.awt.event.KeyEvent;

public class MenuState extends GameState {

    //game window
    GameWindow window = ResourceFactory.get().getGameWindow();

    //background image class
    private Background bg;

    //menu selection
    private int currentChoice = 0;
    private String[] option = {
        "Start",
        "Log In",
        "High Scores",
        "Options",
        "Quit"
    };

    //bitmap fonts for menu
    private Font grayFont = new Font("resources/tilesets/AgencyGray34.png", "/resources/tilesets/Agency34.dat");
    private Font redFont = new Font("resources/tilesets/AgencyRed34.png", "/resources/tilesets/Agency34.dat");

    //title image
    Sprite logo = ResourceFactory.get().getSprite("resources/title/title.png");//use sprite store

    //constructor method
    //@param GameStateManager
    public MenuState(GameStateManager gsm) {
        this.gsm = gsm;
        //
        try {
            bg = new Background("resources/backgrounds/Space.gif", 1);//set tiles background image 
            bg.setVector((float) -30, 10);//set movement vector

        } catch (Exception e) {
            e.printStackTrace();
        }
        init();
    }

    //initialize gamestate
    @Override
    public void init() {

    }

    //update gamestate
    //@param delta timing vector
    @Override
    public void gameUpdate(double delta) {

        bg.gameUpdate(delta);//update background position

    }

    //render inamge buffer (bottom later first, top layer last)
    //@param game graphics context
    @Override
    public void gameRender() {
        //background
        bg.gameRender();

        //draw title
        logo.draw((window.getWidth() - logo.getWidth()) / 2, 75);

        //draw menu options
        for (int i = 0; i < option.length; i++) {
            String s = option[i];
            Font selectedFont;
            if (i == currentChoice) {//hilight current choice
//                g.fillRoundRect((ResourceFactory.get().getGameWindow().getWidth() - 225) / 2, 338 + i * 30, 225, 30, 12, 12);
                selectedFont = redFont;
            } else {
                selectedFont = grayFont;
            }
            selectedFont.draw(s, (window.getWidth() - grayFont.getStringWidth(s)) / 2, 340 + i * 45);
        }

    }

    //evoke selection
    private void select() {
        if (currentChoice == 0) {
            //start
            gsm.setState(GameStateManager.LEVEL1STATE);
        }
        if (currentChoice == 1) {
            Registration reg = new Registration();
            reg.setVisible(true);
           // Game.window.setVisible(true);
            //Scores
        }
        if (currentChoice == 2) {
            //options
        }
        if (currentChoice == 3) {
            //options
        }
        if (currentChoice == 4) {
            //Quit
            System.exit(0);
        }
    }

    //keypress functions
    //@param keyevent keycode
    @Override
    public void keyPressed(int k) {
        if (k == KeyEvent.VK_ENTER) {
            select();
        }
        if (k == KeyEvent.VK_UP) {
            currentChoice--;

            if (currentChoice < 0) {
                currentChoice = option.length - 1;
            }
        }
        if (k == KeyEvent.VK_DOWN) {
            currentChoice++;
            if (currentChoice > option.length - 1) {
                currentChoice = 0;
            }
        }

    }

    //keyrelease functions
    //@param keyevent keycode
    @Override
    public void keyReleased(int k) {

    }

    @Override
    public void keyTyped(int k) {

    }
}
