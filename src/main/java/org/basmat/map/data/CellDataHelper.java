package org.basmat.map.data;


import org.basmat.map.cellfactory.CellFactory;
import org.basmat.map.cellfactory.cells.SocietyCell;
import org.basmat.map.controller.MVBinder;
import org.basmat.map.util.ECellType;
import org.basmat.map.view.CellMatrixPanel;
import org.basmat.map.view.CellPanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * CellDataHelper provides assistance methods in generating a record that holds the cell, it's id and the point at which it exists, as well as methods for setting and overwriting cells in a matrix
 */
public class CellDataHelper {
    private final CellMatrixPanel cellMatrixPanel;
    private final HashMap<ECellType, BufferedImage> imageCache;
    private final CellFactory cellFactory;

    /**
     *
     * @param cellMatrixPanel
     * @param imageCache
     */
    public CellDataHelper(CellMatrixPanel cellMatrixPanel, HashMap<ECellType, BufferedImage> imageCache) {
        this.cellMatrixPanel = cellMatrixPanel;
        this.imageCache = imageCache;
        this.cellFactory = new CellFactory();
    }

    public MVBinder<?> generateNutrientBinder(SocietyCell owner, int id, Point point) {
        return new MVBinder<>(cellFactory.createNutrientCell(owner, id), new CellPanel(imageCache.get(ECellType.NUTRIENTS), id), point);
    }

    public MVBinder<?> generateSocietyBinder(String name, int id, int radius, Point point, int tint) {
        return new MVBinder<>(cellFactory.createSocietyCell(name, id, radius, tint), new CellPanel(imageCache.get(ECellType.SOCIETY_CELL), id), point);
    }

    public MVBinder<?> generateLifeBinder(SocietyCell societyCell, int id, Point point) {
        return new MVBinder<>(cellFactory.createLifeCell(societyCell, id), new CellPanel(imageCache.get(ECellType.LIFE_CELL), id), point);

    }

    /**
     * @param worldCell The worldcell to instantiate
     * @param id The id that the worldcell inherits. Must be the same as the ID for the global map of ID -> MVBinder
     * @param point The point that the worldcell is at
     * @return The newly instantiated MVBinder with type WorldCell
     */
    public MVBinder<?> generateWorldBinder(ECellType worldCell, int id, Point point) {
        return new MVBinder<>(cellFactory.createWorldCell(worldCell, null, id), new CellPanel(imageCache.get(worldCell), id), point);
    }

    /**
     *
     * @param set The MVBinder to set
     * @param remove The MVBinder to remove
     * @return
     */
    public MVBinder<?> overwriteCellData(MVBinder<?> set, MVBinder<?> remove) {
        cellMatrixPanel.removeCell(remove.view());
        setCellData(set);
        return set;
    }

    /**
     * Sets the cell view to the given point in the cell matrix panel
     * @param mvBinder
     * @return
     */
    public MVBinder<?> setCellData(MVBinder<?> mvBinder) {
        cellMatrixPanel.addCellPanel(mvBinder.view(), mvBinder.point());
        return mvBinder;
    }

}
