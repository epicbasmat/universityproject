package org.basmat.cell.data;

public class NutrientCell extends CellData {
    private SocietyCell owner;
    public NutrientCell(SocietyCell owner) {
        super(ECellType.NUTRIENTS);
        this.owner = owner;
    }

    public SocietyCell getOwner() {
        return owner;
    }
}
