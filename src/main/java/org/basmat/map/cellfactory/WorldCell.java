package org.basmat.map.cellfactory;

import org.basmat.map.util.ECellType;

import javax.annotation.Nullable;
import java.awt.*;

/**
 * WorldCell provides the foundation for terrain-type ECellTypes. Extends CellData
 * @see ECellType
 */
public class WorldCell implements IOwnedCell {
    private SocietyCell owner;
    private int id;
    private ECellType cellType;

    /**
     * @param cellType The ECellType of the WorldCell
     * @param owner The owner of the WorldCell
     */
    public WorldCell(ECellType cellType, @Nullable SocietyCell owner, int id) {
        this.cellType = cellType;
        this.owner = owner;
        this.id = id;
    }

    /**
     * @param cellType The ECellType of the WorldCell
     */
    public WorldCell(ECellType cellType, int id) {
        this(cellType, null, id);
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

    @Override
    public int getId() {
        return id;
    }
}
