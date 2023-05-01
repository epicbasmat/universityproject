package org.basmat.map.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Objects;

/**
 * TextureHelper provides methods to assist in texture generation and manipulation.
 */
public class TextureHelper {
    /**
     * This method will load in all textures within the designated path and map the CellType to the loaded BufferedImage
     * @return A HashMap mapping Enum CellType to it's texture
     */
    public static HashMap<ECellType, BufferedImage> cacheCellTextures() {
        BufferedImage missingTexture = new BufferedImage(5, 5, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < missingTexture.getWidth(); x++) {
            for (int y = 0; y < missingTexture.getHeight(); y++) {
                missingTexture.setRGB(x, y, 0xFF00FF);
            }
        }
        HashMap<ECellType, BufferedImage> imageCache = new HashMap<>();
        for (ECellType cellType : ECellType.values()) {
            try {
                if (cellType.getCellDescription() != null) {
                    BufferedImage texture = ImageIO.read(Objects.requireNonNull(TextureHelper.class.getClassLoader().getResourceAsStream(cellType.getPath())));
                    imageCache.put(cellType, texture);
                }
            } catch (Exception e) {
                imageCache.put(ECellType.MISSING_TEXTURE, missingTexture);
            }
        }
        return imageCache;
    }

    /**
     * This method will copy any texture and return it with the colorSpace of TYPE_INT_ARGB, this is done to enable alpha transparency as default-loaded images do not have that enabled.
     * @param reference The reference image to copy
     * @return The same image, but with TYPE_INT_ARGB
     */
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
}
