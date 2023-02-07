package org.basmat.map.cellfactory;

import org.basmat.map.util.ECellType;

import javax.annotation.Nullable;

/**
 * This class provides a construct to represent the ECellType "Nutrient Cell" and contains methods to deal with consuming nutrients within the nutrient cell.
 * This class is a concrete implementation of IOwnedCell
 * @see IOwnedCell
 */
public class NutrientCell implements IOwnedCell {

    private int capacity;
    private SocietyCell owner;
    private int id;

    /**
     * @param owner The owner of the nutrient cell, can be null
     */
    public NutrientCell(@Nullable SocietyCell owner, int id) {
        this.owner = owner;
        this.id = id;
        this.capacity = 0;
    }

    public SocietyCell getOwner() {
        return owner;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return this.capacity;
    }

    @Override
    public void setOwner(SocietyCell owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        String ownerString;

        ownerString = this.owner == null ? "Owner: This cell has no owner" : "Owner: " + this.getOwner().getName();

        return ownerString + "\n" +
                "Cell Name: " + getECellType().getCellName() + "\n" +
                "Cell Description: " + getECellType().getCellDescription();
    }

    @Override
    public ECellType getECellType() {
        return ECellType.NUTRIENTS;
    }

    @Override
    public int getId() {
        return id;
    }
}
