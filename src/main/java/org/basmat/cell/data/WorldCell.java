package org.basmat.cell.data;

/**
 * WorldCell provides the foundation for terrain-type ECellTypes. Extends CellData
 * @see ECellType
 * @see AbstractCell
 */
public class WorldCell extends AbstractCell {

    private SocietyCell owner;

    /**
     * @param cellType the type of cell to be set
     *
     */
    public WorldCell(ECellType cellType, SocietyCell owner) {
        super(cellType);
        this.owner = owner;
    }

    public WorldCell(ECellType cellType) {
        this(cellType, null);
    }

    public void setOwner(SocietyCell owner) {
        this.owner = owner;
    }

    public SocietyCell getOwner() {return this.owner;}
    @Override
    public String toString() {
        String ownerString;
        if (this.owner == null) {
            ownerString = "Owner: This cell has no owner";
        }  else {
            ownerString = "Owner: " + this.getOwner().getName();
        }
        return ownerString + "\n" +
                "Cell Name: " + getCellType().getCellName() + "\n" +
                "Cell Description: " + getCellType().getCellDescription() + "\n" +
                "Is habitable: " + getCellType().isHabitable();
    }

}
