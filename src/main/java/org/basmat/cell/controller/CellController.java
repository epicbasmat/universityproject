package org.basmat.cell.controller;

import org.basmat.cell.data.CellData;
import org.basmat.cell.data.ECellType;
import org.basmat.cell.view.CellMatrixPanel;
import org.basmat.cell.view.CellPanel;

import java.awt.image.BufferedImage;

public class CellController <ChildCell extends CellData>{
    private BufferedImage texture;
    private ChildCell childCell;
    private CellPanel cellView;
    public CellController (ChildCell childCell, BufferedImage texture, CellMatrixPanel cellMatrixPanel, int x, int y) {
        //Copy texture to a new BufferedImage as to not manipulate referenced texture
        this.texture = new BufferedImage(texture.getColorModel(), texture.copyData(null), texture.getColorModel().isAlphaPremultiplied(), null);
        this.childCell = childCell;
        this.cellView = new CellPanel(texture, this);
        cellMatrixPanel.addCellPanel(cellView, x, y);
    }

    public String getStringFromModel() { return childCell.toString(); };
    public ECellType getCellTypeFromModel() { return childCell.getCellType(); };
    public void tellViewToTint() { cellView.setTint(0, 1);};
    public ChildCell getChildCell() {
        return childCell;
    }
}
