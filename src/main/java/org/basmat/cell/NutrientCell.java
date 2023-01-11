package org.basmat.cell;

import java.awt.*;
import java.awt.image.BufferedImage;

public class NutrientCell extends CellData {
    NutrientCell(BufferedImage texture) {
        super(ECellType.NUTRIENTS, texture);
    }
}
