package org.basmat.map.model.cells.factory;

import org.basmat.map.model.cells.LifeCell;
import org.basmat.map.model.cells.NutrientCell;
import org.basmat.map.model.cells.SocietyCell;
import org.basmat.map.model.cells.WorldCell;
import org.basmat.map.util.ECellType;
import org.basmat.map.util.TextureHelper;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;


/**
 * This class is the primary interface to create a cell class. Textures are generated automatically and copied to prevent duplication and enable alpha transparency.
 */
public class CellFactory extends AbstractCellFactory {

    private final HashMap<ECellType, BufferedImage> imageCache;

    public CellFactory () {
        imageCache = TextureHelper.cacheCellTextures();
    }

    @Override
    public NutrientCell createNutrientCell(SocietyCell owner) {
        return new NutrientCell(owner, TextureHelper.copyTexture(imageCache.get(ECellType.NUTRIENTS)));
    }

    @Override
    public SocietyCell createSocietyCell(String name, int radius, int tint) {
        return new SocietyCell(name, radius, tint, TextureHelper.copyTexture(imageCache.get(ECellType.SOCIETY_CELL)));
    }

    @Override
    public WorldCell createWorldCell(ECellType cellType) {
        return new WorldCell(cellType, TextureHelper.copyTexture(imageCache.get(cellType)));
    }

    @Override
    public LifeCell createLifeCell(Point societyCell) {
        return new LifeCell(societyCell, TextureHelper.copyTexture(imageCache.get(ECellType.LIFE_CELL)));
    }
}
