package org.basmat.map.cellfactory;

import org.basmat.map.util.ECellType;

import java.awt.*;

/**
 * This interface provides a construct for all map cells to implement
 */
interface IMapCell {
    @Override String toString();
    ECellType getECellType();
}
