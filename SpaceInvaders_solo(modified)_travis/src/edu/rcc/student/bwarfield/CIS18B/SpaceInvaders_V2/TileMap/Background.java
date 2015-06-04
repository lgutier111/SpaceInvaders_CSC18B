package edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.TileMap;

import edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.Main.GamePanel;
import edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.Main.Sprite;
import edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.Main.SpriteStore;
import java.awt.Graphics2D;

public class Background {

    private Sprite BGImage;
    
    //image position
    private float x;
    private float y;
    //image velocity
    private float dx;
    private float dy;
    //movement scale
    private float moveScale;

    //constructor methos
    //@param ref path to background image
    //@ ms scale of movement for parallax effect
    public Background(String ref, float ms) {
        BGImage = SpriteStore.get().getSprite(ref);//use sprite store to prevent redundant image loads
        moveScale = ms;
    }

    //set position of background image, mod by image dimentions as bounds check
    //@param x horizontal position
    //@param y vertical positioin
    public void setPosition(float x, float y) {
        this.x = (x * moveScale) % BGImage.getWidth();
        this.y = (y * moveScale) % BGImage.getHeight();
    }
    
    //set movement speed of background
    //@param vertical movement speed in px/frame
    //@param horizontal movement in px/frame
    public void setVector(float dx, float dy) {
        this.dx = dx;
        this.dy = dy;
    }

    //calculate and apply movement
    //@param delta timing vector
    public void gameUpdate(long delta) {
        x += (dx * delta) / 100;
        y += (dy * delta) / 100;
        //bounds check
        x = x % BGImage.getWidth();
        y = y % BGImage.getHeight();
//        System.out.println("BG x:" + x + " ,y:" + y);
    }
    
    //draw image to graphics context
    //@param graphics context
    public void gameRender(Graphics2D g) {
        g.drawImage(BGImage.getImage(), (int) x, (int) y, null);
        
        //repeat image if it extents beyond bounds
        if (x + BGImage.getWidth() < GamePanel.G_WIDTH) {
            g.drawImage(BGImage.getImage(), (int) x + BGImage.getWidth(), (int) y, null);
            if (y + BGImage.getHeight() < GamePanel.G_HEIGHT) {
                g.drawImage(BGImage.getImage(), (int) x + BGImage.getWidth(), (int) y + BGImage.getHeight(), null);
            }
            if (y > 0) {
                g.drawImage(BGImage.getImage(), (int) x + BGImage.getWidth(), (int) y - BGImage.getHeight(), null);
            }
        }
        if (x > 0) {
            g.drawImage(BGImage.getImage(), (int) x - BGImage.getWidth(), (int) y, null);
            if (y + BGImage.getHeight() < GamePanel.G_HEIGHT) {
                g.drawImage(BGImage.getImage(), (int) x - BGImage.getWidth(), (int) y + BGImage.getHeight(), null);
            }
            if (y > 0) {
                g.drawImage(BGImage.getImage(), (int) x - BGImage.getWidth(), (int) y - BGImage.getHeight(), null);
            }
        }
        if (y + BGImage.getHeight() < GamePanel.G_HEIGHT) {
            g.drawImage(BGImage.getImage(), (int) x, (int) y + BGImage.getHeight(), null);
            if (x + BGImage.getWidth() < GamePanel.G_WIDTH) {
                g.drawImage(BGImage.getImage(), (int) x + BGImage.getWidth(), (int) y + BGImage.getHeight(), null);
            }
            if (x > 0) {
                g.drawImage(BGImage.getImage(), (int) x - BGImage.getWidth(), (int) y + BGImage.getHeight(), null);
            }
        }
        if (y > 0) {
            g.drawImage(BGImage.getImage(), (int) x, (int) y - BGImage.getHeight(), null);
            if (x + BGImage.getWidth() < GamePanel.G_WIDTH) {
                g.drawImage(BGImage.getImage(), (int) x + BGImage.getWidth(), (int) y - BGImage.getHeight(), null);
            }
            if (x > 0) {
                g.drawImage(BGImage.getImage(), (int) x - BGImage.getWidth(), (int) y - BGImage.getHeight(), null);
            }
        }

    }
}
