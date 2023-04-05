package org.basmat.map.controller.rules;

import org.basmat.map.model.ModelStructure;
import org.basmat.map.model.cells.LifeCell;
import org.basmat.map.model.cells.SocietyCell;
import org.basmat.map.model.cells.factory.CellFactory;
import org.basmat.map.util.*;
import org.basmat.map.util.path.Node;
import org.basmat.map.view.UserInteractionUI;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class GardenerTest {

    private final ModelStructure modelStructure;
    private final CellFactory cellFactory;
    private final HashMap<ECellType, BufferedImage> textureCache;
    private final Winnower winnower;
    private final Gardener gardener;
    private final LinkedList<Point> globalSocietyCellList;
    private final LinkedList<Point> globalLifeCellList;
    private final LinkedList<LinkedList<Node>> listOfPaths;

    GardenerTest() {
        cellFactory = new CellFactory();
        textureCache = TextureHelper.cacheCellTextures();
        modelStructure = new ModelStructure();
        TestUtilities.fillModelWithWorldCell(modelStructure, ECellType.GRASS);
        SimulationProperties simulationProperties = new SimulationProperties(7, 100, 1, 20, 5, 75, 0.6);
        UserInteractionUI userInteractionUi = new UserInteractionUI(null);
        globalSocietyCellList = new LinkedList<>();
        globalLifeCellList = new LinkedList<>();
        listOfPaths = new LinkedList<>();
        gardener = new Gardener(simulationProperties, userInteractionUi, modelStructure, globalSocietyCellList, globalLifeCellList, listOfPaths);
        winnower = new Winnower(simulationProperties, userInteractionUi, modelStructure, globalSocietyCellList, globalLifeCellList, new LinkedList<>());
    }

    @Test
    void expansion_checkExpansionOccurs_expansionOccurs() {
        Point society = new Point(75, 75);
        globalSocietyCellList.add(society);
        modelStructure.setFrontLayer(society, cellFactory.createSocietyCell("test one", 10, 0x00fa09fa, textureCache.get(ECellType.SOCIETY_CELL)));
        SocietyCell coordinate = modelStructure.getCoordinate(society);

        //Add just below the threshold to expand
        for (int i = 0; i < 5; i++) {
            coordinate.addLifeCells();
        }

        PointUtilities.tintArea(10, society, 0x00fa09fa, modelStructure);
        gardener.expand();
        assertNull(modelStructure.getBackLayer(new Point(64, 64)).getOwner());
        assertEquals(0, coordinate.getPreviousExpansionQuotient());
        coordinate.addLifeCells();
        gardener.expand();
        assertNotNull(modelStructure.getBackLayer(new Point(64, 64)));
        assertEquals(1, coordinate.getPreviousExpansionQuotient());
        globalSocietyCellList.remove(0);
        modelStructure.deleteCoordinate(society);
    }

    @Test
    void scatter_checkIfCellsScatterOnCoolDown_newPathIsAddedToPathList() {
        SocietyCell societyCell = cellFactory.createSocietyCell("test two", 10, 0x0000000, textureCache.get(ECellType.SOCIETY_CELL));
        Point societyPoint = new Point(100, 100);
        modelStructure.setFrontLayer(societyPoint, societyCell);
        globalSocietyCellList.add(societyPoint);
        LifeCell testSubject = cellFactory.createLifeCell(societyPoint, textureCache.get(ECellType.LIFE_CELL));
        Point lifePoint = new Point(101, 101);
        modelStructure.setFrontLayer(lifePoint, testSubject);
        globalLifeCellList.add(lifePoint);
        testSubject.resetReproductionCooldown();
        gardener.scatter();
        assertTrue(listOfPaths.isEmpty());
        testSubject.decrementReproductionCooldown();
        testSubject.decrementReproductionCooldown();
        gardener.scatter();
        assertFalse(listOfPaths.isEmpty());
        testSubject.decrementReproductionCooldown();
        gardener.scatter();
        assertEquals(1, listOfPaths.size());
    }

    @Test
    void checkForValidReproduction() {
    }

    @Test
    void unison() {
    }

    @Test
    void reproduce() {
    }
}