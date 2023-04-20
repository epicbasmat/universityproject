package org.basmat.map.model.cells;

import org.basmat.map.model.cells.factory.AbstractSerializableCell;
import org.basmat.map.model.cells.factory.IMapCell;
import org.basmat.map.util.ECellType;

import java.awt.*;
import java.awt.image.BufferedImage;


/**
 * This class provides a construct to represent the ECellType LifeCell and contains the methods to control it's internal systems such as reproductive cooldown or attrition
 */
public class LifeCell extends AbstractSerializableCell implements IMapCell {
    private int reproductionCooldown;
    private final Point societyCell;
    private int attrition;


    /**
     * Instantiate a LifeCell with the Society Cell it lives at, alongside its texture.
     * @param societyCell The society cell that the life cell will be apart of
     * @param texture A reference for the life cell texture.
     */
    public LifeCell(Point societyCell, BufferedImage texture) {
        super(texture);
        this.societyCell = societyCell;
        this.attrition = 0;
        this.reproductionCooldown = 0;
    }

    public Point getSocietyCell() {
        return societyCell;
    }

    @Override
    public ECellType getECellType() {
        return ECellType.LIFE_CELL;
    }

    public void incrementAttrition() {
        attrition++;
    }

    public int getAttrition() {
        return this.attrition;
    }

    public void resetAttrition() {
        attrition = 0;
    }

    @Override
    public String toString() {
        String isOnCooldown = getReproductionCooldown() > 0 ? "Is currently on cooldown: " + getReproductionCooldown() : "Is not on reproduction cooldown";
        //TODO: broken return string
        return "Life cell lives at: " + societyCell + "\n" +
                "Attrition level: " + this.attrition + "\n" +
                isOnCooldown;
    }

    public int getReproductionCooldown() {
        return reproductionCooldown;
    }

    public void decrementReproductionCooldown() {
        if (getReproductionCooldown() > 0) {
            reproductionCooldown--;
        }
    }

    public void resetReproductionCooldown() {
        reproductionCooldown = 11;
    }
}
