package org.basmat.cell;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.UUID;


public class SocietyCell extends CellData implements ICell{

    private String societyName;
    private HashSet<NutrientCell> nutrientCells;
    private HashSet<WorldCell> worldCells;


    SocietyCell(BufferedImage texture) throws IOException {
        super(ECellType.SOCIETYBLOCK, texture);
        societyName = UUID.randomUUID().toString();
        nutrientCells = new HashSet<>();
    }

    public void addNutrientCells(NutrientCell nutrientCell) {
        nutrientCells.add(nutrientCell);
    }

    public void addWorldCell(WorldCell worldCell) {
        worldCells.add(worldCell);
    }
}
