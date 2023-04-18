package org.basmat.userui;

import org.basmat.map.util.ECellType;
import org.basmat.map.view.SimulationInteractionUI;
import org.basmat.map.view.SimulationUI;
import org.basmat.map.view.VariableSelectionUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Parent JFrame to hold all components of the simulation UI, such as the matrix, outputs, buttons etc.
 */
public class GUI extends JFrame {

    private final CardLayout layout;
    private final JPanel simUI;
    private final JPanel cardLayout;

    public GUI(SimulationUI viewStructure, SimulationInteractionUI userInteractionUi, VariableSelectionUI variableSelectionUI) {
        this.setTitle("Simulation");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        cardLayout.add(variableSelectionUI, "Parameter selection");
        cardLayout.add(simUI, "Simulation");
        this.add(cardLayout);
        pack();
        setVisible(true);
    }

    /**
     * Switches the current JFrame to the next JFrame.
     * @deprecated
     */
    public void nextCard() {
        SwingUtilities.invokeLater(() -> {
            layout.next(cardLayout);
            this.setMinimumSize(new Dimension(1200, 950));
        });
    }

    /**
     * Throws an error to the user, and returns them to the main menu
     * @param string The error to provide. It's recommended to also include <code> Error.getLocalizedMessage() </code> with the message.
     */
    public void throwError(String string) {
         JOptionPane.showMessageDialog(this, string, "A fatal error has occurred", JOptionPane.ERROR_MESSAGE);
         layout.first(cardLayout);
    }
}