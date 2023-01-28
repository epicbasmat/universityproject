package org.basmat.map.cellfactory;

import org.basmat.map.util.ECellType;

/**
 * Provides the abstract definitions for the cell factory
 */
abstract class AbstractCellFactory {
    public abstract IOwnedCell createNutrientCell(SocietyCell owner);
    public abstract IMapCell createSocietyCell(String name);
    public abstract IOwnedCell createWorldCell(ECellType cellType, SocietyCell owner);
}
