
package edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.TileMap;

import java.awt.image.BufferedImage;

public class Tile {
    private BufferedImage image;
    private int type;
    
    //Tile Types
    public final static int NORMAL = 0;
    public final static int BLOCKED = 1;
    
    //constructor method
    //@param tile image
    //@param tile tyle
    public Tile(BufferedImage image, int type){
        this.image = image;
        this.type = type;
    }
    
    //getter method
    //@return tile image
    public BufferedImage getImage() {
        return image;
    }

    //@return tile type
    public int getType() {
        return type;
    }
     
}
