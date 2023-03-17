package org.basmat.map.model.cells.factory;

import org.basmat.map.model.cells.LifeCell;
import org.basmat.map.model.cells.NutrientCell;
import org.basmat.map.model.cells.SocietyCell;
import org.basmat.map.model.cells.WorldCell;
import org.basmat.map.util.ECellType;
import org.basmat.map.util.TextureHelper;

import java.awt.image.BufferedImage;


/**
 * This class is the primary interface to create a cell class.
 */
public class CellFactory extends AbstractCellFactory {

    @Override
    public NutrientCell createNutrientCell(SocietyCell owner, BufferedImage texture) {
        return new NutrientCell(owner, TextureHelper.copyTexture(texture));
    }

    @Override
    public SocietyCell createSocietyCell(String name, int radius, int tint, BufferedImage texture) {
        return new SocietyCell(name, radius, tint, TextureHelper.copyTexture(texture));
    }

    @Override
    public WorldCell createWorldCell(ECellType cellType, BufferedImage texture) {
        return new WorldCell(cellType, TextureHelper.copyTexture(texture));
    }

    @Override
    public IMapCell createLifeCell(SocietyCell societyCell, BufferedImage texture) {
        return new LifeCell(societyCell, TextureHelper.copyTexture(texture));
    }
}
