package org.basmat.map.cellfactory.cells.rules;

import org.basmat.map.cellfactory.cells.LifeCell;
import org.basmat.map.cellfactory.cells.NutrientCell;
import org.basmat.map.cellfactory.cells.SocietyCell;

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
    private LinkedList<NutrientCell> globalNutrientCellList;
    private LinkedList<SocietyCell> globalSocietyCellList;
    private LinkedList<LifeCell> globalLifeCellList;

    public RuleApplier(LinkedList<NutrientCell> globalNutrientCellList, LinkedList<SocietyCell> globalSocietyCellList, LinkedList<LifeCell> globalLifeCellList) {
        this.globalNutrientCellList = globalNutrientCellList;
        this.globalSocietyCellList = globalSocietyCellList;
        this.globalLifeCellList = globalLifeCellList;
        new java.util.Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                applyRulesToCells();
            }
        }, 0, 4000);
    }

    private void applyRulesToCells() {
    }
}
