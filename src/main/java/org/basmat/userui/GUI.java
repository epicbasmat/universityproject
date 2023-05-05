package org.basmat.userui;

import org.basmat.map.view.LoadingScreen;
import org.basmat.map.view.SimulationInteractionUI;
import org.basmat.map.view.SimulationUI;
import org.basmat.map.view.VariableSelectionUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Parent JFrame to hold all components of the simulation UI, such as the matrix, outputs, buttons etc.
 */
public class GUI extends JFrame {

    private final CardLayout layout;
    private final JPanel simUI;
    private final JPanel cardLayout;

    public final String PARAMETER_CARD = "PARAMETER_CARD";
    public final String SIMULATION_CARD = "SIMULATION_CARD";
    public final String LOADING_CARD = "LOADING_CARD";
    private final LoadingScreen loading;
    private Dimension minimumSize;


    public GUI(SimulationUI viewStructure, SimulationInteractionUI userInteractionUi, VariableSelectionUI variableSelectionUI) {
        this.setTitle("Simulation");
        //minimumSize acts as the standard dimension for all panels that get too big for their boots
        minimumSize = new Dimension(1500, 850);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTabbedPane tabbedPane = new JTabbedPane();
        this.setMinimumSize(minimumSize);
        //simUI is a panel that holds both the simulation and the user panel
        simUI = new JPanel();
        simUI.setLayout(new BorderLayout());
        simUI.add(viewStructure, BorderLayout.CENTER);
        simUI.add(userInteractionUi, BorderLayout.AFTER_LINE_ENDS);
        this.setLayout(new BorderLayout());
        layout = new CardLayout();
        loading = new LoadingScreen();
        //cardlayout contains the three different cards to flip to, parameter card / main menu, simulation and loading screen
        cardLayout = new JPanel(layout);
        cardLayout.add(variableSelectionUI, this.PARAMETER_CARD);
        cardLayout.add(simUI, this.SIMULATION_CARD);
        cardLayout.add(loading, this.LOADING_CARD);
        //The tabbed pane contains the card layout and the guide, as it's seperate and needs to always be able to be accessed.
        tabbedPane.add("Simulation", cardLayout);
        cardLayout.addMouseListener(viewStructure);
        try {
            this.setIconImage(ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("textures/icon.png"))));
            //guide is a JEditorPane used to display the guide file within /html/
            JEditorPane guide = new JEditorPane();
            guide.setEditable(false);
            guide.setPreferredSize(minimumSize);
            guide.setContentType("text/html");
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("html/guide.html");
            assert resourceAsStream != null;
            //to parse the text through getResourceAsStream, we need read its bytes and convert it to a recognizable format.
            guide.setText(new BufferedReader(new InputStreamReader(resourceAsStream)).lines().collect(Collectors.joining(System.lineSeparator())));
            JScrollPane jScrollPane = new JScrollPane(guide);
            jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            tabbedPane.add("Guide", jScrollPane);
            jScrollPane.setMaximumSize(new Dimension(150, 150));
            simUI.add(new JLabel(new ImageIcon(ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("textures/legend.png"))))), BorderLayout.LINE_START);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.add(tabbedPane);
        pack();
        setVisible(true);
    }


    /**
     * Sets the currently visible card within the internal cardLayout.
     * It's recommended to use the static final Strings implemented in this class.
     * @param card The card ID to flip to.
     */
    public void goToCard(String card) {
        SwingUtilities.invokeLater(() -> {
            layout.show(cardLayout, card);
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
