package org.basmat.cell.data;

public class NutrientCell extends CellData {
    private SocietyCell owner;
    public NutrientCell(SocietyCell owner) {
        super(ECellType.NUTRIENTS);
        this.owner = owner;
    }

    public NutrientCell() {
        this(null);
    }

    public SocietyCell getOwner() {
        return owner;
    }

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
                "Cell Description: " + getCellType().getCellDescription();
    }
}
