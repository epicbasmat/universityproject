package org.basmat.cell.view;

import javax.swing.*;
import java.awt.*;

public class PanelContainer extends JFrame {
    public PanelContainer(CellMatrixPanel cellMatrixPanel) {
        add(cellMatrixPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1000, 1000));
        pack();
        setVisible(true);
    }

    public void callRepaint() {
        repaint();
    }
}
