package SpaceInvaders_V4.Util;

import SpaceInvaders_V4.Main.ResourceFactory;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

//this class manages a bitmap font built with an image grid of 16 x 16 with 
//ascii values ranging from 0-255
//optional metrics files are 16 lines of 8 groups of 4digit hex values delimted 
//by spaces, each 2digit hex equalling char width in pixels
//bitmap and metric files generateg using F2Ibuilder http://f2ibuilder.dukitan.com/  
//Note: F2I metrics files need to be converted to UTF-8 encoding before use 
public class Font {

    //spriteMap for this font
    private Sprite spriteMap;
    //size of the tiles in given image
    private int tileWidth;
    private int tileHeight;
    //array of font tiles
    private Sprite[][] fontMap;
    private int[] metrics;

    //constructor method
    //@param ref reference to spriteMap image file
    public Font(String ref) {
        this.spriteMap = ResourceFactory.get().getSprite(ref);

        //calsulate tilesize (assuming spritemap is (16x16) square tiles
        tileWidth = spriteMap.getWidth() / 16;
        tileHeight = spriteMap.getHeight() / 16;

        //split spriteMap into individual tiles
        fontMap = new Sprite[16][16];

        //loop through image grid, saving each cell to fontMap array
        for (int col = 0; col < 16; col++) {
            for (int row = 0; row < 16; row++) {
                fontMap[row][col] = ResourceFactory.get().getSubSprite(
                        spriteMap.getRef(),
                        col * tileWidth,
                        row * tileHeight,
                        tileWidth,
                        tileHeight
                );
            }
        }
    }

    //constructor method
    //@param ref reference to spriteMap image file
    //@param met reference path to metrics dat file 
    public Font(String ref, String met) {
        this.spriteMap = ResourceFactory.get().getSprite(ref);
        metrics = new int[256];

        //read in metrics file
        try {
            InputStream in = getClass().getResourceAsStream(met);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(in)
            );//read in metrics file

            String delim = "\\s+";//regex for whitespace
            for (int i = 0; i < 16; i++) {
                String line = br.readLine();//read row

                String[] tokens = line.split(delim);//split line 
                for (int j = 0; j < 8; j++) {

                    metrics[(2 * j) + (i * 16)] = Integer.parseInt(tokens[j].substring(0, 2), 16);//parse char width
                    metrics[(2 * j) + (i * 16) + 1] = Integer.parseInt(tokens[j].substring(2, 4), 16);//parse char width
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //calculate tilesize (assuming spritemap is (16x16) square tiles
        tileWidth = spriteMap.getWidth() / 16;
        tileHeight = spriteMap.getHeight() / 16;

        //split spriteMap into individual tiles
        fontMap = new Sprite[16][16];

        //loop through image grid, saving each cell to fontMap array
        for (int col = 0; col < 16; col++) {
            for (int row = 0; row < 16; row++) {
                fontMap[row][col] = ResourceFactory.get().getSubSprite(
                        spriteMap.getRef(),
                        col * tileWidth,
                        row * tileHeight,
                        tileWidth,
                        tileHeight
                );
            }
        }
    }

    //get the pixel width of given string
    //@param text string
    public int getStringWidth(String text) {
        if (metrics == null) {
            return text.length() * tileWidth;
        } else {
            int len = 0;
            for (int i = 0; i < text.length(); i++) {
                len += metrics[(int) text.charAt(i)];
            }
            return len;
        }
    }

    //get the pixel height for this font
    public int getHeight() {
        return tileHeight;
    }

    public void draw(String txt, int x, int y) {
        int len = 0;

        for (int i = 0; i < txt.length(); i++) {
            int c = txt.charAt(i);//convert char to ascii value

            //skip out of range chars
            if (c < 0 || c > 255) {
                continue;
            }
            //draw char
            if (metrics == null) {
                fontMap[(int) c / 16][(int) c % 16].draw(x + (i * tileWidth), y);
            } else {
                fontMap[(int) c / 16][(int) c % 16].draw(x + (len), y);
                len += metrics[c];
            }

        }
    }

}
