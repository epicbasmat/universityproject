package org.basmat.map.view;

import org.basmat.map.controller.CellMatrixController;
import org.basmat.map.util.SimulationProperties;

import javax.swing.*;
import java.awt.*;

public class MenuUI extends JPanel {

    private final JSpinner societyCount;
    private final JSpinner nutrientCount;
    private final JSpinner initialNutrientCount;
    private final JSpinner attritionThreshold;
    private final JSpinner overcrowdThreshold;
    private final JSpinner ratioThreshold;
    private final JSpinner foodThreshold;

    public MenuUI(CellMatrixController cellMatrixController) {
        this.setLayout(new BorderLayout());
        JPanel buttonBox = new JPanel();
        buttonBox.setLayout(new BoxLayout(buttonBox, BoxLayout.Y_AXIS));
        societyCount = new JSpinner(new SpinnerNumberModel(7, 1, 14, 1));
        buttonBox.add(new UserInput("Society Count", societyCount));
        nutrientCount = new JSpinner(new SpinnerNumberModel(100, 5, 300, 5));
        buttonBox.add(new UserInput("Nutrient Count", nutrientCount));
        initialNutrientCount = new JSpinner(new SpinnerNumberModel(1, 0, 4, 1));
        buttonBox.add(new UserInput("Initial Nutrient Count per Society", initialNutrientCount));
        attritionThreshold = new JSpinner(new SpinnerNumberModel(20, 1, 40, 2));
        buttonBox.add(new UserInput("Time steps before attrition kill", attritionThreshold));
        overcrowdThreshold = new JSpinner(new SpinnerNumberModel(5, 1, 8, 1));
        buttonBox.add(new UserInput("Overcrowd threshold", overcrowdThreshold));
        ratioThreshold = new JSpinner(new SpinnerNumberModel(75, 25, 150, 5));
        buttonBox.add(new UserInput("Ratio of land to life cell before collapsing", ratioThreshold));
        foodThreshold = new JSpinner(new SpinnerNumberModel(0.6, 0.1, 1, 0.1));
        buttonBox.add(new UserInput("Ratio of food to life cell before a death occurs", foodThreshold));
        this.add(buttonBox, BorderLayout.CENTER);
        Button confirmSystemVariables = new Button("Confirm system variables");
        confirmSystemVariables.addActionListener(e ->
                cellMatrixController.constructSimulation(new SimulationProperties(
                    (Integer) societyCount.getValue(),
                    (Integer) nutrientCount.getValue(),
                    (Integer) initialNutrientCount.getValue(),
                    (Integer) attritionThreshold.getValue(),
                    (Integer) overcrowdThreshold.getValue(),
                    (Integer) ratioThreshold.getValue(),
                    (Double) foodThreshold.getValue()
        )));
        this.add(confirmSystemVariables, BorderLayout.AFTER_LINE_ENDS);
    }
}

class UserInput extends JPanel {
    JLabel systemVariableName;
    JSpinner spinner;

    public UserInput(String systemVariableName, JSpinner spinner) {
        this.systemVariableName = new JLabel(systemVariableName);
        this.spinner = spinner;
        SpringLayout mgr = new SpringLayout();
        this.setLayout(mgr);
        mgr.putConstraint(SpringLayout.WEST, spinner, 5, SpringLayout.EAST, this.systemVariableName);
        mgr.putConstraint(SpringLayout.NORTH, spinner, 0, SpringLayout.NORTH, this.systemVariableName);
        this.add(this.systemVariableName);
        this.add(spinner);
    }
}
