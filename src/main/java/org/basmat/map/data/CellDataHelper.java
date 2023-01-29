package org.basmat.map.data;


import org.basmat.map.cellfactory.CellFactory;
import org.basmat.map.cellfactory.NutrientCell;
import org.basmat.map.cellfactory.SocietyCell;
import org.basmat.map.cellfactory.WorldCell;
import org.basmat.map.controller.MVBinder;
import org.basmat.map.util.ECellType;
import org.basmat.map.view.CellMatrixPanel;
import org.basmat.map.view.CellPanel;
import org.checkerframework.checker.units.qual.C;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

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

    public MVBinder<?> generateSocietyBinder(String name, int id, Point point) {
        return new MVBinder<>(cellFactory.createSocietyCell(name, id), new CellPanel(imageCache.get(ECellType.SOCIETY_CELL), id), point);
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

    public MVBinder<?> overwriteCellData(MVBinder<?> mvBinder) {
        cellMatrixPanel.removeCell(mvBinder.view());
        setCellData(mvBinder);
        return mvBinder;
    }

    public MVBinder<?> setCellData(MVBinder<?> mvBinder) {
        cellMatrixPanel.addCellPanel(mvBinder.view(), mvBinder.point());
        return mvBinder;
    }

}
