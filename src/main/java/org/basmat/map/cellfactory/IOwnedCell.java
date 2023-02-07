package org.basmat.map.cellfactory;

import org.basmat.map.cellfactory.IMapCell;
import org.basmat.map.cellfactory.SocietyCell;

/**
 * This interface extends IMapCell and provides methods to describe the owner of the cell that implements this interface
 */
interface IOwnedCell extends IMapCell {
    SocietyCell getOwner();
    void setOwner(SocietyCell owner);
}
