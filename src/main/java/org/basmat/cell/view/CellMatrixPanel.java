package org.basmat.cell.view;

import javax.swing.*;
import java.awt.*;

public class CellMatrixPanel extends JPanel {
    private GridBagConstraints c;
    public CellMatrixPanel(int sizeX, int sizeY) {
        setSize(sizeX * 5, sizeY * 5);
        setVisible(true);
        c = new GridBagConstraints();
        setLayout(new GridBagLayout());
        //c.fill = GridBagConstraints.BOTH;
    }

    public void addCellPanel(CellPanel cellPanel, int x, int y) {
        c.gridx = x;
        c.gridy = y;
        c.ipadx = -5;
        c.ipady = -5;
        this.add(cellPanel, c);
        this.revalidate();
        this.repaint();
    }
}
