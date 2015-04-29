package SpaceInvaders_V3.TileMap;

import SpaceInvaders_V3.Main.ResourceFactory;
import SpaceInvaders_V3.Util.Sprite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileMap {

    //position
    private float x;
    private float y;
    private float dx;
    private float dy;

    //bounds
    private int xmin;
    private int ymin;
    private int xmax;
    private int ymax;

    //map
    private int[][] map;
    private int tileSize;
    private int numRows;
    private int numCols;
    private int width;
    private int height;

    //tileset
    private Sprite tileSet;
    private int numTilesAcross;
    private int numTilesDown;
    private Sprite[][] tiles;

    //drawing
    private int rowOffset;
    private int colOffset;
    private int numRowsToDraw;//(rowOffset+(Java2DGameWindow.G_HEIGHT/tileSize))
    private int numColsToDraw;

    //class constructor 
    //@param tileSize size on individual map tiles in px
    public TileMap(int tileSize) {
        this.tileSize = tileSize;
        numRowsToDraw = ResourceFactory.get().getGameWindow().getHeight() / tileSize + 2;
        numColsToDraw = ResourceFactory.get().getGameWindow().getWidth() / tileSize + 2;

    }

    //load tileset image for tilemap
    //@param ref path to tileset resource file
    public void loadTiles(String ref) {

        //get image from spritesstore singlet
        tileSet = ResourceFactory.get().getSprite(ref);
        //calculate number of tiles in sileset
        numTilesAcross = tileSet.getWidth() / tileSize;
        numTilesDown = tileSet.getHeight() / tileSize;

        //create array index for each tile
        tiles = new Sprite[numTilesDown][numTilesAcross];

        //split tileset image into individual tiles
//        BufferedImage subImage;
        for (int col = 0; col < numTilesAcross; col++) {//for each col in tileset
            for (int row = 0; row < numTilesDown; row++) {//for each row in tileset
//                subImage = tileSet.getSubimage(col * tileSize, row * tileSize, tileSize, tileSize);//get subimage
                tiles[row][col] = ResourceFactory.get().getSubSprite(tileSet.getRef(), col * tileSize, row * tileSize, tileSize, tileSize);//assign to tile with normal type
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
            xmin = -width + ResourceFactory.get().getGameWindow().getWidth();
            xmax = 0;
            ymin = -height + ResourceFactory.get().getGameWindow().getHeight();
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

    //move method based on timng system
    //@param delta the amount of time passed in seconds
    public void move(Double delta) {
        if (dx > 0 && x + dx * delta >= xmax) {
            dx = 0;
            x = xmax;
        }
        if (dx < 0 && x + dx * delta <= xmin) {
            dx = 0;
            x = xmin;
        }
        if (dy > 0 && y + dy * delta >= ymax) {
            dy = 0;
            y = ymax;
        }
        if (dy < 0 && y + dy * delta <= ymin) {
            dy = 0;
            y = ymin;
        }

        //update location based on move speed
        x += (delta * dx);//shift horizontally
        y += (delta * dy);//shift vertically
        
        setPosistion(x, y);

    }

    //set horizontal speed
    //@param dx speed in pixels/sec
    public void setHorizontalMovement(float dx) {
        this.dx = dx;
    }

    //set vertical speed
    //@param dy speed in pixels/sec
    public void setVerticalMovement(float dy) {
        this.dy = dy;
    }

    //get Horizontal speed
    //@return speed in pixels/sec
    public float getHorizontalMovement() {
        return dx;
    }

    //get vertical speed
    //@return speed in pixels/sec
    public float getVerticalMovement() {
        return dy;
    }

    //getter methods
    //@return horizontal map position
    public float getX() {
        return x;
    }

    //@return vertical map position
    public float getY() {
        return y;
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
    public void draw() {
        for (int row = rowOffset; row < rowOffset + numRowsToDraw; row++) {//for each row
            if (row >= numRows) {//bound check
                break;
            }
            for (int col = colOffset; col < colOffset + numColsToDraw; col++) {//for each col
                if (col >= numCols) {//bound check
                    break;
                }

                if (map[row][col] == 0) {//0 value is blank so don't draw it
                    continue;
                }

                int rc = map[row][col];//get map time value
                //parse numbers to row/col positions in Tiles instance
                int r = rc / numTilesAcross;
                int c = rc % numTilesAcross;

                tiles[r][c].draw(
                        (int) x + col * tileSize,
                        (int) y + row * tileSize
                );//draw each tile in appropriate position

            }
        }
    }
}
