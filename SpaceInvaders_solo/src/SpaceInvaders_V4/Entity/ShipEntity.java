package SpaceInvaders_V4.Entity;


public class ShipEntity extends Entity{

    public ShipEntity(int x, int y, String ref) {
        super(x, y, ref);
    }

    @Override
    public void collidedWith(Entity other) {
    }
    
}
