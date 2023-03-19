package org.basmat.map.model.cells;

import org.basmat.map.model.cells.factory.IMapCell;
import org.basmat.map.util.ECellType;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

/**
 * SocietyCell provides a foundation where any life cells will get their data from.
 * This class implements IMapCell
 * @see IMapCell
 */
public class SocietyCell implements IMapCell {

    private String societyName;
    private int radius;
    private int tint;
    private BufferedImage texture;
    private LinkedList<NutrientCell> nutrientCells;
    private LinkedList<Point> lifeCells;


    /**
     *
     * @param name The name of the society cell, used to distinguish other society cells
     * @param radius The radius, or area of effect, that the society cell has.
     * @param tint The tint that tints the area of effect for visual clarity.
     * @param texture The referential texture of the society cell
     */
    public SocietyCell(String name, int radius, int tint, BufferedImage texture) {
        this.societyName = name;
        this.radius = radius;
        this.tint = tint;
        this.texture = texture;
        nutrientCells = new LinkedList<>();
        lifeCells = new LinkedList<>();
    }

    public void addNutrientCells(NutrientCell nutrientCell) {
        nutrientCells.add(nutrientCell);
    }
    public LinkedList<Point> getLifeCells() {
        return lifeCells;
    }

    public void addLifeCells(Point lifeCellCoordinates) {
        lifeCells.add(lifeCellCoordinates);
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
    public int getTint() {
        return tint;
    }
    public String getName() {
        return this.societyName;
    }

    public int getRadius() {
        return radius;
    }

    @Override
    public BufferedImage getTexture() {
        return texture;
    }

    @Override
    public String toString() {
        return "Society Name: " + societyName + "\n" +
                "Cell Name: " + getECellType().getCellName() + "\n" +
                "Cell Description: " + getECellType().getCellDescription() + "\n" +
                "Owned nutrient cells: " + nutrientCells.size() + "\n" +
                "Capacity of nutrient cells: " + getNutrientCapacity() + "\n" +
                "Population: " + getSize();

    }

    public int getSize() {
        return lifeCells.size();
    }

    public int getNutrientCapacity() {
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
}
