package org.basmat.map.model.cells;

import org.basmat.map.model.cells.factory.IMapCell;
import org.basmat.map.util.ECellType;

import java.awt.*;
import java.awt.image.BufferedImage;


/**
 * LifeCell provides the model of a life cell
 */
public class LifeCell implements IMapCell {
    private int reproductionCooldown;
    private Point societyCell;
    private BufferedImage texture;
    private int attrition;


    /**
     *
     * @param societyCell The society cell that the life cell will be apart of
     * @param texture A reference for the life cell texture.
     */
    public LifeCell(Point societyCell, BufferedImage texture) {
        this.societyCell = societyCell;
        this.texture = texture;
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

    @Override
    public BufferedImage getTexture() {
        return texture;
    }

    @Override
    public String toString() {
        String isOnCooldown = getReproductionCooldown() > 0 ? "Is currently on cooldown: " + getReproductionCooldown() : "Is not on reproduction cooldown";
        //TODO: broken return string
        return "Life cell lives at: "  + "\n" +
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

    public void setReproductionCooldown() {
        reproductionCooldown = 10;
    }
}
