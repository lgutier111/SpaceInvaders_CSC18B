package edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.Main;

import java.awt.Graphics;
import java.awt.Image;

//Sprite class to hold image information which can be instantiated
//multiple times without the need to load multiple images 
public class Sprite {
    //sprite image
    private Image image;

    //returns sprite image
    //@return Image sprite image
    public Image getImage() {
        return image;
    }
    
    //create sprite based on image
    //@param image the image that is this sprite
    public Sprite(Image image){
        this.image = image;
    }
    
    //get width of the drawn sprite
    //@return int width in pixels of the sprite
    public int getWidth(){
        return image.getWidth(null);
    }
    
    //get the height of the drawn sprite
    //@return int height in pixels
    public int getHeight(){
        return image.getHeight(null);
    }
    
    //draw the sprite onto the graphics context provided
    //@param g The graphics context on which to draw the sprite
    //@param x horizontal coodinate
    //@param y vertical coordinate
    public void draw(Graphics g, int x, int y){
        g.drawImage(image,x,y,null);
    }
}