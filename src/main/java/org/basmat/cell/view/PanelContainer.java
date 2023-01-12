package org.basmat.cell.view;

import javax.swing.*;
import java.awt.*;

public class PanelContainer extends JFrame {
    public PanelContainer(CellMatrixPanel cellMatrixPanel) {
        add(cellMatrixPanel);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(750, 750));
    }
}
