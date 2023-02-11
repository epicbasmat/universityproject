package org.basmat.map.cellfactory;

import org.basmat.map.cellfactory.cells.SocietyCell;
import org.basmat.map.util.ECellType;

/**
 * Provides the abstract definitions for the cell factory
 */
abstract class AbstractCellFactory {
    public abstract IOwnedCell createNutrientCell(SocietyCell owner, int id);
    public abstract IMapCell createSocietyCell(String name, int id, int radius);
    public abstract IOwnedCell createWorldCell(ECellType cellType, SocietyCell owner, int id);
    public abstract IMapCell createLifeCell(SocietyCell societyCell, int id);
}
