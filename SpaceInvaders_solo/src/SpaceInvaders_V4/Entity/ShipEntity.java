package SpaceInvaders_V4.Entity;

/**
 *
 * @author Bee-Jay
 */
public class ShipEntity extends Entity{

    /**
     *
     * @param x
     * @param y
     * @param ref
     */
    public ShipEntity(int x, int y, String ref) {
        super(x, y, ref);
    }

    @Override
    public void collidedWith(Entity other) {
    }
    
}
