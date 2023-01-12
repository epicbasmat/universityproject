package org.basmat.cell.data;

public class WorldCell extends CellData{

    private SocietyCell owner;

    /**
     * @param cellType the type of cell to be set
     */
    public WorldCell(ECellType cellType, SocietyCell owner) {
        super(cellType);
        this.owner = owner;
    }

    public void setOwner(SocietyCell owner) {
        this.owner = owner;
    }

    public SocietyCell getOwner() {return this.owner;}
    @Override
    public String toString() {
        return "Owner: " + this.owner + "\n" +
                "Cell Name: " + getCellType().getLocalizedName() + "\n" +
                "Is habitable: " + getCellType().isHabitable();
    }

}
