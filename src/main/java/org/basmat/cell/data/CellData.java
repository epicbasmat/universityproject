package org.basmat.cell.data;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CellData  {

    private ECellType cellType;
    private BufferedImage texture;
    private Point point;

    CellData(ECellType cellType) {
        this.cellType = cellType;
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
}
