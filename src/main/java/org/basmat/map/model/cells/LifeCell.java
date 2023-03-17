package org.basmat.map.model.cells;

import org.basmat.map.model.cells.factory.IMapCell;
import org.basmat.map.util.ECellType;

import java.awt.image.BufferedImage;


/**
 * LifeCell provides the model of a life cell
 */
public class LifeCell implements IMapCell {
    private SocietyCell societyCell;
    private BufferedImage texture;
    private int attrition;


    /**
     *
     * @param societyCell The society cell that the life cell will be apart of
     * @param texture A reference for the life cell texture.
     */
    public LifeCell(SocietyCell societyCell, BufferedImage texture) {
        this.societyCell = societyCell;
        this.texture = texture;
        this.attrition = 0;
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
        return "Life cell lives at: " + this.societyCell.getName() + "\n" +
                "Attrition level: " + this.attrition;
    }
}
