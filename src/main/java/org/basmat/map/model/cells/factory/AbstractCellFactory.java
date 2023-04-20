package org.basmat.map.model.cells.factory;

import org.basmat.map.model.cells.SocietyCell;
import org.basmat.map.util.ECellType;

import java.awt.*;

/**
 * Provides the abstract definitions for the cell factory
 */
abstract class AbstractCellFactory {
    protected abstract IOwnedCell createNutrientCell(SocietyCell owner);
    protected abstract IMapCell createSocietyCell(String name, int radius, int tint);
    protected abstract IOwnedCell createWorldCell(ECellType cellType);
    protected abstract IMapCell createLifeCell(Point societyCell);
}
