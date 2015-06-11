package SpaceInvaders_V4.GameState;

import SpaceInvaders_V4.LevelEditor.EntityListEditor;
import SpaceInvaders_V4.Main.GameWindow;
import SpaceInvaders_V4.Util.Sprite;
import SpaceInvaders_V4.Main.ResourceFactory;
import SpaceInvaders_V4.TileMap.Background;
import SpaceInvaders_V4.Users.Score;
import SpaceInvaders_V4.Users.UserControlPanel;
import SpaceInvaders_V4.Util.Font;
import java.awt.event.KeyEvent;

public class MenuState extends GameState {

    //game window
    GameWindow window = ResourceFactory.get().getGameWindow();

    //background image class
    private Background bg;

    private UserControlPanel ucp = null;
    private EntityListEditor ele = null;

    //menu selection
    private int currentChoice = 0;
    private String[] option = {
        "Start",
        "User Control Panel",
        "Level Editor",
        "Quit"
    };

    //bitmap fonts for menu
    private Font grayFont = new Font("resources/tilesets/AgencyGray34.png", "/resources/tilesets/Agency34.dat");
    private Font redFont = new Font("resources/tilesets/AgencyRed34.png", "/resources/tilesets/Agency34.dat");

    //title image
    Sprite logo = ResourceFactory.get().getSprite("resources/title/title.png");//use sprite store

    /**
     * constructor method 
     * @param gsm current GameStateMabager
     */
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

    /**
     * initialize gamestate
     */
    @Override
    public void init() {

    }

    /**
     * update gameState
     *
     * @param delta timing vector
     */
    @Override
    public void gameUpdate(double delta) {

        bg.gameUpdate(delta);//update background position

    }

    /**
     * render image buffer (bottom later first, top layer last)
     */
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

    /**
     * evoke menu selection
     */
    private void select() {
        if (currentChoice == 0) {
            //start
            gsm.setState(GameStateManager.LEVEL1STATE);
            Score.reset();
        }
        if (currentChoice == 1) {
            //user login
            if (ucp == null) {
                ucp = new UserControlPanel();
            }
            ResourceFactory.get().getGameWindow().setVisable(false);
            ucp.setVisible(true);
            ucp.requestFocus();
        }
        if (currentChoice == 2) {
            //level editor
            if (ele == null) {
                ele = new EntityListEditor();
            }
            ele.setVisible(true);
            ele.requestFocus();
        }
        if (currentChoice == 3) {
            //Quit
            System.exit(0);
        }
    }

    /*keypress functions
     *@param keyevent keycode
     */
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

    /**
     * keyRelease functions
     *
     * @param k keyCode
     */
    @Override
    public void keyReleased(int k) {

    }

    /**
     * KeyTypes Function
     *
     * @param k keyCode
     */
    @Override
    public void keyTyped(int k) {

    }
}
