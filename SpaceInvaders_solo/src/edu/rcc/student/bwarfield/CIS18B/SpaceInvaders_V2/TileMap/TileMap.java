package edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.TileMap;

import edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.Main.GamePanel;
import edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.Main.SpriteStore;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileMap {

    //position
    private float x;
    private float y;

    //bounds
    private int xmin;
    private int ymin;
    private int xmax;
    private int ymax;

    private float tween;

    //map
    private int[][] map;
    private int tileSize;
    private int numRows;
    private int numCols;
    private int width;
    private int height;

    //tileset
    private BufferedImage tileSet;
    private int numTilesAcross;
    private int numTilesDown;
    private Tile[][] tiles;

    //drawing
    private int rowOffset;
    private int colOffset;
    private int numRowsToDraw;//(rowOffset+(GamePanel.G_HEIGHT/tileSize))
    private int numColsToDraw;

    //class constructor 
    //@param tileSize size on individual map tiles in px
    public TileMap(int tileSize) {
        this.tileSize = tileSize;
        numRowsToDraw = GamePanel.G_HEIGHT / tileSize + 2;
        numColsToDraw = GamePanel.G_WIDTH / tileSize + 2;
//        tween = (float) 0.07;
    }

    //load tileset image for tilemap
    //@param ref path to tileset resource file
    public void loadTiles(String ref) {

        //get image from spritesstore singlet
        tileSet = (BufferedImage) SpriteStore.get().getSprite(ref).getImage();
        //calculate number of tiles in sileset
        numTilesAcross = tileSet.getWidth() / tileSize;
        numTilesDown = tileSet.getHeight() / tileSize;

        //create array index for each tile
        tiles = new Tile[numTilesDown][numTilesAcross];

        //split tileset image into individual tiles
        BufferedImage subImage;
        for (int col = 0; col < numTilesAcross; col++) {//for each col in tileset
            for (int row = 0; row < numTilesDown; row++) {//for each row in tileset
                subImage = tileSet.getSubimage(col * tileSize, row * tileSize, tileSize, tileSize);//get subimage
                tiles[row][col] = new Tile(subImage, Tile.NORMAL);//assign to tile with normal type
            }

        }

    }

    //load map file
    //@param ref path to map file
    public void loadMap(String ref) {
        try {
            InputStream in = getClass().getResourceAsStream(ref);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(in)
            );//read in map file

            numCols = Integer.parseInt(br.readLine());//first line reads number of cols in mapfile
            numRows = Integer.parseInt(br.readLine());//second line reads number of rows in mapfile
            map = new int[numRows][numCols];//create array for mapfile
            width = numCols * tileSize;
            height = numRows * tileSize;

            //set bounds
            xmin = -width + GamePanel.G_WIDTH;
            xmax = 0;
            ymin = -height + GamePanel.G_HEIGHT;
            ymax = 0;

            String delim = "\\s+";//regex for whitespace

            for (int rows = 0; rows < numRows; rows++) {

                String line = br.readLine();//read row of map information
                String[] tokens = line.split(delim);//split line for individual map tiles
                for (int cols = 0; cols < numCols; cols++) {
                    map[rows][cols] = Integer.parseInt(tokens[cols]);//assign value to 2d array
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //getter methods
    //@return horizontal map position
    public int getX() {
        return (int) x;
    }

    //@return vertical map position
    public int getY() {
        return (int) y;
    }

    //@return map tileSize in pixels
    public int getTileSize() {
        return tileSize;
    }

    //@return map width in pixels
    public int getWidth() {
        return width;
    }

    //@return map height in pixels
    public int getHeight() {
        return height;
    }

    //@return number a rows in map
    public int getNumRows() {
        return numRows;
    }

    //@return number of cols in map
    public int getNumCols() {
        return numCols;
    }
    
    //@return tile time (unused in this game at the moment. Used for collision with map objects)
    public int getType(int row, int col) {
        int rc = map[row][col];
        int r = rc / numTilesAcross;
        int c = rc % numTilesAcross;
        return tiles[r][c].getType();
    }

    //set position of map
    //there will generally be negative values to reach the parts of the map that are not on the screen
    //@param x position
    //@param y position
    public void setPosistion(float x, float y) {
        //set map coordinates
        this.x = x;
        this.y = y;
        
        //check against boundaries
        fixBounds();

        //set drawing offset
        colOffset = (int) -this.x / tileSize;
        rowOffset = (int) -this.y / tileSize;
    }

    //check map position against boundaries to prevent map from sliding off the screen
    private void fixBounds() {
        if (x < xmin) {
            x = xmin;
        }
        if (x > xmax) {
            x = xmax;
        }
        if (y < ymin) {
            y = ymin;
        }
        if (y > ymax) {
            y = ymax;
        }
    }

    //draw map to graphics context
    //@param graphics context
    public void draw(Graphics2D g) {
        for (int row = rowOffset; row < rowOffset + numRowsToDraw; row++) {//for each row
            if (row >= numRows) {//bound check
                break;
            }
            for (int col = colOffset; col < colOffset + numColsToDraw; col++) {//for each col
                if (col >= numCols) {//bound checkk
                    break;
                }

                if (map[row][col] == 0) {//0 value is blank so don't draw it
                    continue;
                }

                int rc = map[row][col];//get map time value
                //parse numbers to row/col positions in Tiles instance
                int r = rc / numTilesAcross;
                int c = rc % numTilesAcross;
                g.drawImage(
                        tiles[r][c].getImage(), (int) x + col * tileSize, (int) y + row * tileSize, null
                );//draw each tile in appropriate position

            }
        }
    }
}
