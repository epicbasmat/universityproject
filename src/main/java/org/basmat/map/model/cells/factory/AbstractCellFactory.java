package org.basmat.map.model.cells.factory;

import org.basmat.map.model.cells.SocietyCell;
import org.basmat.map.util.ECellType;

import java.awt.*;

/**
 * Provides the abstract definitions for the cell factory
 */
abstract class AbstractCellFactory {
    public abstract IOwnedCell createNutrientCell(SocietyCell owner);
    public abstract IMapCell createSocietyCell(String name, int radius, int tint);
    public abstract IOwnedCell createWorldCell(ECellType cellType);
    public abstract IMapCell createLifeCell(Point societyCell);
}
