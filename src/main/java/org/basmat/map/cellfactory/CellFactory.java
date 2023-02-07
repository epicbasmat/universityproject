package org.basmat.map.cellfactory;

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
    public SocietyCell createSocietyCell(String name, int id) {
        return new SocietyCell(name, id);
    }

    @Override
    public WorldCell createWorldCell(ECellType cellType, SocietyCell owner, int id) {
        return new WorldCell(cellType, owner, id);
    }
}
