/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpaceInvaders_V4.Entity;

import SpaceInvaders_V4.Main.ResourceFactory;
import SpaceInvaders_V4.Util.Sprite;
import SpaceInvaders_V4.Util.SystemTimer;

/**
 * The Fire class must be called and managed by the entity class upon which it is to be
 * Calling Entity class should create local ArrayList fields to manage Fire Effects
 * @author Bee-Jay
 */
public class Fire extends Entity {

    private Sprite[][] frames;
    private int currentRow = 0;
    private int currentCol = 0;
    private double lastFrameChange = 0;
    int tileHeight;
    int tileWidth;

    /**
     *
     * @param x
     * @param y
     * @param ref
     */
    public Fire(int x, int y, String ref) {
        super(x, y, ref.equals("small") ? "resources/sprites/effects/fire_16px_8x4tile.png"
                : ref.equals("med") ? "resources/sprites/effects/fire_32x48px_8x5tile.png" : "resources/sprites/effects/fire_32x64px_8x4tile.png");

        frames = new Sprite[ref.equals("med") ? 5 : 4][8];

        
        tileHeight = sprite.getHeight() / frames.length;
        tileWidth = sprite.getWidth() / frames[0].length;

        for (int row = 0; row < frames.length; row++) {
            for (int col = 0; col < frames[0].length; col++) {
                frames[row][col] = ResourceFactory.get().getSubSprite(sprite.getRef(), tileWidth * col, tileHeight * row, tileWidth, tileHeight);
            }
        }

        currentRow = (int) (Math.random() * (ref.equals("med") ? 5 : 4));
    }

    @Override
    public void doLogic() {
        if (SystemTimer.getTime() - lastFrameChange > 0.04) {
            currentCol = currentCol + 1 >= frames[0].length ? 0 : currentCol + 1;

            lastFrameChange = SystemTimer.getTime();
        }
    }

    @Override
    public void draw(){
        frames[currentRow][currentCol].draw((int) (x-tileWidth/2), (int) (y-tileHeight/2));
    }
    
    @Override
    public void collidedWith(Entity other) {

    }

}
