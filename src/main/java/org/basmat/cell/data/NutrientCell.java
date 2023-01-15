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
        if (getOwner() == null) {
            return "Owner: " + "This cell has no owner" + "\n" +
                    "Cell Name: " + getCellType().getCellName() + "\n" +
                    "Cell Description: " + getCellType().getCellDescription();
        } else {
            return "Owner: " + getOwner().getName() + "\n" +
                    "Cell Name: " + getCellType().getCellName() + "\n" +
                    "Cell Description: " + getCellType().getCellDescription();
        }
    }
}
