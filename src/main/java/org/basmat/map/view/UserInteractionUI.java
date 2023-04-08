package org.basmat.map.view;

import org.basmat.map.controller.CellMatrixController;

import javax.swing.*;
import java.awt.*;

/**
 * This class sets up the Panel used for user selection of system variables
 */
public class UserInteractionUI extends JPanel {

    private JPanel userPanel;
    private CellMatrixController cellMatrixController;
    private JTextArea simulationTextInfo;
    private JTextArea timeStepTextInfo;
    private JScrollPane textScrollArea;
    private int timestep;

    public UserInteractionUI(CellMatrixController cellMatrixController) {
        userPanel = new JPanel();
        userPanel.setLayout(new GridLayout());
        userPanel.setVisible(true);
        this.cellMatrixController = cellMatrixController;
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
        timeStepTextInfo.setText(Integer.toString(timestep));
    }

    private void setupButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setVisible(true);
        buttonPanel.setLayout(new FlowLayout());
        Button play = new Button("Play simulation");
        play.addActionListener((point) -> cellMatrixController.doThing());
        Button pause = new Button("Pause simulation");
        pause.addActionListener((point) -> cellMatrixController.doThing());
        play.setSize(30, 30);
        buttonPanel.add(play);
        buttonPanel.add(pause);
        userPanel.add(buttonPanel);
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

