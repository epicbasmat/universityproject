package org.basmat.map.setup;

import org.basmat.map.model.ModelStructure;
import org.basmat.map.model.cells.factory.IMapCell;
import org.basmat.map.view.CellPanel;
import org.basmat.map.view.ViewStructure;

import javax.swing.*;
import java.awt.*;

public class ViewSetup {

    public static final int IS_NOT_LAZY = 0;
    public static final int IS_LAZY = 1;

    public static void setupView(ViewStructure viewStructure, ModelStructure modelStructure) {
        setupView(viewStructure, modelStructure, IS_NOT_LAZY);
    }

    public static void setupView(ViewStructure viewStructure, ModelStructure modelStructure, int isLazy) {
        //TODO: Release 145 from it's hardcoded hell
        SwingUtilities.invokeLater(() -> {
            for (int x = 0; x < 150; x++) {
                for (int y = 0; y < 150; y++) {
                    if (isLazy == IS_LAZY) {
                        if (!(viewStructure.getPanel(new Point(x, y)).isEqualTexture(modelStructure.getCoordinate(new Point(x, y)).getTexture()))) {
                            viewStructure.overwriteCell(new Point(x, y), new CellPanel(modelStructure.getCoordinate(new Point(x, y)).getTexture()));
                        }
                    } else {
                        setPanel(viewStructure, modelStructure, x, y);
                    }
                }
            }
        });
    }

    public static void revalidate(ViewStructure viewStructure) {
        SwingUtilities.invokeLater(viewStructure::revalidate);
    }

    private static void setPanel(ViewStructure viewStructure, ModelStructure modelStructure, int x, int y) {
        IMapCell coordinate = modelStructure.getCoordinate(new Point(x, y));
        CellPanel cellPanel = new CellPanel(coordinate.getTexture());
        viewStructure.addCellPanel(cellPanel, new Point(x, y));
    }
}
