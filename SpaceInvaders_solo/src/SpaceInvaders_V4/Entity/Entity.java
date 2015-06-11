package SpaceInvaders_V4.Entity;

//Abstract Class for entity movement and colision detection
import SpaceInvaders_V4.Util.Sprite;
import SpaceInvaders_V4.Main.ResourceFactory;
import java.awt.Rectangle;

/**
 * abstract class for game entities
 * @author Bee-Jay
 */
public abstract class Entity {

   
    /**
     *current horizontal coordinates
     */
    protected float x;//

    /**
     *current vertical coordinates
     */
    protected float y;

    /**
     *sprite for entity
     */
    protected Sprite sprite;
    //

    /**
     *horizontal speed of entity in pix/sec
     */
    protected float dx;

    /**
     *vertical speed of entity in pix/sec
     */
    protected float dy;


    /**
     * collision detection variable
     */
    protected Rectangle hitBox = new Rectangle();

    /**
     * Entity Constructor
     *
     * @param x initial x location
     * @param y initial y location
     * @param ref reference to sprite image for i/o
     */
    public Entity(int x, int y, String ref) {
        this.sprite = ResourceFactory.get().getSprite(ref);
        this.x = x;
        this.y = y;
        hitBox = new Rectangle(
                (int) x - (sprite.getWidth() / 2),
                (int) y - (sprite.getHeight() / 2),
                sprite.getWidth(),
                sprite.getHeight()
        );
    }

    /**
     * Move entity relative to frame
     *
     * @param delta the amount of time passed since last move in seconds
     */
    public void move(Double delta) {
        //update location based on move speed
        x += (delta * dx);//shift horizontally
        y += (delta * dy);//shift vertically

    }

    /**
     * Move entity relative to horizontal vectors. Use for air entities
     *
     * @param delta the amount of time passed since last move in seconds
     * @param xShift horizontal vector
     */
    public void move(Double delta, float xShift) {
        //update location based on move speed
        x += (delta * (dx + xShift));//shift horizontally
        y += (delta * dy);//shift vertically

    }

    /**
     * Move entity relative to horizontal and vertical vectors. Use for ground
     * entities
     *
     *
     * @param delta the amount of time passed since last move in seconds
     * @param xShift horizontal vector
     * @param yShift vertical vector
     */
    public void move(Double delta, float xShift, float yShift) {
        //update location based on move speed
        x += (delta * (dx + xShift));//shift horizontally
        y += (delta * (dy + yShift));//shift vertically

    }

    /**
     * set horizontal speed
     *
     * @param dx horizontal speed in pixels/sec
     */
    public void setHorizontalMovement(float dx) {
        this.dx = dx;
    }

    /**
     * set vertical speed
     *
     * @param dy vertical speed in pixels/sec
     */
    public void setVerticalMovement(float dy) {
        this.dy = dy;
    }

    /**
     * get Horizontal speed
     *
     * @return Horizontal speed in pixels/sec
     */
    public float getHorizontalMovement() {
        return dx;
    }

    /**
     * get vertical speed
     *
     * @return vertical speed in pixels/sec
     */
    public float getVerticalMovement() {
        return dy;
    }

    /**
     * get Entity HitBox
     *
     * @return entity collision HitBox
     */
    public Rectangle getHitBox() {
        return hitBox;
    }

    /**
     * Draw this entity to the graphics context provided
     */
    public void draw() {
        //center sprite on x/y coords
        sprite.draw((int) x - (sprite.getWidth() / 2), (int) y - (sprite.getHeight() / 2));

    }

    /**
     * Execute the update logic associated with this entity.
     */
    public void doLogic() {
    }

    /**
     * Get the x location of this entity
     *
     * @return x location
     */
    public float getX() {
        return x;
    }

    /**
     * set x location for this entity
     *
     * @param x location
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Get the y location of this entity
     *
     * @return y location
     */
    public float getY() {
        return y;
    }

    /**
     * set y location for this entity
     *
     * @param y location
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * get the current sprite object
     *
     * @return current sprite object
     */
    public Sprite getSprite() {
        return this.sprite;
    }

    /**
     * check collisions
     *
     * @param other the entity to check against
     * @return true if collision detected
     */
    public boolean collidesWith(Entity other) {
        return hitBox.intersects(other.getHitBox());
    }

    /**
     * Notification that this entity collided with another.
     *
     * @param other The entity with which this entity collided.
     */
    public abstract void collidedWith(Entity other);

}
