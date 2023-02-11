package org.basmat.map.cellfactory;

import org.basmat.map.cellfactory.cells.SocietyCell;

/**
 * This interface extends IMapCell and provides methods to describe the owner of the cell that implements this interface
 */
public interface IOwnedCell extends IMapCell {
    SocietyCell getOwner();
    void setOwner(SocietyCell owner);
}
