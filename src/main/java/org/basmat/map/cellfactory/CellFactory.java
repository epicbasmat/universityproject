package org.basmat.map.cellfactory;

import org.basmat.map.util.ECellType;


/**
 * This class is the primary interface to create a cell class.
 */
public class CellFactory extends AbstractCellFactory {

    @Override
    public NutrientCell createNutrientCell(SocietyCell owner) {
        return new NutrientCell(owner);
    }

    @Override
    public SocietyCell createSocietyCell(String name) {
        return new SocietyCell(name);
    }

    @Override
    public WorldCell createWorldCell(ECellType cellType, SocietyCell owner) {
        return new WorldCell(cellType, owner);
    }
}
