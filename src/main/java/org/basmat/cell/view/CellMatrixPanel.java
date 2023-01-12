package org.basmat.cell.view;

import javax.swing.*;
import java.awt.*;

public class CellMatrixPanel extends JPanel {
    private GridBagConstraints c;
    public CellMatrixPanel(int sizeX, int sizeY) {
        setSize(750, 750);
        setVisible(true);
        c = new GridBagConstraints();
        setLayout(new GridBagLayout());
        c.fill = GridBagConstraints.BOTH;
    }

    public void addCellPanel(CellPanel cellPanel, int x, int y) {
        c.gridx = x-5;
        c.gridy = y-5;
        this.add(cellPanel, c);
        repaint();
    }
}
