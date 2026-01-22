package graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.HashMap;
import java.util.Map;

public class ResourceManager {
    private static Map<String, BufferedImage> textures = new HashMap<>();

    public static BufferedImage getTexture(String path) {
        if (!textures.containsKey(path)) {
            try {
                // Try looking in local file system first (for development)
                File f = new File(path);
                if (f.exists()) {
                    textures.put(path, ImageIO.read(f));
                } else {
                    // Try class path resource
                    textures.put(path, ImageIO.read(ResourceManager.class.getResourceAsStream("/" + path)));
                }
            } catch (Exception e) {
                System.err.println("Could not load texture: " + path);
                return null;
            }
        }
        return textures.get(path);
    }
}
