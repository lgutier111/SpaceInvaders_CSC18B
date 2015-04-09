package edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.GameState;

import edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.Main.GamePanel;
import edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.Main.Sprite;
import edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.Main.SpriteStore;
import edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.TileMap.Background;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class MenuState extends GameState {

    //background image class
    private Background bg;

    //menu selection
    private int currentChoice = 0;
    private String[] option = {
        "Start",
        "High Scores",
        "Options",
        "Quit"
    };

    //title image
    Sprite logo = SpriteStore.get().getSprite("resources/title/title.png");//use sprite store singlet

    //constructor method
    //@param GameStateManager
    public MenuState(GameStateManager gsm) {
        this.gsm = gsm;
        //
        try {
            bg = new Background("resources/backgrounds/Space.gif", 1);//set tiles background image 
            bg.setVector((float) -4, 1);//set movement vector

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //initialize gamestate
    @Override
    public void init() {

    }

    //update gamestate
    //@param delta timing vector
    @Override
    public void gameUpdate(long delta) {
        bg.gameUpdate(delta);//update background position 
    }

    //render inamge buffer (bottom later first, top layer last)
    //@param game graphics context
    @Override
    public void gameRender(Graphics2D g) {
        //background
        bg.gameRender(g);

        //draw title
        logo.draw(g, (GamePanel.G_WIDTH - logo.getWidth()) / 2, 75);

        g.setFont(new Font("Arial Black", Font.PLAIN, 24));
        //draw menu options
        for (int i = 0; i < option.length; i++) {
            if (i == currentChoice) {//hilight current choice
                g.setColor(Color.WHITE);
                g.fillRoundRect((GamePanel.G_WIDTH - 225) / 2, 338 + i * 30, 225, 30, 12, 12);
                g.setColor(Color.RED);
            } else {
                g.setColor(new Color(175, 175, 175));
            }
            //draw option
            String s = option[i];
            g.drawString(s, (GamePanel.G_WIDTH - g.getFontMetrics().stringWidth(s)) / 2, 360 + i * 30);
        }
        

    }

    //evoke selection
    private void select() {
        if (currentChoice == 0) {
            //start
            gsm.setState(GameStateManager.LEVEL1STATE);
        }
        if (currentChoice == 1) {
            //Scores
        }
        if (currentChoice == 2) {
            //options
        }
        if (currentChoice == 3) {
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
