package org.basmat.map.util;

import org.basmat.map.model.ModelStructure;
import org.basmat.map.model.cells.WorldCell;
import org.basmat.map.model.cells.factory.CellFactory;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PointUtilitiesTest {

    @Test
    void calculateRandomCoordinates_isInRangeOfRadius_True() {
        Point center = new Point(0, 0);
        int radius  = 10;
        for (int i = 0; i < 100; i++) {
            Point p = PointUtilities.calculateRandomCoordinates(center, 10);
            assert(Math.pow(p.x - center.x, 2) + Math.pow(p.y - center.y, 2) < Math.pow(radius, 2));
        }
    }

    @Test
    void validateBounds_deniesOutOfRangePos_False() {
        assertFalse(PointUtilities.validateBounds(new Point(200, 200)));
    }

    @Test
    void validateBounds_deniesOutOfRangeNegative_False() {
        assertFalse(PointUtilities.validateBounds(new Point(-1, -1)));
    }

    @Test
    void validateBounds_acceptsEdgeCase_True() {
        assertTrue(PointUtilities.validateBounds(new Point(0, 0)));
        assertTrue(PointUtilities.validateBounds(new Point(149, 149)));
    }

    @Test
    void calculateRandomValidCoordinates_doesNotExceedBoundsAndIsValidType_True() {
        HashMap<ECellType, BufferedImage> eCellTypeBufferedImageHashMap = TextureHelper.cacheCellTextures();
        ModelStructure modelStructure = new ModelStructure();
        TestUtilities.fillModelWithWorldCell(modelStructure, ECellType.GRASS);
        modelStructure.setFrontLayer(new Point(50, 50), new CellFactory().createSocietyCell("test", 10, 0x00ff09ff));
        LinkedList<ECellType> valid = new LinkedList<>();
        valid.add(ECellType.GRASS);
        for (int i = 0; i < 100; i++) {
            Point point = PointUtilities.calculateRandomValidCoordinates(new Point(50, 50), 10, modelStructure, valid);
            assertTrue(PointUtilities.validateBounds(point));
        }
    }

    @Test
    void tintArea_setAllPointsInCircleToTintAndOwner_allIsOwnedAndTinted() {
        HashMap<ECellType, BufferedImage> eCellTypeBufferedImageHashMap = TextureHelper.cacheCellTextures();
        ModelStructure modelStructure = new ModelStructure();
        TestUtilities.fillModelWithWorldCell(modelStructure, ECellType.GRASS);
        modelStructure.setFrontLayer(new Point(50, 50), new CellFactory().createSocietyCell("test", 10, 0x00ff00ff));
        PointUtilities.tintArea(10, new Point(50, 50), 0x00f009f0, modelStructure);
        PointUtilities.forPointsInCircle(10, new Point(50, 50), (point -> {
            int actual = modelStructure.getCoordinate(point).getTexture().getRGB(2, 2);
            int ref = eCellTypeBufferedImageHashMap.get(modelStructure.getCoordinate(point).getECellType()).getRGB(2, 2);
            if (modelStructure.getCoordinate(point).getECellType() == ECellType.GRASS) {
                assertEquals(actual, (ref | 0x00f009f0));
            }
        }));
    }

    @Test
    void resetArea_setsAllPointsInCircleToNull_IsAllNull() {
        ModelStructure modelStructureTinted = new ModelStructure();
        TestUtilities.fillModelWithWorldCell(modelStructureTinted, ECellType.GRASS);
        modelStructureTinted.setFrontLayer(new Point(50, 50), new CellFactory().createSocietyCell("test", 10, 0x00ff00ff));
        PointUtilities.tintArea(10, new Point(50, 50), 0x00f009f0, modelStructureTinted);
        PointUtilities.resetArea(10, new Point(50, 50), modelStructureTinted);
        PointUtilities.forPointsInCircle(10, new Point(50, 50), point -> {
            if(modelStructureTinted.getCoordinate(point) instanceof WorldCell worldCell) {
                assertNull(worldCell.getOwner());
            }
        });
    }

    @Test
    void getAllValidatedNeighbours_hasNoIllegalNeighbours_streamReturnsTrue() {
        List<Point> allValidatedNeighbours = PointUtilities.getAllValidatedNeighbours(new Point(0, 0));
        assertTrue(allValidatedNeighbours.stream().allMatch(PointUtilities::validateBounds));
    }

    @Test
    void getImmediateValidatedNeighbours_hasNoIllegalNeighbours_streamReturnsTrue() {
        List<Point> immediateValidatedNeighbours = PointUtilities.getImmediateValidatedNeighbours(new Point(0, 0));
        assertTrue(immediateValidatedNeighbours.stream().allMatch(PointUtilities::validateBounds));
    }
}