package org.basmat.cell.data;

public class NutrientCell extends AbstractCell {
    private SocietyCell owner;

    /**
     *
     * @param owner The owner of the nutrient cell
     * @param cellType The cellType of nutreint cell,
     */
    public NutrientCell(SocietyCell owner, ECellType cellType) {
        super(cellType);
        this.owner = owner;
    }

    public NutrientCell(SocietyCell cell) {
        this(cell, ECellType.NUTRIENTS);
    }



    public NutrientCell() {
        this( null, ECellType.NUTRIENTS);
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
