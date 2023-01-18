package org.basmat.cell.data;

import java.util.HashSet;

/**
 * SocietyCell provides a foundation where any life cells will get their data from.
 */
public class SocietyCell extends AbstractCell {

    private String societyName;
    private HashSet<NutrientCell> nutrientCells;

    /**
     *
     * @param name The name of the society.
     */
    public SocietyCell(String name, ECellType cellType) {
        super(cellType);
        this.societyName = name;
        nutrientCells = new HashSet<>();
    }

    public HashSet<NutrientCell> getNutrientHashSet() { return this.nutrientCells;}
    public boolean getNutrientCell(NutrientCell nutrientCell) { return nutrientCells.contains(nutrientCell);}
    public void addNutrientCells(NutrientCell nutrientCell) {
        nutrientCells.add(nutrientCell);
    }

    public String getName() {
        return this.societyName;
    }
    @Override
    public String toString() {
        return "Society Name: " + societyName + "\n" +
                "Cell Name: " + getCellType().getCellName() + "\n" +
                "Cell Description: " + getCellType().getCellDescription();

    }
}
