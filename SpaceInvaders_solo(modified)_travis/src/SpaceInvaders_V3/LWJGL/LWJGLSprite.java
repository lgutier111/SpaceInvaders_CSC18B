package SpaceInvaders_V3.LWJGL;

import SpaceInvaders_V3.Util.Sprite;
import java.awt.Color;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

public class LWJGLSprite implements Sprite {

    //texture that stores the image for this sprite
    private Texture texture;

    //sprite dimentions
    private int width;
    private int height;

    //subImage data
    private int subX = 0;
    private int subY = 0;
    private float subWidth;
    private float subHeight;

    //color mask data
    private Color color;
    private float alpha;

    private String ref;

    //create a new sprite from a specified image
    //@param window the window in whish the sprite will be drawn
    //@param ref a reference to the sprite image
    public LWJGLSprite(LWJGLGameWindow window, String ref) {

        this.ref = ref;
        texture = TextureStore.get().getTexture(ref);
        height = texture.getImageHeight();
        width = texture.getImageWidth();
        subHeight = height;
        subWidth = width;

    }

    //create a new sprite from a specified image
    //@param window the window in whish the sprite will be drawn
    //@param ref a reference to the sprite image
    public LWJGLSprite(
            LWJGLGameWindow window,
            String ref,
            Color color,
            float alpha,
            int[] subData) {

        this.ref = ref;
        texture = TextureStore.get().getTexture(ref);

        height = texture.getImageHeight();
        width = texture.getImageWidth();

        subX = subData[0];
        subY = subData[1];
        subWidth = subData[2];
        subHeight = subData[3];

        this.color = color;
        this.alpha = alpha;
    }

    //get width of sprite
    //@return the width of the sprite in pixels
    @Override
    public int getWidth() {
        return (int) width;
    }

    //get height of sprite
    //@return the height of the sprite in pixels
    @Override
    public int getHeight() {
        return (int) height;
    }

    //Draw the sprite at the specified location
    //@param x The x location at which to draw this sprite
    //@param y The y location at which to draw this sprite
    @Override
    public void draw(int x, int y) {
        // store the current model matrix
        GL11.glPushMatrix();

        // bind to the appropriate texture for this sprite
        texture.bind();

        // translate to the right location and prepare to draw
        GL11.glTranslatef(x, y, 0);
        GL11.glColor3f(1, 1, 1);

        // draw a quad textured to match the sprite
        GL11.glBegin(GL11.GL_QUADS);
        {

//            GL11.glTexCoord2f(subX, subY);
//            GL11.glVertex2f(0, 0);
//            GL11.glTexCoord2f(subX, subY+texture.getHeight());
//            GL11.glVertex2f(0, height);
//            GL11.glTexCoord2f(subX + texture.getWidth(), subY+ texture.getHeight());
//            GL11.glVertex2f(width, height);
//            GL11.glTexCoord2f(subX + texture.getWidth(), subY);
//            GL11.glVertex2f(width, 0);
            GL11.glTexCoord2f((float)subX/(float)(width/texture.getWidth()), (float)subY/(float)(height/texture.getHeight()));
            GL11.glVertex2f(0, 0);
            
            GL11.glTexCoord2f((float)subX/(float)(width/texture.getWidth()), (float)subY+subHeight/(float)(height/texture.getHeight()));
            GL11.glVertex2f(0, subHeight);
            
            GL11.glTexCoord2f((float)subX + subWidth/(float)(width/texture.getWidth()), (float)subY+ subHeight/(float)(height/texture.getHeight()));
            GL11.glVertex2f(subWidth, subHeight);
            
            GL11.glTexCoord2f((float)subX + subWidth/(float)(width/texture.getWidth()), (float)subY/(float)(height/texture.getHeight()));
            GL11.glVertex2f(subWidth, 0);

            System.out.println("Sprite "+ref+" width: "+width+" height: "+height);
           System.out.println("Sprite "+ref+" subwidth: "+subWidth+" subheight: "+subHeight);
        }
        GL11.glEnd();

        // restore the model view matrix to prevent contamination
        GL11.glPopMatrix();
    }

    @Override
    public Image getImage() {
        return null;
    }

    @Override
    public String getRef() {
        return this.ref;
    }
    
    @Override
    public void drawRotate(AffineTransform tra){
        
    }
}
