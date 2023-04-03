package org.basmat.map.model.cells.factory;

import org.basmat.map.model.cells.SocietyCell;
import org.basmat.map.util.ECellType;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Provides the abstract definitions for the cell factory
 */
abstract class AbstractCellFactory {
    public abstract IOwnedCell createNutrientCell(SocietyCell owner, BufferedImage texture);
    public abstract IMapCell createSocietyCell(String name, int radius, int tint, BufferedImage texture);
    public abstract IOwnedCell createWorldCell(ECellType cellType, BufferedImage texture);

    public abstract IMapCell createLifeCell(Point societyCell, BufferedImage texture);
}
