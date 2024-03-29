package org.basmat.map.controller.rules;

import org.basmat.map.controller.Controller;
import org.basmat.map.model.ModelStructure;
import org.basmat.map.model.cells.LifeCell;
import org.basmat.map.model.cells.SocietyCell;
import org.basmat.map.model.cells.factory.CellFactory;
import org.basmat.map.model.cells.factory.IOwnedCell;
import org.basmat.map.setup.ModelSetup;
import org.basmat.map.util.ECellType;
import org.basmat.map.util.PointUtilities;
import org.basmat.map.util.SimulationProperties;
import org.basmat.map.util.TestUtilities;
import org.basmat.map.util.path.Node;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class GardenerTest {

    private ModelStructure modelStructure;
    private final CellFactory cellFactory;
    private Gardener gardener;
    private LinkedList<Point> globalSocietyCellList;
    private ArrayList<Point> globalLifeCellList;
    private ArrayList<LinkedList<Node>> listOfPaths;
    private Controller c;
    private SocietyCell societyCell;
    private Point societyPoint;

    GardenerTest() throws InterruptedException {
        cellFactory = new CellFactory();
        modelStructure = new ModelStructure();
        TestUtilities.fillModelWithWorldCell(modelStructure, ECellType.GRASS);
        SimulationProperties simulationProperties = new SimulationProperties(7, 100, 1, 20, 5, 75, 0.6);
        globalSocietyCellList = new LinkedList<>();
        globalLifeCellList = new ArrayList<>();
        listOfPaths = new ArrayList<>();
        c = new Controller(150, 150);
        gardener = new Gardener(c, modelStructure, globalSocietyCellList, globalLifeCellList, listOfPaths);
    }

    public void constructNewSimulation() {
        modelStructure = new ModelStructure();
        SimulationProperties simulationProperties = new SimulationProperties(7, 100, 1, 20, 5, 75, 0.6);
        globalSocietyCellList = new LinkedList<>();
        globalLifeCellList = new ArrayList<>();
        listOfPaths = new ArrayList<>();
        c = new Controller(150,150);
        c.setSimulationProperties(simulationProperties);
        new ModelSetup(c, modelStructure, globalSocietyCellList, globalLifeCellList);
        TestUtilities.fillModelWithWorldCell(modelStructure, ECellType.GRASS);
        societyCell = cellFactory.createSocietyCell("test", 12, 0);
        societyPoint = new Point(12, 12);
        modelStructure.setFrontLayer(societyPoint, societyCell);
        globalSocietyCellList.add(societyPoint);
        this.gardener = new Gardener(c, modelStructure, globalSocietyCellList, globalLifeCellList, listOfPaths);
    }

    @Test
    void expansion_checkExpansionDoesNotOccur_expansionDoesNotOccur() {
        constructNewSimulation();
        Point society = new Point(90, 90);
        globalLifeCellList.add(society);
        modelStructure.setFrontLayer(society, cellFactory.createSocietyCell("test zero", 10, 0x00000000));
        SocietyCell frontLayer = modelStructure.getFrontLayer(society);
        for (int i = 0; i < 5; i++) {
            frontLayer.addLifeCells();
        }
        PointUtilities.tintArea(10, society, 0x00, modelStructure);
        gardener.expand();
        assertEquals(0, frontLayer.getPreviousExpansionQuotient());
        assertNull(((IOwnedCell) modelStructure.getCoordinate(new Point(101, 90))).getOwner());
    }

    @Test
    void expansion_checkExpansionOccurs_expansionOccurs() {
        constructNewSimulation();

        Point society = new Point(75, 75);
        globalSocietyCellList.add(society);
        modelStructure.setFrontLayer(society, cellFactory.createSocietyCell("test one", 10, 0x00fa09fa));
        SocietyCell coordinate = modelStructure.getCoordinate(society);

        //Add just below the threshold to expand
        for (int i = 0; i < 5; i++) {
            coordinate.addLifeCells();
        }

        PointUtilities.tintArea(10, society, 0x00fa09fa, modelStructure);
        gardener.expand();
        assertNull(modelStructure.getBackLayer(new Point(64, 64)).getOwner());
        assertEquals(0, coordinate.getPreviousExpansionQuotient());
        //assert that it doesnt expand, add ne more life cell and expect that it expands
        coordinate.addLifeCells();
        gardener.expand();
        assertNotNull(modelStructure.getBackLayer(new Point(64, 64)));
        assertEquals(1, coordinate.getPreviousExpansionQuotient());
        globalSocietyCellList.remove(0);
        modelStructure.deleteCoordinate(society);
    }

    @Test
    void scatter_checkIfCellsScatterOnCoolDown_newPathIsAddedToPathList() {
        constructNewSimulation();
        SocietyCell societyCell = cellFactory.createSocietyCell("test two", 10, 0x0000000);
        Point societyPoint = new Point(100, 100);
        modelStructure.setFrontLayer(societyPoint, societyCell);
        globalSocietyCellList.add(societyPoint);
        LifeCell testSubject = cellFactory.createLifeCell(societyPoint);
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
    void checkForValidReproduction_cellsReproduceUnderCorrectCircumstance_newCellIsCreated() {
        constructNewSimulation();
        SocietyCell societyCell = cellFactory.createSocietyCell("test three", 10, 0x0000000);
        Point societyPoint = new Point(100, 50);
        modelStructure.setFrontLayer(societyPoint, societyCell);
        Point point1 = new Point(101, 50);
        modelStructure.setFrontLayer(point1, cellFactory.createLifeCell(societyPoint));
        societyCell.addLifeCells();
        globalLifeCellList.add(point1);
        int snapshot = globalLifeCellList.size();
        gardener.checkForValidReproduction();
        assertEquals(snapshot, globalLifeCellList.size());
        Point point2 = new Point(102, 50);
        modelStructure.setFrontLayer(point2, cellFactory.createLifeCell(societyPoint));
        societyCell.addLifeCells();
        globalLifeCellList.add(point2);
        snapshot = globalLifeCellList.size();
        gardener.checkForValidReproduction();
        assertEquals(snapshot + 1, globalLifeCellList.size());
    }

    @Test
    void checkForValidReproduction_cellsDoNotReproduce_lifeCellIsNotCreated() {
        constructNewSimulation();
        SocietyCell societyCell = cellFactory.createSocietyCell("test four", 10, 0x00000000);
        Point societyPoint = new Point(50, 100);
        modelStructure.setFrontLayer(societyPoint, societyCell);
        Point parent1 = new Point(51, 100);
        Point parent2 = new Point(52, 101);
        //misalign the parents to ensure they do not reproduce
        modelStructure.setFrontLayer(parent1, cellFactory.createLifeCell(societyPoint));
        modelStructure.setFrontLayer(parent2, cellFactory.createLifeCell(societyPoint));
        globalLifeCellList.add(parent1);
        globalLifeCellList.add(parent2);
        int snapshot = globalLifeCellList.size();
        gardener.checkForValidReproduction();
        assertEquals(snapshot, globalLifeCellList.size());
    }
}