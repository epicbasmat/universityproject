package org.basmat.map.view;

import org.basmat.map.controller.Controller;

import javax.swing.*;
import java.awt.*;

/**
 * This class contains the JFrame responsible for the primary interface for the user. This panel contains the buttons that the user can click on, as well as system output.
 */
public class SimulationInteractionUI extends JPanel {

    private final JPanel userPanel;
    private final Controller controller;
    private JTextArea simulationTextInfo;
    private JTextArea timeStepTextInfo;
    private JScrollPane textScrollArea;
    private int timestep;
    private Button play;
    private Button pause;
    private Button saveAsPng;
    private Button saveData;

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
        buttonPanel.setLayout(new GridBagLayout());
        play = new Button("Play simulation");
        pause = new Button("Pause simulation");
        saveAsPng = new Button("Save as PNG");
        saveData = new Button("Save data");
        GridBagConstraints c = new GridBagConstraints();
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
        saveAsPng.addActionListener((point) -> {
            controller.saveAsImage();
        });
        saveData.addActionListener((point) ->  {
            controller.saveAsData();
        });
        play.setSize(30, 30);

        //Setting the simulation buttons position. it's messy
        c.gridx = 0;
        c.gridy = 0;
        c.ipady = 10;
        buttonPanel.add(play, c);
        c.gridx = 1;
        c.gridy = 0;
        c.ipady = 10;
        buttonPanel.add(pause, c);
        c.gridx = 0;
        c.gridy = 1;
        c.ipady = 20;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        c.weightx = 0;
        buttonPanel.add(saveAsPng, c);
        c.gridx = 0;
        c.gridy = 2;
        c.ipady = 20;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        c.weightx = 0;
        buttonPanel.add(saveData, c);
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


    /**
     * Disables both the play and pause buttons to prevent user interaction.
     */
    public void disableUserInput() {
        play.setEnabled(false);
        pause.setEnabled(false);
    }

    /**
     * Enables the play to enable user interaction
     */
    public void enableUserInput() {
        play.setEnabled(true);
    }

    /**
     * This method sends a string to the view
     * @param string The string to render
     */
    public void appendText(String string) {
        simulationTextInfo.append(string + "\n");
    }
}

