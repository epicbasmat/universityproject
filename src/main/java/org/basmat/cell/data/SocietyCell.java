package org.basmat.cell.data;

import java.util.HashSet;
import java.util.UUID;


public class SocietyCell extends CellData {

    private String societyName;
    private HashSet<NutrientCell> nutrientCells;
    private HashSet<WorldCell> worldCells;


    public SocietyCell(String name) {
        super(ECellType.SOCIETYBLOCK);
        societyName = UUID.randomUUID().toString();
        this.societyName = name;
        nutrientCells = new HashSet<>();
    }

    public void addNutrientCells(NutrientCell nutrientCell) {
        nutrientCells.add(nutrientCell);
    }

    public void addWorldCell(WorldCell worldCell) {
        worldCells.add(worldCell);
    }

    @Override
    public String toString() {
        return "Name: " + societyName;
    }
}
