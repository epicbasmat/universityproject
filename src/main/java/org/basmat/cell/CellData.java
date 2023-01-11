package org.basmat.cell;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CellData implements ICell{

    private ECellType cellType;
    private BufferedImage texture;
    private Point point;

    CellData(ECellType cellType, BufferedImage texture) {
        this.cellType = cellType;
        //To copy a texture to be original for the class
        this.texture = new BufferedImage(texture.getColorModel(),
                texture.copyData(null),
                texture.getColorModel().isAlphaPremultiplied(),
                null);
    }

    public BufferedImage getTexture() {
        return this.texture;
    }

    public ECellType getCellType() {
        return cellType;
    }

    public void setCellType(ECellType cellType) throws IOException {
        this.texture = ImageIO.read(new File(cellType.getPath()));
    }

    public void setTint(int mask, int operand) {
        for (int x = 0; x < texture.getWidth(); x++) {
            for (int y = 0; y < texture.getHeight(); y++) {
                switch (operand) {
                    case 1:
                        texture.setRGB(x, y, ~texture.getRGB(x, y));
                        break;
                    case 2:
                        texture.setRGB(x, y, texture.getRGB(x, y) & mask);
                        break;
                    case 3:
                        texture.setRGB(x, y, texture.getRGB(x, y) ^ mask);
                }
            }
        }
    }
}
