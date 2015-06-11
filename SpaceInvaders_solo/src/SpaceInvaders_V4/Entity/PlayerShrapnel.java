package SpaceInvaders_V4.Entity;

import SpaceInvaders_V4.GameState.GameState;
import SpaceInvaders_V4.Main.ResourceFactory;
import SpaceInvaders_V4.Util.Sprite;
import SpaceInvaders_V4.Util.SystemTimer;
import java.awt.Color;

/**
 *
 * @author Bee-Jay
 */
public class PlayerShrapnel extends ShotEntity {

    private Sprite[][] frames;
    private Sprite[][] shadowFrames;
    private int currentRow;
    private int currentCol;
    private double lastFrameChange;
    
    /**
     *
     * @param game
     * @param dmg
     * @param speed
     * @param angle
     * @param x
     * @param y
     * @param ref
     */
    public PlayerShrapnel(GameState game, int dmg, int speed, float angle, int x, int y, String ref) {
        super(game, dmg, speed, angle, x, y, "resources/sprites/player/Player1Shrapnel.png");
        
        frames = new Sprite[4][8];
        shadowFrames = new Sprite[frames.length][frames[0].length];
        int height = sprite.getHeight()/frames.length;
        int width = sprite.getWidth()/frames[0].length;
        
        for(int row = 0; row<frames.length;row++){
            for(int col = 0; col< frames[0].length; col++){
                frames[row][col]=ResourceFactory.get().getSubSprite(sprite.getRef(), width*col, height*row, width, height);
                shadowFrames[row][col]=ResourceFactory.get().getShadedSubSprite(sprite.getRef(), width*col, height*row, width, height, Color.black, 0.5f);
            }
        }
        
        currentCol = 0;
        currentRow = (int)(Math.random()*4);
        sprite = frames[currentRow][currentCol];
    }

    @Override
    public void doLogic() {
        super.doLogic();
        
        if(SystemTimer.getTime()-lastFrameChange > 0.04){
            currentCol++;
            if(currentCol >= frames[currentRow].length){
                currentCol =0;
            }
            sprite = frames[currentRow][currentCol];
            lastFrameChange = SystemTimer.getTime();
        }
    }

    @Override
    public void draw() {
        shadowFrames[currentRow][currentCol].draw((int)x,(int) y);
        super.draw(); 
    }
    
    
    
}
