package org.basmat.map.model.cells.factory;

import org.basmat.map.util.ECellType;

import java.awt.image.BufferedImage;

/**
 * This interface provides a construct for all cells that exist within a map to implement
 */
public interface IMapCell {
    BufferedImage getTexture();
    @Override String toString();
    ECellType getECellType();
}
