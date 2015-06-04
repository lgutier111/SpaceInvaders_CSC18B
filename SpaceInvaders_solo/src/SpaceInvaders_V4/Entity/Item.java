
package SpaceInvaders_V4.Entity;

public abstract class Item extends Entity {

    protected int type;
    public static int BULLET = 0;
    public static int LASER = 1;
    
    public Item(int x, int y, String ref) {
        super(x, y, ref);
    }
    
    public int getType(){
        return type;
    }
}
