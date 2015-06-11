
package SpaceInvaders_V4.Entity;

/**
 *
 * @author Bee-Jay
 */
public abstract class Item extends Entity {

    /**
     *
     */
    protected int type;

    /**
     *
     */
    public static int BULLET = 0;

    /**
     *
     */
    public static int LASER = 1;
    
    /**
     *
     * @param x
     * @param y
     * @param ref
     */
    public Item(int x, int y, String ref) {
        super(x, y, ref);
    }
    
    /**
     *
     * @return
     */
    public int getType(){
        return type;
    }
}
