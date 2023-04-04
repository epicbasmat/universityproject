package org.basmat.userui;

import org.basmat.map.view.MenuUI;
import org.basmat.map.view.UserInteractionUI;
import org.basmat.map.view.ViewStructure;

import javax.swing.*;
import java.awt.*;

/**
 * Parent JFrame to hold all components of the simulation UI, such as the matrix, outputs, buttons etc.
 */
public class PanelContainer extends JFrame {
    public PanelContainer(ViewStructure viewStructure, UserInteractionUI userInteractionUi, MenuUI menuUI) {
        this.setLayout(new BorderLayout());
        JPanel simUI = new JPanel();
        simUI.setLayout(new BorderLayout());
        simUI.add(viewStructure, BorderLayout.CENTER);
        simUI.add(userInteractionUi, BorderLayout.AFTER_LINE_ENDS);
        JTabbedPane jTab = new JTabbedPane();
        jTab.addTab("Menu", menuUI);
        jTab.addTab("Simulation", simUI);
        this.add(jTab);
        setTitle("Cell Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addMouseListener(viewStructure);
        setPreferredSize(new Dimension(1200, 1200));
        pack();
        setVisible(true);
    }
}
