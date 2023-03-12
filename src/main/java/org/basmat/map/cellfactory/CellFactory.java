package org.basmat.map.cellfactory;

import org.basmat.map.cellfactory.cells.LifeCell;
import org.basmat.map.cellfactory.cells.NutrientCell;
import org.basmat.map.cellfactory.cells.SocietyCell;
import org.basmat.map.cellfactory.cells.WorldCell;
import org.basmat.map.util.ECellType;


/**
 * This class is the primary interface to create a cell class.
 */
public class CellFactory extends AbstractCellFactory {

    @Override
    public NutrientCell createNutrientCell(SocietyCell owner, int id) {
        return new NutrientCell(owner, id);
    }

    @Override
    public SocietyCell createSocietyCell(String name, int id, int radius, int tint) {
        return new SocietyCell(name, id, radius, tint);
    }

    @Override
    public WorldCell createWorldCell(ECellType cellType, SocietyCell owner, int id) {
        return new WorldCell(cellType, owner, id);
    }

    @Override
    public IMapCell createLifeCell(SocietyCell societyCell, int id) {
        return new LifeCell(societyCell, id);
    }
}
