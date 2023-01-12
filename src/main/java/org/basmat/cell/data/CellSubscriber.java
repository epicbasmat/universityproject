package org.basmat.cell.data;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;

public class CellSubscriber {
    private LinkedList<NutrientCell> globalNutrientCells;
    private LinkedList<SocietyCell> globalSocietyCells;

    public CellSubscriber() {
        globalNutrientCells = new LinkedList<>();
        globalSocietyCells = new LinkedList<>();
    }

    public void addToGlobalNutrientCells(NutrientCell nutrientCell) {
        globalNutrientCells.add(nutrientCell);
    }

    public void addToGlobalSocietyCells(SocietyCell societyCell) {
        globalSocietyCells.add(societyCell);
    }

    public LinkedList<NutrientCell> getGlobalNutrientCells () {
        return this.globalNutrientCells;
    }

    public LinkedList<SocietyCell> getGlobalSocietyCells() {
        return globalSocietyCells;
    }
}
