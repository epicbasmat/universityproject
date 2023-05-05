package org.basmat.map.setup;

import org.basmat.map.model.ModelStructure;
import org.basmat.map.model.cells.factory.IMapCell;
import org.basmat.map.view.CellPanel;
import org.basmat.map.view.SimulationUI;

import javax.swing.*;
import java.awt.*;

public class ViewSetup {

    public static final int IS_NOT_LAZY = 0;
    public static final int IS_LAZY = 1;

    public static void setupView(SimulationUI viewStructure, ModelStructure modelStructure) {
        setupView(viewStructure, modelStructure, IS_NOT_LAZY);
    }

    /**
     * Reconstructs the user view to match the model.
     * @param viewStructure The viewStructure to update.
     * @param modelStructure The modelStructure to match against
     * @param isLazy Determines if the logic is lazy. If there is no change to the cell, then the system will not re-render the texture.
     */
    public static void setupView(SimulationUI viewStructure, ModelStructure modelStructure, int isLazy) {
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
            viewStructure.revalidate();
        });
    }

    public static void revalidate(SimulationUI viewStructure) {
        SwingUtilities.invokeLater(viewStructure::revalidate);
    }

    private static void setPanel(SimulationUI viewStructure, ModelStructure modelStructure, int x, int y) {
        IMapCell coordinate = modelStructure.getCoordinate(new Point(x, y));
        CellPanel cellPanel = new CellPanel(coordinate.getTexture());
        viewStructure.addCellPanel(cellPanel, new Point(x, y));
    }
}
