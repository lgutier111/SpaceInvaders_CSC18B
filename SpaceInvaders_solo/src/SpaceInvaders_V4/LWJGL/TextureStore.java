package SpaceInvaders_V4.LWJGL;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class TextureStore {

    private static final TextureStore store = new TextureStore();

    public static TextureStore get() {
        return store;
    }

    private final HashMap textures = new HashMap();

    public Texture getTexture(String ref) {
        Texture tex = (Texture) textures.get(ref);
        if (tex != null) {
            return tex;
        }
        try {
//            texture = window.getTextureLoader().getTexture(ref);

            InputStream in = ResourceLoader.getResourceAsStream(ref);
            tex = TextureLoader.getTexture(ref.substring(ref.length() - 3), in);

        } catch (IOException e) {

            System.err.println("Unable to load texture: " + ref);
            System.exit(0);
        }
        tex.setTextureFilter(GL11.GL_NEAREST);
        
        System.out.println("Texture "+ref+" width: "+tex.getWidth()+" height: "+tex.getHeight());
        System.out.println("Image "+ref+" width: "+tex.getImageWidth()+" height: "+tex.getTextureHeight());
        
        textures.put(ref, tex);
        
        return tex;
    }

}
