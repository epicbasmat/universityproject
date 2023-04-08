package org.basmat.map.util;

import org.basmat.map.model.ModelStructure;
import org.basmat.map.model.cells.factory.CellFactory;
import org.basmat.map.util.path.Node;
import org.basmat.map.util.path.Pathfind;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PathfindTest {

    private final ModelStructure modelStructure;
    private CellFactory cellFactory;

    PathfindTest() {
        modelStructure = new ModelStructure();
        cellFactory = new CellFactory();
        TestUtilities.fillModelWithWorldCell(modelStructure, ECellType.GRASS);
    }
    
    @Test
    void pathfind_pathIsValid_currentEqualsDestination() {
        Point current = null;
        LinkedList<Node> nodes = Pathfind.aStar(2500, modelStructure, new Point(0, 0), new Point(100, 100));
        for (Node node : nodes) {
            current = node.point();
        } 
        assertEquals(new Point(100, 99), current);
    }

    @Test
    void pathfind_pathIsBlocked_aStarReturnsZeroLengthList() {
        for (int y = 0; y < 150; y++) {
            modelStructure.setBackLayer(new Point(50, y), cellFactory.createWorldCell(ECellType.DEEP_WATER));
        }
        assertEquals(0, Pathfind.aStar(5000, modelStructure, new Point(0, 0), new Point(100, 100)).size());
    }

    @Test
    void pathfind_pathIsValidThroughMaze_currentEqualsDestination() {
        modelStructure.setBackLayer(new Point(50, 50), cellFactory.createWorldCell(ECellType.GRASS));
        for (int y = 5; y < 150; y++) {
            modelStructure.setBackLayer(new Point(60, y), cellFactory.createWorldCell(ECellType.MOUNTAIN_PEAK));
        }

        for (int y = 150; y > 5; y--) {
            modelStructure.setBackLayer(new Point(60, y), cellFactory.createWorldCell(ECellType.MOUNTAIN_PEAK));
        }

        Point current = null;
        LinkedList<Node> nodes = Pathfind.aStar(25000, modelStructure, new Point(0, 0), new Point(100, 100));
        for (Node node : nodes) {
            current = node.point();
        }
        assertEquals(new Point(100, 99), current);
    }
}
