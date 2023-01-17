package org.basmat.cell.data;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * CellData provides overarching methods that all cell data types should implement to help with models
 */
public abstract class CellData  {

    private ECellType cellType;
    private BufferedImage texture;
    private Point point;

    /**
     * @param cellType the ECellType to set
     */
    CellData(ECellType cellType) {
        this.cellType = cellType;
    }

    public BufferedImage getTexture() {
        return this.texture;
    }

    /**
     * Returns the set ECellType
     * @return the set cell type
     */
    public ECellType getCellType() {
        return cellType;
    }

    public void setCellType(ECellType cellType) throws IOException {
        this.texture = ImageIO.read(new File(cellType.getPath()));
    }
}
