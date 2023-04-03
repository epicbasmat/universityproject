package org.basmat.map.model.cells.factory;

import org.basmat.map.model.cells.SocietyCell;

/**
 * This interface extends IMapCell and provides methods to describe the owner of the cell that implements this interface
 */
public interface IOwnedCell extends IMapCell {
    SocietyCell getOwner();
    void setOwner(SocietyCell owner);
}
