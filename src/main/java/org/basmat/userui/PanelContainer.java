package org.basmat.userui;

import org.basmat.map.util.ECellType;
import org.basmat.map.view.MenuUI;
import org.basmat.map.view.UserInteractionUI;
import org.basmat.map.view.ViewStructure;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Parent JFrame to hold all components of the simulation UI, such as the matrix, outputs, buttons etc.
 */
public class PanelContainer extends JFrame {

    private final CardLayout layout;
    private final JPanel simUI;
    private final JPanel cardLayout;

    public PanelContainer(ViewStructure viewStructure, UserInteractionUI userInteractionUi, MenuUI menuUI) {
        this.setTitle("Simulation");
        try {
            this.setIconImage(ImageIO.read(new File(ECellType.BASE_PATH.getPath() + "icon.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.setMinimumSize(new Dimension(600, 450));
        addMouseListener(viewStructure);
        simUI = new JPanel();
        simUI.setLayout(new BorderLayout());
        simUI.add(viewStructure, BorderLayout.CENTER);
        simUI.add(userInteractionUi, BorderLayout.AFTER_LINE_ENDS);
        this.setLayout(new BorderLayout());
        layout = new CardLayout();
        cardLayout = new JPanel(layout);
        cardLayout.add(menuUI, "Parameter selection");
        cardLayout.add(simUI, "Simulation");
        this.add(cardLayout);
        pack();
        setVisible(true);
    }

    public void nextCard() {
        SwingUtilities.invokeLater(() -> {
            layout.next(cardLayout);
            this.setMinimumSize(new Dimension(1200, 950));
        });
    }
}
