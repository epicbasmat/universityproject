package org.basmat.userui;

import org.basmat.map.view.UI;
import org.basmat.map.view.ViewStructure;

import javax.swing.*;
import java.awt.*;

/**
 * Parent JFrame to hold all components of the simulation UI, such as the matrix, outputs, buttons etc.
 */
public class PanelContainer extends JFrame {
    public PanelContainer(ViewStructure viewStructure, UI ui) {
        this.setLayout(new FlowLayout());
        add(viewStructure);
        add(ui);
        addMouseListener(viewStructure);
        setTitle("SIMULATION");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1000, 1000));
        pack();
        setVisible(true);
    }
}
