package org.basmat.cell.data;

import java.util.HashSet;
import java.util.UUID;

/**
 * SocietyCell provides a foundation where any life cells will get their data from.
 */
public class SocietyCell extends CellData {

    private String societyName;
    private HashSet<NutrientCell> nutrientCells;
    private HashSet<WorldCell> worldCells;


    /**
     *
     * @param name The name of the society.
     */
    public SocietyCell(String name) {
        super(ECellType.SOCIETYBLOCK);
        this.societyName = name;
        nutrientCells = new HashSet<>();
    }

    public void addNutrientCells(NutrientCell nutrientCell) {
        nutrientCells.add(nutrientCell);
    }

    public void addWorldCell(WorldCell worldCell) {
        worldCells.add(worldCell);
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
