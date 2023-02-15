package org.basmat.map.cellfactory.cells;

import org.basmat.map.cellfactory.IMapCell;
import org.basmat.map.util.ECellType;

import java.util.HashSet;
import java.util.LinkedList;

/**
 * SocietyCell provides a foundation where any life cells will get their data from.
 * This class is a concrete implementation of IMapCell
 * @see IMapCell
 */
public class SocietyCell implements IMapCell {

    private String societyName;
    private int id;
    private int radius;
    private LinkedList<NutrientCell> nutrientCells;
    private LinkedList<LifeCell> lifeCells;

    /**
     * Constructs a society class with the name and the determined celltype, usually ECellType.SOCIETYCELL.
     * @param name The name of the society.
     */
    public SocietyCell(String name, int id, int radius) {
        this.societyName = name;
        this.id = id;
        this.radius = radius;
        nutrientCells = new LinkedList<>();
        lifeCells = new LinkedList<>();
    }

    public void addNutrientCells(NutrientCell nutrientCell) {
        nutrientCells.add(nutrientCell);
    }

    public void addLifeCells(LifeCell lifeCell) {
        lifeCells.add(lifeCell);
        incrementSupportedCount();
    }

    private void incrementSupportedCount() {
        for (NutrientCell e : nutrientCells) {
            if (e.getSupportingCount() < e.getCapacity()) {
                e.incrementSupportingCount();
                break;
            }
        }
    }
    public String getName() {
        return this.societyName;
    }

    public int getRadius() {
        return radius;
    }

    @Override
    public String toString() {
        return "Society Name: " + societyName + "\n" +
                "Cell Name: " + getECellType().getCellName() + "\n" +
                "Cell Description: " + getECellType().getCellDescription() + "\n" +
                "Owned nutrient cells: " + nutrientCells.size() + "\n" +
                "Capacity of nutrient cells: " + getCapacity() + "\n" +
                "Population: " + lifeCells.size();

    }

    public int getCapacity() {
        int capacity = 0;
        for (NutrientCell e : nutrientCells) {
            //TODO: Cache result to prevent constant O(n) calls
            capacity = capacity + e.getCapacity();
        }
        return capacity;
    }

    @Override
    public ECellType getECellType() {
        return ECellType.SOCIETY_CELL;
    }

    @Override
    public int getId() {
        return id;
    }
}
