package org.basmat.map.data;


import org.basmat.map.util.ECellType;
import org.basmat.map.view.CellMatrixPanel;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class CellDataHelper {
    private CellMatrixPanel cellMatrixPanel;
    private HashMap<ECellType, BufferedImage> imageCache;

    public CellDataHelper(CellMatrixPanel cellMatrixPanel, HashMap<ECellType, BufferedImage> imageCache) {
        this.cellMatrixPanel = cellMatrixPanel;
        this.imageCache = imageCache;
    }
}
