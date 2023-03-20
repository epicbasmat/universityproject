package org.basmat.map.controller.rules;

import org.basmat.map.model.ModelStructure;
import org.basmat.map.model.cells.LifeCell;
import org.basmat.map.model.cells.NutrientCell;
import org.basmat.map.model.cells.SocietyCell;
import org.basmat.map.util.path.Node;
import org.basmat.map.view.ViewStructure;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TimerTask;

/**
 * RuleApplier applies the gardeners and the winnowers ruleset using a Java Timer, recurring at a set time inverval.
 */

/*
They existed, because they had to exist. They had no antecedent and no constituents, and there is no instrument of causality by which they could be portioned into components and assigned a schematic of their origin.
In the day between the morning and the evening, the gardener and the winnower played a game of possibilities.
 */
public class RuleApplier {
    private final HashMap<Point, LinkedList<Node>> activeSocietyCells;
    private Gardener gardener;
    private LinkedList<Point> globalNutrientCellList;
    private LinkedList<Point> globalSocietyCellList;
    private LinkedList<Point> globalLifeCellList;
    private ViewStructure viewStructure;
    private ModelStructure modelStructure;

    /**
     *
     * @param viewStructure
     * @param modelStructure
     * @param globalNutrientCellList
     * @param globalSocietyCellList
     * @param globalLifeCellList
     */
    public RuleApplier(ViewStructure viewStructure, ModelStructure modelStructure, LinkedList<Point> globalNutrientCellList, LinkedList<Point> globalSocietyCellList, LinkedList<Point> globalLifeCellList) {
        this.viewStructure = viewStructure;
        this.modelStructure = modelStructure;
        this.gardener = new Gardener(viewStructure, modelStructure, globalNutrientCellList, globalSocietyCellList, globalLifeCellList);
        this.globalNutrientCellList = globalNutrientCellList;
        this.globalSocietyCellList = globalSocietyCellList;
        this.globalLifeCellList = globalLifeCellList;
        activeSocietyCells = new HashMap<>();
        new java.util.Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //applyRulesToCells();
            }
        }, 0, 4000);
    }

    public void invokeGardener(){
        gardener.unison();
    }

    public void gen() {
        gardener.reproduce();
    }

    private void applyRulesToCells() {
        gardener.unison();
    }
}
