package org.basmat.cell;

import java.awt.*;
import java.awt.image.BufferedImage;

public class WorldCell extends CellData{

    /**
     *
     * @param cellType the type of cell to be set
     * @param texture the internal texture to be set
     */
    WorldCell(ECellType cellType, BufferedImage texture) {
        super(cellType, texture);
    }


}
