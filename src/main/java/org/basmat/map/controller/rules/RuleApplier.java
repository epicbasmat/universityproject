package org.basmat.map.controller.rules;

import org.basmat.map.controller.Controller;
import org.basmat.map.model.ModelStructure;
import org.basmat.map.util.path.Node;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * RuleApplier unvokes the gardener and the winnower in a set order.
 */

/*
They existed, because they had to exist. They had no antecedent and no constituents, and there is no instrument of causality by which they could be portioned into components and assigned a schematic of their origin.
In the day between the morning and the evening, the gardener and the winnower played a game of possibilities.
 */

/**
 * RuleApplier initializes the Gardener and the Winnower and applies their rules in a sequence
  */
public class RuleApplier {
    private final Winnower winnower;
    private final Gardener gardener;

    /**
     *
     * @param modelStructure The model structure to apply the rules to
     * @param globalSocietyCellList The list of all society cell's coordinates.
     * @param globalLifeCellList The list of all life cell's coordinates
     */
    public RuleApplier(Controller controller, ModelStructure modelStructure, LinkedList<Point> globalSocietyCellList, ArrayList<Point> globalLifeCellList) {
        ArrayList<LinkedList<Node>> listOfPaths = new ArrayList<>();
        this.gardener = new Gardener(controller, modelStructure, globalSocietyCellList, globalLifeCellList, listOfPaths);
        this.winnower = new Winnower(controller, modelStructure, globalSocietyCellList, globalLifeCellList, listOfPaths);
    }

    public void invokeRules(){
        gardener.checkForValidReproduction();
        gardener.reproduce();
        gardener.scatter();
        gardener.expand();
        gardener.unison();
        winnower.overcrowded();
        winnower.collapse();
        winnower.famine();
        winnower.loneliness();
    }
}