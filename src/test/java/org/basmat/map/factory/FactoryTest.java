package org.basmat.map.factory;

import org.basmat.map.model.cells.LifeCell;
import org.basmat.map.model.cells.NutrientCell;
import org.basmat.map.model.cells.SocietyCell;
import org.basmat.map.model.cells.WorldCell;
import org.basmat.map.model.cells.factory.CellFactory;
import org.basmat.map.util.ECellType;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class FactoryTest {
    private final CellFactory cellFactory;

    FactoryTest() {
        cellFactory = new CellFactory();
    }

    @Test
    void createNutrientCell_createsNutrientCell_nutrientCellIsReturned() {
        assertInstanceOf(NutrientCell.class, cellFactory.createNutrientCell(null));
    }

    @Test
    void createNutrientCell_nutrientCellHasOwnerSet_nutrientCellHasOwner() {
        SocietyCell societyCell = cellFactory.createSocietyCell("test1", 12, 0x00f);
        NutrientCell nutrientCell = cellFactory.createNutrientCell(societyCell);
        assertEquals(nutrientCell.getOwner(), societyCell);
    }

    @Test
    void createSocietyCell_createsSocietyCell_societyCellIsReturned() {
        assertInstanceOf(SocietyCell.class, cellFactory.createSocietyCell(null, 0, 0x000));
    }

    @Test
    void createSocietyCell_societyCellHasNameSet_correctNameIsReturned() {
        SocietyCell test2 = cellFactory.createSocietyCell("test2", 0, 0x000);
        assertInstanceOf(SocietyCell.class, test2);
        assertEquals("test2", test2.getName());
    }

    @Test
    void createSocietyCell_societyCellHasRadiusSet_correctRadiusIsReturned() {
        SocietyCell test2 = cellFactory.createSocietyCell("test2", 12, 0x000);
        assertInstanceOf(SocietyCell.class, test2);
        assertEquals(12, test2.getRadius());
    }

    @Test
    void createSocietyCell_societyCellHasRadiusSet_correctTintIsReturned() {
        SocietyCell test2 = cellFactory.createSocietyCell("test2", 12, 0xffaa);
        assertInstanceOf(SocietyCell.class, test2);
        assertEquals(0xffaa, test2.getTint());
    }

    @Test
    void createWorldCell_createsWorldCell_worldCellIsReturned() {
        assertInstanceOf(WorldCell.class, cellFactory.createWorldCell(ECellType.WATER));
    }

    @Test
    void createWorldCell_setWorldCellAsGrass_worldCellIsGrass() {
        WorldCell worldCell = cellFactory.createWorldCell(ECellType.GRASS);
        assertInstanceOf(WorldCell.class, worldCell);
        assertEquals(ECellType.GRASS, worldCell.getECellType());
    }

    @Test
    void createWorldCell_worldCellHasCorrectTexture_worldCellHasGrassTexture() throws IOException, URISyntaxException {
        WorldCell worldCell = cellFactory.createWorldCell(ECellType.GRASS);
        assertInstanceOf(WorldCell.class, worldCell);

        int width  = worldCell.getTexture().getWidth();
        int height = worldCell.getTexture().getHeight();

        BufferedImage toCompare = ImageIO.read(new File(Objects.requireNonNull(this.getClass().getClassLoader().getResource("textures/grass.png")).toURI()));

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                assertEquals(toCompare.getRGB(x, y), worldCell.getTexture().getRGB(x, y));
            }
        }
    }

    @Test
    void createLifeCell_createsLifeCell_returnsLifeCell() {
        assertInstanceOf(LifeCell.class, cellFactory.createLifeCell(null));
    }

    @Test
    void createLifeCell_hasOwner_returnsOwner() {
        SocietyCell societyCell = cellFactory.createSocietyCell("test1", 12, 0x00f);
        Point sc = new Point(80, 80);
        LifeCell lifeCell    = cellFactory.createLifeCell(sc);
        assertEquals(sc, lifeCell.getSocietyCell());
    }
}
