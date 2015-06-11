package SpaceInvaders_V4.TileMap;

import SpaceInvaders_V4.Main.ResourceFactory;
import SpaceInvaders_V4.Util.Sprite;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * TileMap files were generated using Tiled Map Editor at
 * http://www.mapeditor.org/. This class parses and implements a JSON export of
 * map files
 *
 * @author Bee-Jay
 */
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
    JSONObject json;
    private int[][] map;
    private int xTileSize;
    private int yTileSize;
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

    /**
     * Create new TileMap
     *
     * @param jsonRef reference to tileMap JSON file
     */
    public TileMap(String jsonRef) {
        json = parseJSON(jsonRef);

        this.xTileSize = (int) (long) json.get("tilewidth");
        this.yTileSize = (int) (long) json.get("tileheight");
        numRowsToDraw = ResourceFactory.get().getGameWindow().getHeight() / this.yTileSize + 2;
        numColsToDraw = ResourceFactory.get().getGameWindow().getWidth() / this.xTileSize + 2;

        loadTiles();
        loadMap();

    }

    /**
     * load tileSet image for tileMap
     */
    private void loadTiles() {

        //get image from spritesstore singlet
        tileSet = ResourceFactory.get().getSprite((String) ((JSONObject) ((JSONArray) json.get("tilesets")).get(0)).get("image"));

        //calculate number of tiles in tileset
        numTilesAcross = tileSet.getWidth() / xTileSize;
        numTilesDown = tileSet.getHeight() / yTileSize;

        //create array index for each tile
        tiles = new Sprite[numTilesDown][numTilesAcross];

        //split tileset image into individual tiles
        for (int col = 0; col < numTilesAcross; col++) {//for each col in tileset
            for (int row = 0; row < numTilesDown; row++) {//for each row in tileset
                tiles[row][col] = ResourceFactory.get().getSubSprite(tileSet.getRef(), col * xTileSize, row * yTileSize, xTileSize, yTileSize);//assign to tile with normal type
            }

        }

    }

    /**
     * load map file
     */
    private void loadMap() {
        //get layer object from JSONObject
        JSONObject layer = ((JSONObject) ((JSONArray) json.get("layers")).get(0));
        JSONArray data = (JSONArray) layer.get("data");//tileMap terrain values

        //get map parrameters
        numCols = (int) (long) layer.get("width");//number of cols in map
        numRows = (int) (long) layer.get("height");//number of rows in map
        map = new int[numRows][numCols];//create MapArray for mapfile size
        width = numCols * xTileSize;//mapWidth in px
        height = numRows * yTileSize;//mapHeight in px

        //set bounds
        xmin = -width + ResourceFactory.get().getGameWindow().getWidth();
        xmax = 0;
        ymin = -height + ResourceFactory.get().getGameWindow().getHeight();
        ymax = 0;

        //parse data JSONarray into mapArray
        for (int rows = 0; rows < numRows; rows++) {
            for (int cols = 0; cols < numCols; cols++) {
                map[rows][cols] = (int) (long) data.get((rows * numCols) + (cols));//assign value to 2d array
            }
        }

    }

    /**
     * move method based on timing system
     *
     * @param delta the amount of time passed in seconds
     */
    public void move(Double delta) {
        //bound check
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

    /**
     *
     * @param dx speed in pixels/sec
     */
    public void setHorizontalMovement(float dx) {
        this.dx = dx;
    }

    /**
     * set vertical speed
     *
     * @param dy speed in pixels/sec
     */
    public void setVerticalMovement(float dy) {
        this.dy = dy;
    }

    /**
     * get Horizontal speed
     *
     * @return speed in pixels/sec
     */
    public float getHorizontalMovement() {
        return dx;
    }

    /**
     * get vertical speed
     *
     * @return speed in pixels/sec
     */
    public float getVerticalMovement() {
        return dy;
    }

    /**
     *
     * @return horizontal map position
     */
    public float getX() {
        return x;
    }

    /**
     *
     * @return vertical map position
     */
    public float getY() {
        return y;
    }

    /**
     *
     * @return map tileWidth in pixels
     */
    public int getTileWidth() {
        return xTileSize;
    }

    /**
     *
     * @return map tileHeight in pixels
     */
    public int getTileHeight() {
        return yTileSize;
    }

    /**
     *
     * @return map width in pixels
     */
    public int getWidth() {
        return width;
    }

    /**
     *
     * @return map height in pixels
     */
    public int getHeight() {
        return height;
    }

    /**
     *
     * @return number a rows in map
     */
    public int getNumRows() {
        return numRows;
    }

    /**
     *
     * @return number of cols in map
     */
    public int getNumCols() {
        return numCols;
    }

    /**
     * set position of map. there will generally be negative values to reach the
     * parts of the map that are not on the screen
     *
     * @param x position
     * @param y position
     */
    public void setPosistion(float x, float y) {
        //set map coordinates
        this.x = x;
        this.y = y;

        //check against boundaries
        fixBounds();

        //set drawing offset
        colOffset = (int) -this.x / xTileSize;
        rowOffset = (int) -this.y / yTileSize;
    }

    /**
     * check map position against boundaries to prevent map from sliding off the
     * screen
     */
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

    /**
     * draw map to graphics context
     *
     */
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

                int rc = map[row][col] - 1;//get map time value
                //parse numbers to row/col positions in Tiles instance
                int r = rc / numTilesAcross;
                int c = rc % numTilesAcross;

                tiles[r][c].draw(
                        (int) x + col * xTileSize,
                        (int) y + row * yTileSize
                );//draw each tile in appropriate position

            }
        }
    }

    /**
     * parse file reference into JSON object
     *
     * @param json path to JSON file
     */
    private JSONObject parseJSON(String ref) {
        JSONParser parser = new JSONParser();
        JSONObject jObj = new JSONObject();

        try {
            Object obj = parser.parse(new FileReader(ref));
            jObj = (JSONObject) obj;

        } catch (IOException | ParseException ex) {
            Logger.getLogger(EntityMap.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jObj;
    }
}
