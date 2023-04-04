package org.basmat.map.controller.rules;

import org.basmat.map.model.ModelStructure;
import org.basmat.map.setup.ViewSetup;
import org.basmat.map.util.SimulationProperties;
import org.basmat.map.util.path.Node;
import org.basmat.map.view.UserInteractionUI;
import org.basmat.map.view.ViewStructure;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * RuleApplier applies the gardeners and the winnowers ruleset using a Java Timer, recurring at a set time inverval.
 */

/*
They existed, because they had to exist. They had no antecedent and no constituents, and there is no instrument of causality by which they could be portioned into components and assigned a schematic of their origin.
In the day between the morning and the evening, the gardener and the winnower played a game of possibilities.
 */

/**
 * RuleApplier initializes the Gardener and the Winnower and applies their rules in a sequence,
 * and then re-renders the view with what has changed in the model
  */
public class RuleApplier {
    private final HashMap<Point, LinkedList<Node>> activeSocietyCells;
    private final Winnower winnower;
    private final UserInteractionUI userInteractionUi;
    private Gardener gardener;
    private LinkedList<Point> globalNutrientCellList;
    private LinkedList<Point> globalSocietyCellList;
    private LinkedList<Point> globalLifeCellList;
    private ViewStructure viewStructure;
    private ModelStructure modelStructure;

    private LinkedList<LinkedList<Node>> listOfPaths;

    /**
     *
     * @param viewStructure The view structure to update
     * @param modelStructure The model structure to apply the rules to
     * @param globalSocietyCellList The list of all society cell's coordinates.
     * @param globalLifeCellList The list of all life cell's coordinates
     */
    public RuleApplier(SimulationProperties simulationProperties, UserInteractionUI userInteractionUi, ViewStructure viewStructure, ModelStructure modelStructure, LinkedList<Point> globalSocietyCellList, LinkedList<Point> globalLifeCellList) {
        this.userInteractionUi = userInteractionUi;
        this.viewStructure = viewStructure;
        this.modelStructure = modelStructure;
        this.listOfPaths = new LinkedList<>();
        this.gardener = new Gardener(simulationProperties, userInteractionUi, modelStructure, globalSocietyCellList, globalLifeCellList, listOfPaths);
        this.winnower = new Winnower(simulationProperties, userInteractionUi, modelStructure, globalSocietyCellList, globalLifeCellList, listOfPaths);
        this.globalSocietyCellList = globalSocietyCellList;
        this.globalLifeCellList = globalLifeCellList;
        activeSocietyCells = new HashMap<>();
    }

    public void invokeGardener(){
        //new java.util.Timer().scheduleAtFixedRate(new TimerTask() {
        //    @Override
        //    public void run() {
        gardener.checkForValidReproduction();
        gardener.reproduce();
        gardener.scatter();
        gardener.expand();
        gardener.unison();
        winnower.overcrowded();
        winnower.collapse();
        winnower.famine();
        winnower.stagnation();
        ViewSetup.setupView(viewStructure, modelStructure, ViewSetup.IS_LAZY);
        userInteractionUi.incrementTimeStep();
        //ViewSetup.revalidate(viewStructure);
        //    }
        //}, 0, 750);

    }

    private void applyRulesToCells() {
        gardener.unison();
    }
}
