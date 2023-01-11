package org.basmat.cell;

import javax.swing.*;
import java.awt.*;

public class CellMatrixPanel extends JPanel {
    private CellPanel[][] cellPanelMatrix;
    GridBagConstraints gbc = new GridBagConstraints();
    CellMatrixPanel(int sizeX, int sizeY) {
        setSize(sizeX, sizeY);
        setVisible(true);
        /**
         * TODO: CHANGE DIVIDABLE BY 5 TO BE PARAMETERIZED
         */
        setLayout(new GridBagLayout());
        cellPanelMatrix = new CellPanel[sizeX][sizeY];
    }

    public void addCellPanel(CellPanel cellPanel, Point point) {
        gbc.gridx = (int) point.getX();
        gbc.gridy = (int) point.getY();
        this.add(cellPanel);

    }

    public CellPanel getCellPanel(int x, int y) {
        return this.cellPanelMatrix[x][y];
    }

}
