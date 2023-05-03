package org.basmat.map.view;

import org.basmat.map.controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;

/**
 * This class contains the JFrame responsible for the primary interface for the user. This panel contains the buttons that the user can click on, as well as system output.
 */
public class SimulationInteractionUI extends JPanel {

    private final JPanel userPanel;
    private final Controller controller;
    private JTextArea simulationTextInfo;
    private JTextArea timeStepTextInfo;
    private int timestep;
    private Button play;
    private Button pause;
    private JComboBox<String> cellSelector;

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
        setupSimulationOutput();
    }

    private void timeStepSetup() {
        timeStepTextInfo = new JTextArea(1, 1);
        timeStepTextInfo.setFont(new java.awt.Font("Consolas", Font.PLAIN, 12));
        timeStepTextInfo.setEditable(false);
        timeStepTextInfo.setSize(10, 10);
        //Initialize to -1 so when incrementTimeStep is initially called, it gets set to zero
        timestep = -1;
    }

    /**
     * Increments the current time step, and updates the view to match it.
     */
    public void incrementTimeStep() {
        timestep++;
        timeStepTextInfo.setText("Amount of societies: " + controller.getAmountOfSocieties() + "\n" +
                "Amount of Life Cells: " + controller.getAmountOfLifeCells() + "\n" +
                "Attrition threshold: " + controller.getSimulationProperties().attritionThreshold() + "\n" +
                "Food / life cell before starvation: " + controller.getSimulationProperties().foodThreshold() + "\n" +
                "Land / Life Cell before collapse: " + controller.getSimulationProperties().landRatio() + "\n" +
                "Initial Nutrient Cells / Society: " + controller.getSimulationProperties().initialNutrientCount() + "\n" +
                "Total Nutrient Cells: " + controller.getSimulationProperties().nutrientCount() + "\n" +
                "Overcrowding threshold: " + controller.getSimulationProperties().overcrowdThreshold()+ "\n" +
                "Seed: " + controller.getSeed() + "\n" +
                "**=======================**\n" +
                "|| Current timestep: " + timestep + "\n" +
                "**=======================**\n");

    }

    private void setupButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setVisible(true);
        buttonPanel.setLayout(new GridBagLayout());
        play = new Button("Play simulation");
        pause = new Button("Pause simulation");
        Button saveAsPng = new Button("Save as PNG");
        Button saveData = new Button("Save data");
        JToggleButton editingState = new JToggleButton("Enable Editing Mode");
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
            //To ensure that the user cannot change the state of the simulation while active, disable it while saving
            if (editingState.isSelected()) {
                editingState.setSelected(false);
            }
            controller.saveAsImage();
        });
        saveData.addActionListener((point) ->  {
            if (editingState.isSelected()) {
                editingState.setSelected(false);
            }
            controller.saveAsData();
        });
        play.setSize(30, 30);
        editingState.addItemListener(e -> {
            int state = e.getStateChange();
            switch (state) {
                case (ItemEvent.SELECTED) -> {
                    cellSelector.setEnabled(true);
                    controller.pause();
                    editingState.setText("Disable Editing Mode");
                    disableTimeButtons();
                    controller.setEditing(true);
                    break;
                }
                case (ItemEvent.DESELECTED) -> {
                    cellSelector.setEnabled(false);
                    editingState.setText("Enable Editing Mode");
                    controller.setEditing(false);
                    enablePlayButton();
                }
            }
        });

        //Generate a jcombo box containing all the allowed editable cells. The map exists as a way to grab the actual cell from the user representation, which is their names.
        cellSelector = new JComboBox<>(controller.getMapNameToCell().keySet().toArray(new String[0]));
        cellSelector.addActionListener(e -> {
            controller.setSelectedCell(controller.getMapNameToCell().get(cellSelector.getItemAt(cellSelector.getSelectedIndex())));
        });
        cellSelector.setEnabled(false);



        //Setting the simulation buttons position. it's messy
        //For each element, decrease by one y position to stack them
        //apart from the time buttons & selection elements
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
        c.gridx = 0;
        c.gridy = 3;
        c.ipady = 20;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        c.weightx = 0;
        buttonPanel.add(editingState, c);
        userPanel.add(buttonPanel);
        c.gridx = 0;
        c.gridy = 4;
        c.ipady = 10;
        buttonPanel.add(new JLabel("Select Cell to set: "), c);
        userPanel.add(buttonPanel);
        c.gridx = 1;
        c.gridy = 4;
        c.ipady = 10;
        buttonPanel.add(cellSelector, c);
        userPanel.add(buttonPanel);
        revalidate();
    }

    private void setupSimulationOutput() {
        simulationTextInfo = new JTextArea(null, null, 10, 10);
        JScrollPane simulationOutput = new JScrollPane(simulationTextInfo);
        simulationOutput.setAutoscrolls(true);
        simulationTextInfo.setAutoscrolls(true);
        simulationOutput.setPreferredSize(new Dimension(400, 300));
        simulationTextInfo.setEditable(false);
        this.add(simulationOutput);
    }


    /**
     * Disables both the play and pause buttons to prevent user interaction.
     */
    public void disableTimeButtons() {
        play.setEnabled(false);
        pause.setEnabled(false);
    }

    /**
     * Enables the play to enable user interaction
     */
    public void enablePlayButton() {
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

