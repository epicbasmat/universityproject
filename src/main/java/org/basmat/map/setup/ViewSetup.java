package org.basmat.map.setup;

import org.basmat.map.model.ModelStructure;
import org.basmat.map.model.cells.factory.IMapCell;
import org.basmat.map.view.CellPanel;
import org.basmat.map.view.ViewStructure;

import java.awt.*;

public class ViewSetup {
    public static void setupView(ViewStructure viewStructure, ModelStructure modelStructure) {
        //TODO: Release 145 from it's hardcoded hell
        for (int x = 0; x < 145; x++) {
            for (int y = 0; y < 145; y++) {
                IMapCell coordinate = modelStructure.getCoordinate(new Point(x, y));
                CellPanel cellPanel = new CellPanel(coordinate.getTexture());
                viewStructure.addCellPanel(cellPanel, new Point(x, y));
            }
        }
    }
}
