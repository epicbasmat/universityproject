package org.basmat.map.view;

import org.basmat.map.controller.Controller;

import javax.swing.*;
import java.awt.*;

/**
 * This class sets up the Panel used for user selection of system variables
 */
public class SimulationInteractionUI extends JPanel {

    private final JPanel userPanel;
    private final Controller controller;
    private JTextArea simulationTextInfo;
    private JTextArea timeStepTextInfo;
    private JScrollPane textScrollArea;
    private int timestep;

    public SimulationInteractionUI(Controller controller) {
        userPanel = new JPanel();
        userPanel.setLayout(new GridLayout());
        userPanel.setVisible(true);
        this.controller = controller;
        setSize(100,100);
        setVisible(true);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setupButtonPanel();
        this.add(userPanel);
        timeStepSetup();
        userPanel.add(timeStepTextInfo);
        textArea();
    }

    private void timeStepSetup() {
        timeStepTextInfo = new JTextArea(1, 1);
        timeStepTextInfo.setEditable(false);
        timeStepTextInfo.setSize(10, 10);
        timestep = 0;
    }

    public void incrementTimeStep() {
        timestep++;
        timeStepTextInfo.setText(Integer.toString(timestep) + "\n" + "Seed: " + controller.getSeed());
    }

    private void setupButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setVisible(true);
        buttonPanel.setLayout(new FlowLayout());
        Button play = new Button("Play simulation");
        Button pause = new Button("Pause simulation");
        pause.setEnabled(false);
        play.addActionListener((point) -> {
            play.setEnabled(false);
            controller.play();
            pause.setEnabled(true);
        });
        pause.addActionListener((point) -> {
            pause.setEnabled(false);
            controller.pause();
            play.setEnabled(true);
        });
        play.setSize(30, 30);
        buttonPanel.add(play);
        buttonPanel.add(pause);
        userPanel.add(buttonPanel);
        revalidate();
    }

    private void textArea() {
        simulationTextInfo = new JTextArea(null, null, 10, 10);
        textScrollArea = new JScrollPane(simulationTextInfo);
        textScrollArea.setPreferredSize(new Dimension(400, 300));
        simulationTextInfo.setEditable(false);
        this.add(textScrollArea);
    }

    public void appendText(String string) {
        simulationTextInfo.append(string + "\n");
    }
}

