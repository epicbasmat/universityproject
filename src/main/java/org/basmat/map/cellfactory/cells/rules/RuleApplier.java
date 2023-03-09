package org.basmat.map.cellfactory.cells.rules;

import org.basmat.map.cellfactory.cells.LifeCell;
import org.basmat.map.cellfactory.cells.NutrientCell;
import org.basmat.map.cellfactory.cells.SocietyCell;

import java.util.LinkedList;
import java.util.TimerTask;

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
        }, 0, 1000);
    }

    private void applyRulesToCells() {
    }
}
