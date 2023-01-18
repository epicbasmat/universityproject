package org.basmat.cell.data;

import org.basmat.cell.view.CellMatrixPanel;
import org.basmat.cell.view.CellPanel;

import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * CellDataHelper provides self-referencial methods for easy addition and removing of data from the view and model at the same time.
 */
public class CellDataHelper {
    private ECellType cellType;
    private int x;
    private int y;
    private CellMatrixPanel cellMatrixPanel;
    private HashMap<ECellType, BufferedImage> imageCache;
    private HashMap<CellPanel, ? super AbstractCell> dataMap;
    private CellPanel currentCellPanel;


    /**
     *
     * @param cellType The type of cell to create
     * @param x the X coordinate of the cell to manipulate
     * @param y the Y coordinate of the cell to manipulatre
     * @param cellMatrixPanel the CellMatrixPanel that contains all child CellPanels
     * @param imageCache The cache of BufferedImages used when setting textures
     * @param dataMap The HashMap containing all known maps from a CellPanel to a data cell.
     */
    public CellDataHelper(ECellType cellType, int x, int y, CellMatrixPanel cellMatrixPanel, HashMap<ECellType, BufferedImage> imageCache, HashMap<CellPanel, ? super AbstractCell> dataMap) {
        this.cellType = cellType;
        this.x = x;
        this.y = y;
        this.cellMatrixPanel = cellMatrixPanel;
        this.imageCache = imageCache;
        this.dataMap = dataMap;
    }

    /**
     * Intermediate operation - removes a cell from the CellPanelMatrix with the coordinates provided from class initialization
     * @return this
     */
    public CellDataHelper deleteCellData() {
        cellMatrixPanel.removeCell(cellMatrixPanel.getPanel(x, y));
        return this;
    }

    /**
     * Intermediate operation - creates a new CellPanel with the data provided with class initialization
     * @return
     */
    public CellDataHelper setCellData() {
        currentCellPanel = new CellPanel(imageCache.get(cellType));
        cellMatrixPanel.addCellPanel(currentCellPanel, x, y);
        return this;
    }

    /**
     * Terminal operation - Used to instantiate a class of type WorldCell
     * @see WorldCell
     */
    public void mapWorldCellToView() {
        dataMap.put(currentCellPanel, new WorldCell(cellType));
    }

    /**
     * Terminal operation - Used to instantiate a new class of type NutrientCell
     * @param owner The owner of the nutrient cell, if any are needed
     * @see NutrientCell
     */
    public void mapNutrientCellToView(SocietyCell owner) {
        dataMap.put(currentCellPanel, new NutrientCell(cellType));
    }

    /**
     * Terminal operation - Used to instantiate a new class of type SocietyCell
     * @param name The name of the society
     * @see SocietyCell
     */
    public void mapSocietyCellToView(String name) {
        dataMap.put(currentCellPanel, new SocietyCell(name, cellType));
    }

}
