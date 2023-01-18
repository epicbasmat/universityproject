package org.basmat.cell.controller;

import org.basmat.cell.data.CellData;
import org.basmat.cell.data.ECellType;
import org.basmat.cell.view.CellMatrixPanel;
import org.basmat.cell.view.CellPanel;

import javax.swing.*;
import java.awt.image.BufferedImage;

/**
 * CellController provides the Controller, which manages the model and view, of a particular cell.
 * @param <ChildCell>
 */
public class CellController <ChildCell extends CellData>{
    private BufferedImage texture;
    private ChildCell childCell;
    private CellPanel cellView;

    /**
     * Instantiates the controller class with the associated model and generates a view.
     * @param childCell The cell to inherit the data from
     * @param texture The reference texture that the cell will have, match childCell's data
     * @param cellMatrixPanel The reference of the instantiated cellMatrixPanel
     * @param x The x coordinate of the cell
     * @param y The y coordinate of the cell
     */
    public CellController (ChildCell childCell, BufferedImage texture, CellMatrixPanel cellMatrixPanel, int x, int y) {
        this.texture = texture;
        this.childCell = childCell;
        this.cellView = new CellPanel(texture, this);
        cellMatrixPanel.addCellPanel(cellView, x, y);
    }

    public CellPanel getView() {
        return this.cellView;
    }
    public String getStringFromModel() { return childCell.toString(); };
    public ECellType getCellTypeFromModel() { return childCell.getCellType(); };
    public void tellViewToTint() {
        //ff9fcc9f
        cellView.setTint(0x0090cc90);

    };
    public ChildCell getChildCell() {
        return childCell;
    }
}
