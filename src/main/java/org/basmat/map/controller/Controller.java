package org.basmat.map.controller;


import org.basmat.map.controller.rules.RuleApplier;
import org.basmat.map.model.ModelStructure;
import org.basmat.map.setup.ModelSetup;
import org.basmat.map.setup.ViewSetup;
import org.basmat.map.util.SimulationProperties;
import org.basmat.map.view.VariableSelectionUI;
import org.basmat.map.view.SimulationInteractionUI;
import org.basmat.map.view.SimulationUI;
import org.basmat.userui.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.LinkedList;

public class Controller {

    private int seed;
    private final SimulationUI viewStructure;
    private final ModelStructure modelStructure;
    private final GUI GUI;
    private final Timer timer;
    private RuleApplier ruleApplier;
    private final SimulationInteractionUI userInteractionUi;
    private final VariableSelectionUI variableSelectionUi;
    private final LinkedList<Point> globalSocietyCellList;
    private final LinkedList<Point> globalLifeCellList;
    private SimulationProperties simulationProperties;


    public Controller(int cellMatrixWidth, int cellMatrixHeight) throws InterruptedException {
        globalSocietyCellList = new LinkedList<>();
        globalLifeCellList = new LinkedList<>();
        modelStructure = new ModelStructure();
        viewStructure = new SimulationUI(cellMatrixWidth, cellMatrixHeight, this);
        userInteractionUi = new SimulationInteractionUI(this);
        variableSelectionUi = new VariableSelectionUI(this);
        GUI = new GUI(viewStructure, userInteractionUi, variableSelectionUi);
        ActionListener updateAction = e -> {
            ruleApplier.invokeRules();
            ViewSetup.setupView(viewStructure, modelStructure, ViewSetup.IS_LAZY);
            userInteractionUi.incrementTimeStep();
        };
        timer = new Timer(1000, updateAction);
    }

    public void pause() {
        timer.stop();
    }

    public void play() {
        timer.start();
    }

    public int getSeed() {
        return this.seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    public void constructSimulation(SimulationProperties simulationProperties) {
        this.simulationProperties = simulationProperties;
        ModelSetup modelSetup = new ModelSetup(this, modelStructure, globalSocietyCellList, globalLifeCellList);
        ruleApplier = new RuleApplier(this, modelStructure, globalSocietyCellList, globalLifeCellList);
        try {
            modelSetup.setupMap();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ViewSetup.setupView(viewStructure, modelStructure);
        GUI.nextCard();
    }

    /**
     * Provides a temporary method for viewing data requested by the cell matrix view.
     * @param e the MouseEvent that the cell captures
     */
    public void displayData(Point e) {
        //Weird subtractions are necessary to align click co-ordinate with cell matrix co-ordinate
        pushText(modelStructure.getCoordinate(e).toString() + "\n");
    }

    public SimulationProperties getSimulationProperties() {
        return this.simulationProperties;
    }

    public void pushText(String string) {
        userInteractionUi.appendText(string + "\n");
    }
}