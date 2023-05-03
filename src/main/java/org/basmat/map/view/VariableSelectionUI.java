package org.basmat.map.view;

import org.basmat.map.controller.Controller;
import org.basmat.map.util.SimulationProperties;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

/**
 * VariableSelectionUI contains all the elements associated with the main menu and variable selection. This menu deals with variable selection from the user, and also loading a file.
 */
public class VariableSelectionUI extends JPanel {

    private final JSpinner societyCount;
    private final JSpinner nutrientCount;
    private final JSpinner initialNutrientCount;
    private final JSpinner attritionThreshold;
    private final JSpinner overcrowdThreshold;
    private final JSpinner ratioThreshold;
    private final JSpinner foodThreshold;
    private final LinkedList<JSpinner> spinnerList;
    private final Button confirmSystemVariables;

    private int maxSize;

    public VariableSelectionUI(Controller controller) {
        spinnerList = new LinkedList<>();
        GridBagConstraints c = new GridBagConstraints();
        this.setLayout(new GridBagLayout());
        //Setup the spinners. each spinner has a counterpart label which explains what the spinner changes to the user.
        societyCount = new JSpinner(new SpinnerNumberModel(7, 1, 14, 1));
        setSystemVariable(c, "Society count", societyCount);
        nutrientCount = new JSpinner(new SpinnerNumberModel(100, 5, 300, 5));
        setSystemVariable(c, "Nutrient Count", nutrientCount);
        initialNutrientCount = new JSpinner(new SpinnerNumberModel(1, 0, 4, 1));
        setSystemVariable(c, "Minimum Nutrient Cells per Society", initialNutrientCount);
        attritionThreshold = new JSpinner(new SpinnerNumberModel(20, 1, 40, 2));
        setSystemVariable(c, "Time steps before attrition kill", attritionThreshold);
        overcrowdThreshold = new JSpinner(new SpinnerNumberModel(5, 1, 8, 1));
        setSystemVariable(c, "Overcrowd threshold", overcrowdThreshold);
        ratioThreshold = new JSpinner(new SpinnerNumberModel(55, 10, 100, 5));
        setSystemVariable(c, "Ratio of land to life cell before collapsing", ratioThreshold);
        foodThreshold = new JSpinner(new SpinnerNumberModel(0.6, 0.1, 1, 0.1));
        setSystemVariable(c, "Ratio of food to life cell before a death occurs", foodThreshold);
        confirmSystemVariables = new Button("Generate new simulation");
        confirmSystemVariables.addActionListener(e ->
                controller.constructSimulation(new SimulationProperties(
                    (Integer) societyCount.getValue(),
                    (Integer) nutrientCount.getValue(),
                    (Integer) initialNutrientCount.getValue(),
                    (Integer) attritionThreshold.getValue(),
                    (Integer) overcrowdThreshold.getValue(),
                    (Integer) ratioThreshold.getValue(),
                    (Double) foodThreshold.getValue()
        )));
        c.gridy = 8;
        c.gridx = 0;
        c.ipady = 20;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 3;
        c.weightx = 0;
        c.anchor = GridBagConstraints.FIRST_LINE_END;
        this.add(confirmSystemVariables, c);
        c.gridy = 9;
        Button loadFile = new Button("Alternatively, load from a .dat file");
        loadFile.addActionListener((actionListener) -> {
             JFileChooser jFileChooser = new JFileChooser("./");
             int i = jFileChooser.showOpenDialog(this);
             if (i == JFileChooser.APPROVE_OPTION) {
                 controller.loadFromFile(jFileChooser.getSelectedFile());
             }
        });
        this.add(loadFile, c);
    }

    void setEnabledUI(boolean enabled) {
        spinnerList.forEach(e -> e.setEnabled(enabled));
        confirmSystemVariables.setEnabled(enabled);
    }

    /**
     * Sets a JLabel and Spinner to a specific area in the GridBagLayout, defined by the passed GridBagConstraints.
     * @param c The GridBagConstraints to provide the layout with. Note that this will automatically assign the x,y of the components so there is no need to change it past initialization
     * @param label The label of the spinner
     * @param spinner The spinner for the user
     */
    void setSystemVariable(GridBagConstraints c, String label, JSpinner spinner) {
        //for a given grid bag constraint c, we want to go down one to set the label
        spinner.setMinimumSize(new Dimension(170, 20));
        c.gridy++;
        c.anchor = GridBagConstraints.LINE_START;
        JLabel comp = new JLabel(label);
        comp.getInsets().set(100, 100, 100 ,100);
        this.add(comp, c);
        // and then go across one to set the spinner, this gives the user a visual correlation between the description of the spinner, and the spinner
        c.gridx = + 1;
        c.anchor = GridBagConstraints.LINE_END;
        this.add(spinner, c);
        // and then reset the x position, but keep the y position. this means when the next call occurs, no overlapping of elements happens
        c.gridx =-1;
    }
}
