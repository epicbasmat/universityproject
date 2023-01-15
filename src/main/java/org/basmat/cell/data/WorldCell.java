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
        if (this.owner == null) {
            return "Owner: " + "this cell has no owner" + "\n" +
                    "Cell Name: " + getCellType().getCellName() + "\n" +
                    "Cell Description: " + getCellType().getCellDescription() + "\n" +
                    "Is habitable: " + getCellType().isHabitable();
        }  else {
            return "Owner: " + this.owner.getName() + "\n" +
                    "Cell Name: " + getCellType().getCellName() + "\n" +
                    "Cell Description: " + getCellType().getCellDescription() + "\n" +
                    "Is habitable: " + getCellType().isHabitable();
        }
    }

}
