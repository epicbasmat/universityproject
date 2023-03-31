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
        this.setLayout(new BorderLayout());
        add(viewStructure, BorderLayout.CENTER);
        add(ui, BorderLayout.AFTER_LINE_ENDS);
        setTitle("SIMULATION");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addMouseListener(viewStructure);
        setPreferredSize(new Dimension(1200, 1200));
        pack();
        setVisible(true);

    }
}
