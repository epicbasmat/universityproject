package org.basmat.map.util;

import org.basmat.map.model.ModelStructure;
import org.basmat.map.model.cells.factory.CellFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class TestUtilities {
    public static void fillModelWithWorldCell(ModelStructure modelStructure, ECellType eCellType) {
        CellFactory cellFactory = new CellFactory();
        HashMap<ECellType, BufferedImage> cache = TextureHelper.cacheCellTextures();
        for (int i = 0; i < 150; i++) {
            for (int j = 0; j < 150; j++) {
                modelStructure.setBackLayer(new Point(i,j), cellFactory.createWorldCell(eCellType, cache.get(eCellType)));
            }
        }
    }
}
