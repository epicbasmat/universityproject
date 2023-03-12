package org.basmat.map.cellfactory.cells;

import org.basmat.map.cellfactory.IMapCell;
import org.basmat.map.util.ECellType;


/**
 * LifeCell provides the model of a life cell and contains the relevent data to
 */
public class LifeCell implements IMapCell {
    private SocietyCell societyCell;
    private int id;
    private int attrition;


    /**
     *
     * @param id The id of the cell. Must match that of the associated view
     * @param societyCell The society cell that the life cell is associated with.
     */
    public LifeCell(SocietyCell societyCell, int id) {
        this.id = id;
        this.societyCell = societyCell;
        this.attrition = 0;
    }

    @Override
    public ECellType getECellType() {
        return ECellType.LIFE_CELL;
    }

    @Override
    public int getId() {
        return this.id;
    }

    public void incrementAttrition() {
        attrition++;
    }

    @Override
    public String toString() {
        return "Life cell lives at: " + this.societyCell.getName() + "\n" +
                "Attrition level: " + this.attrition;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LifeCell lifeCell) {
            return lifeCell.getId() == lifeCell.getId();
        } else return false;
    }
}
