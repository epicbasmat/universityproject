package org.basmat.map.util;

import org.basmat.map.model.ModelStructure;
import org.basmat.map.model.cells.factory.CellFactory;
import org.basmat.map.model.cells.factory.IMapCell;

import java.awt.*;

public class TestUtilities {
    public static void fillModelWithWorldCell(ModelStructure modelStructure, ECellType eCellType) {
        CellFactory cellFactory = new CellFactory();
        for (int i = 0; i < 150; i++) {
            for (int j = 0; j < 150; j++) {
                modelStructure.setBackLayer(new Point(i,j), cellFactory.createWorldCell(eCellType));
            }
        }
    }

    public static  <T extends IMapCell> void overwriteModelWithWorldCell(ModelStructure modelStructure, ECellType blacklist, ECellType filler) {
        CellFactory cellFactory = new CellFactory();
        for (int i = 0; i < 150; i++) {
            for (int j = 0; j < 150; j++) {
                Point p =new Point(i,j);
                if (!(modelStructure.getCoordinate(p).getECellType().equals(blacklist))) {
                    modelStructure.deleteCoordinate(p);
                    modelStructure.setBackLayer(p, cellFactory.createWorldCell(filler));
                }
            }
        }
    }
}
