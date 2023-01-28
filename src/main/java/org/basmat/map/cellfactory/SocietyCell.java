package org.basmat.map.cellfactory;

import org.basmat.map.util.ECellType;

import java.awt.*;
import java.util.HashSet;

/**
 * SocietyCell provides a foundation where any life cells will get their data from.
 * This class is a concrete implementation of IMapCell
 * @see IMapCell
 */
class SocietyCell implements IMapCell {

    private String societyName;
    private HashSet<NutrientCell> nutrientCells;

    /**
     * Constructs a society class with the name and the determined celltype, usually ECellType.SOCIETYCELL.
     * @param name The name of the society.
     */
    public SocietyCell(String name) {
        this.societyName = name;
        nutrientCells = new HashSet<>();
    }
    public void addNutrientCells(NutrientCell nutrientCell) {
        nutrientCells.add(nutrientCell);
    }

    public String getName() {
        return this.societyName;
    }

    @Override
    public String toString() {
        return "Society Name: " + societyName + "\n" +
                "Cell Name: " + getECellType().getCellName() + "\n" +
                "Cell Description: " + getECellType().getCellDescription() + "\n" +
                "Owned nutrient cells: " + nutrientCells.size();

    }

    @Override
    public ECellType getECellType() {
        return ECellType.SOCIETY_CELL;
    }
}
