package org.basmat.map.model.cells;

import org.basmat.map.model.cells.factory.AbstractSerializableCell;
import org.basmat.map.model.cells.factory.IOwnedCell;
import org.basmat.map.util.ECellType;

import java.awt.image.BufferedImage;

/**
 * WorldCell provides the foundation for terrain-type ECellTypes. Extends CellData
 * @see ECellType
 */
public class WorldCell extends AbstractSerializableCell implements IOwnedCell {
    private SocietyCell owner;
    private final ECellType cellType;


    /**
     * Instantiates a WorldCell with the associated CellType and an owner, if it has one.
     * @param cellType The cell type that the WorldCell can be
     * @param texture The referential texture of a WorldCell, ideally matching the passed cellType
     */
    public WorldCell(ECellType cellType, BufferedImage texture) {
        super(texture);
        this.cellType = cellType;
        this.owner = null;
    }

    @Override
    public void setOwner(SocietyCell owner) {
        this.owner = owner;
    }

    @Override
    public SocietyCell getOwner() {return this.owner;}

    @Override
    public String toString() {
        String ownerString;

        ownerString = this.owner == null ? "Owner: This cell has no owner" : "Owner: " + this.getOwner().getName();

        return ownerString + "\n" +
                "Cell Name: " + getECellType().getCellName() + "\n" +
                "Cell Description: " + getECellType().getCellDescription() + "\n" +
                "Is habitable: " + getECellType().isHabitable();
    }

    @Override
    public ECellType getECellType() {
        return cellType;
    }

}
