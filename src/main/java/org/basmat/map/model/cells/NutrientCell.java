package org.basmat.map.model.cells;

import org.basmat.map.model.cells.factory.AbstractSerializableCell;
import org.basmat.map.model.cells.factory.IOwnedCell;
import org.basmat.map.util.ECellType;

import javax.annotation.Nullable;
import java.awt.image.BufferedImage;

/**
 * This class provides a construct to represent the ECellType "Nutrient Cell" and contains methods to deal with consuming nutrients within the nutrient cell.
 * This class is a concrete implementation of IOwnedCell
 * @see IOwnedCell
 */
public class NutrientCell extends AbstractSerializableCell implements IOwnedCell {

    private int capacity;
    private int supporting;
    private SocietyCell owner;

    /**
     * Instantiates a NutrientCell with an owner, if applicable, and it's texture
     * @param owner The owner of the nutrient cell
     * @param texture The reference texture of the nutrient cell.
     */
    public NutrientCell(@Nullable SocietyCell owner, BufferedImage texture) {
        super(texture);
        this.owner = owner;
        this.capacity = (int) (Math.random() * 7 - 1) + 2;
        this.supporting = 0;
    }

    public SocietyCell getOwner() {
        return owner;
    }

    public int getCapacity() {
        return this.capacity;
    }

    @Override
    public void setOwner(SocietyCell owner) {
        this.owner = owner;
    }

    /**
     * This gets the current nutrition cells supported life cell count. Once this hits the maximum capacity this cell allows, it cannot support any more cells
     * @return the currently supported amount of life cells
     */
    public int getSupportingCount() {
        return supporting;
    }

    public void resetSupportingCount() { supporting = 0; }
    /**
     * Increments the amount of nutrient cells that are being supported by one. If the capacity is at maximum, it cannot be incremented.
     */
    public void incrementSupportingCount() {
        if (supporting < capacity) {
            supporting++;
        }
    }

    @Override
    public String toString() {
        String ownerString;

        ownerString = this.owner == null ? "Owner: This cell has no owner" : "Owner: " + this.getOwner().getName();

        return ownerString + "\n" +
                "Cell Name: " + getECellType().getCellName() + "\n" +
                "Cell Description: " + getECellType().getCellDescription() + "\n" +
                "Life support capacity: " + getCapacity() + "\n" +
                "Currently supporting: " + getSupportingCount() + " cells";
    }

    @Override
    public ECellType getECellType() {
        return ECellType.NUTRIENTS;
    }
}
