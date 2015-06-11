package SpaceInvaders_V4.Entity;

import SpaceInvaders_V4.GameState.GameState;
import SpaceInvaders_V4.Main.ResourceFactory;
import SpaceInvaders_V4.Util.Sprite;
import SpaceInvaders_V4.Util.SystemTimer;

/**
 *
 * @author Bee-Jay
 */
public class SmallExplosion extends Entity {

    private GameState game;
    private Sprite[] frames;
    private int currentFrame = 0;
    private double lastFrameChange = 0;

    /**
     *
     * @param game
     * @param x
     * @param y
     * @param ref
     */
    public SmallExplosion(GameState game, int x, int y, String ref) {
        super(x, y, ref.equals("big") ? "resources/sprites/effects/explosion_65px_12tile.png" : "resources/sprites/effects/explosion_50px_12tile.png");
        this.game = game;
        frames = new Sprite[12];
        int tileHeight = sprite.getHeight();
        int tileWidth = sprite.getWidth() / frames.length;

        for (int col = 0; col < frames.length; col++) {
            frames[col] = ResourceFactory.get().getSubSprite(sprite.getRef(), tileWidth * col, 0, tileWidth, tileHeight);
        }

        sprite = frames[currentFrame];

    }

    @Override
    public void doLogic() {
        if (SystemTimer.getTime() - lastFrameChange > 0.04) {
            if (currentFrame < frames.length - 1) {
                currentFrame++;
                sprite = frames[currentFrame];
            } else {
                game.getRemoveEffects().add(this);
            }

            lastFrameChange = SystemTimer.getTime();
        }
    }

    @Override
    public void collidedWith(Entity other) {
    }

}
