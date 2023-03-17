package org.basmat.map.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

public class TextureHelper {
    public static HashMap<ECellType, BufferedImage> cacheCellTextures(HashMap<ECellType, BufferedImage> imageCache) {
        System.out.println("Grabbing textures");
        for (ECellType cellType : ECellType.values()) {
            try {
                if (cellType.getCellDescription() != null) {
                    BufferedImage texture = ImageIO.read(new File(cellType.getPath()));
                    imageCache.put(cellType, texture);
                }
            } catch (Exception e) {
                System.out.println("An error has occurred fetching textures: ");
                System.out.println(e.getMessage());
                System.out.println("Accessing file path: " + cellType.getPath());
            }
        }
        System.out.println("Finished");
        return imageCache;
    }

    public static BufferedImage copyTexture(BufferedImage reference) {
        //A new texture has to be loaded from the original Image.IO read to determine the imagetype to enable alpha transparency.
        BufferedImage texture = new BufferedImage(reference.getWidth(), reference.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < reference.getWidth(); i++) {
            for (int j = 0; j < reference.getHeight(); j++) {
                texture.setRGB(i, j, reference.getRGB(i, j));
            }
        }
        return texture;
    }


    /**
     * Tints the loaded bufferedimage from the view.
     * @param tint The RGB value to mask the image with using an OR operator. \n 0xAARRGGBB - AA = Alpha, RR = Red, GG = Green, BB = Blue
     */
    public static void setTint(BufferedImage texture, int tint) {
        for (int x = 0; x < texture.getWidth(); x++) {
            for (int y = 0; y < texture.getHeight(); y++) {
                texture.setRGB(x, y, texture.getRGB(x, y) | tint);
            }
        }
    }
}
