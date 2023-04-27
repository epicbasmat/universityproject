package org.basmat.map.controller.rules;

import org.basmat.map.controller.Controller;
import org.basmat.map.model.ModelStructure;
import org.basmat.map.model.cells.LifeCell;
import org.basmat.map.model.cells.NutrientCell;
import org.basmat.map.model.cells.SocietyCell;
import org.basmat.map.model.cells.factory.CellFactory;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WinnowerTest {

    private ModelStructure modelStructure;
    private final CellFactory cellFactory;
    private Winnower winnower;
    private LinkedList<Point> globalSocietyCellList;
    private ArrayList<Point> globalLifeCellList;
    private ArrayList<LinkedList<Node>> listOfPaths;
    private SocietyCell societyCell;
    private Point societyPoint;
    private Controller c;

    WinnowerTest() {
        cellFactory = new CellFactory();
        constructNewSimulation();
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
        this.winnower = new Winnower(c, modelStructure, globalSocietyCellList, globalLifeCellList, listOfPaths);
    }

    @Test
    void overcrowded_LifeCellsDoNotDieToOvercrowded_noDeathsOccur() {
        constructNewSimulation();
        Point subject = new Point(101, 101);
        modelStructure.setFrontLayer(subject, cellFactory.createLifeCell(societyPoint));
        globalLifeCellList.add(subject);
        PointUtilities.getImmediateValidatedNeighbours(subject).forEach(p -> {
            modelStructure.setFrontLayer(p, cellFactory.createLifeCell(societyPoint));
            globalLifeCellList.add(p);
        });
        int snapshot = globalLifeCellList.size();
        winnower.overcrowded();
        assertEquals(snapshot, globalLifeCellList.size());
    }

    @Test
    void overcrowded_LifeCellsDieToOvercrowded_deathsOccur() {
        constructNewSimulation();

        Point subject = new Point(50, 50);

        modelStructure.setFrontLayer(subject, cellFactory.createLifeCell(societyPoint));
        globalLifeCellList.add(subject);

        //Swarm the subject with life cells, making sure all neighbourin cells are Life Cells
        PointUtilities.getAllValidatedNeighbours(subject).forEach(p -> {
            modelStructure.setFrontLayer(p, cellFactory.createLifeCell(societyPoint));
            globalLifeCellList.add(p);
        });

        int snapshot = globalLifeCellList.size();
        winnower.overcrowded();
        assertNotEquals(snapshot, globalLifeCellList.size());
    }

    @Test
    void overcrowded_LifeCellstDieToOvercrowdedWithLowerParams_deathsOccur(){
        constructNewSimulation();

        //Construct a new simulation but with a small overcrowd threshold, this should mean cells die with less around them
        c.setSimulationProperties(new SimulationProperties(7, 100, 1, 20, 2, 75, 0.6));
        TestUtilities.fillModelWithWorldCell(modelStructure, ECellType.GRASS);
        Point subject = new Point(23, 100);
        modelStructure.setFrontLayer(subject, cellFactory.createLifeCell(societyPoint));
        globalLifeCellList.add(subject);
        //swarm with 4 neighbours rather than 8
        PointUtilities.getImmediateValidatedNeighbours(subject).forEach(p -> {
            modelStructure.setFrontLayer(p, cellFactory.createLifeCell(societyPoint));
            globalLifeCellList.add(p);
        });
        int snapshot = globalLifeCellList.size();
        winnower.overcrowded();
        assertNotEquals(snapshot, globalLifeCellList.size());
    }

    @Test
    void overcrowded_LifeCellsDoNotDieToOvercrowdedWithMaxParams_noDeathsOccur() {
        constructNewSimulation();

        c.setSimulationProperties(new SimulationProperties(7, 100, 1, 20, 8, 75, 0.6));
        TestUtilities.fillModelWithWorldCell(modelStructure, ECellType.GRASS);
        Point subject = new Point(64, 64);
        modelStructure.setFrontLayer(subject, cellFactory.createLifeCell(societyPoint));
        globalLifeCellList.add(subject);
        PointUtilities.getAllValidatedNeighbours(subject).forEach(p -> {
            modelStructure.setFrontLayer(p, cellFactory.createLifeCell(societyPoint));
            globalLifeCellList.add(p);
        });
        int snapshot = globalLifeCellList.size();
        winnower.overcrowded();
        assertEquals(snapshot, globalLifeCellList.size());
    }

    @Test
    void overcrowded_LifeCellsDoNotDieToOvercrowdedWithEdgeCaseParams_noDeathsOccur() {
        constructNewSimulation();
        //because it should be greater than the threshold to die, they should not die if 4 are surrounding them with the threshold set to 4
        c.setSimulationProperties(new SimulationProperties(7, 100, 1, 20, 4, 75, 0.6));
        TestUtilities.fillModelWithWorldCell(modelStructure, ECellType.GRASS);
        Point subject = new Point(123, 123);
        modelStructure.setFrontLayer(subject, cellFactory.createLifeCell(societyPoint));
        globalLifeCellList.add(subject);
        PointUtilities.getImmediateValidatedNeighbours(subject).forEach(p -> {
            modelStructure.setFrontLayer(p, cellFactory.createLifeCell(societyPoint));
            globalLifeCellList.add(p);
        });
        int snapshot = globalLifeCellList.size();
        winnower.overcrowded();
        assertEquals(snapshot, globalLifeCellList.size());
    }

    @Test
    void stagnation_attritionIncrementsWithNoNeighbours_lifeCellIncreasesInAttrition() {
        constructNewSimulation();

        TestUtilities.fillModelWithWorldCell(modelStructure, ECellType.GRASS);
        Point subject = new Point(3, 100);
        modelStructure.setFrontLayer(subject, cellFactory.createLifeCell(societyPoint));
        globalLifeCellList.add(subject);
        int snapshot = ((LifeCell) modelStructure.getFrontLayer(subject)).getAttrition();
        winnower.stagnation();
        assertNotEquals(snapshot, ((LifeCell) modelStructure.getFrontLayer(subject)).getAttrition());
    }
    @Test

    void stagnation_attritionGetsResetWithNeighbours_attritionResets() {
        constructNewSimulation();

        TestUtilities.fillModelWithWorldCell(modelStructure, ECellType.GRASS);
        Point subject1 = new Point(6, 100);
        modelStructure.setFrontLayer(subject1, cellFactory.createLifeCell(societyPoint));
        globalLifeCellList.add(subject1);
        winnower.stagnation();
        winnower.stagnation();
        int snapshot = ((LifeCell) modelStructure.getFrontLayer(subject1)).getAttrition();
        Point subject2 = new Point(7, 100);
        modelStructure.setFrontLayer(subject2, cellFactory.createLifeCell(societyPoint));
        globalLifeCellList.add(subject2);
        winnower.stagnation();
        assertNotEquals(snapshot, ((LifeCell) modelStructure.getFrontLayer(subject1)).getAttrition());
    }

    @Test
    void stagnation_attritionKillsCell_cellGetsKilled() {
        constructNewSimulation();

        TestUtilities.fillModelWithWorldCell(modelStructure, ECellType.GRASS);
        Point subject1 = new Point(10, 100);
        modelStructure.setFrontLayer(subject1, cellFactory.createLifeCell(societyPoint));
        globalLifeCellList.add(subject1);
        for (int i = 0; i < c.getSimulationProperties().attritionThreshold() + 1; i++) {
            winnower.stagnation();
        }
        assertNull(modelStructure.getFrontLayer(subject1));
    }

    @Test
    void stagnation_attritionKillThresholdEqualsProperties_thresholdEqualsProperties() {
        constructNewSimulation();
        TestUtilities.fillModelWithWorldCell(modelStructure, ECellType.GRASS);
        Point subject1 = new Point(13, 100);
        modelStructure.setFrontLayer(subject1, cellFactory.createLifeCell(societyPoint));
        globalLifeCellList.add(subject1);
        assertNotNull(modelStructure.getFrontLayer(subject1));
        //+ 1 to push it over past threshold
        for (int i = 0; i < c.getSimulationProperties().attritionThreshold() + 1; i++) {
            winnower.stagnation();
        }
        assertNull(modelStructure.getFrontLayer(subject1));
    }

    @Test
    void famine_cellIsKilledOverThreshold_lifeCellIsRemoved() {
        constructNewSimulation();
        TestUtilities.fillModelWithWorldCell(modelStructure, ECellType.GRASS);
        societyCell = cellFactory.createSocietyCell("test2", 12, 0);
        societyPoint = new Point(60, 60);
        globalSocietyCellList.add(societyPoint);
        modelStructure.setFrontLayer(societyPoint, societyCell);
        //Setup an amount of nutrient cells for the society.
        for (int i = 0; i < 10; i++) {
            NutrientCell nutrientCell = cellFactory.createNutrientCell(societyCell);
            modelStructure.setFrontLayer(PointUtilities.calculateRandomValidCoordinates(societyPoint, 12, modelStructure, List.of(new ECellType[]{ECellType.GRASS})), nutrientCell);
            societyCell.addNutrientCells(nutrientCell);

        }
        //And then add as many life cells as we can
        for (int j = 0; j < societyCell.getNutrientCapacity() * (c.getSimulationProperties().foodThreshold() + 1.1); j++) {
            Point e = PointUtilities.calculateRandomValidCoordinates(societyPoint, 12, modelStructure, List.of(new ECellType[]{ECellType.GRASS}));
            globalLifeCellList.add(e);
            societyCell.addLifeCells();
            modelStructure.setFrontLayer(e, cellFactory.createLifeCell(societyPoint));
        }
        int snapshot = globalLifeCellList.size();
        winnower.famine();
        assertNotEquals(snapshot, globalLifeCellList.size());
    }

    @Test
    void famine_cellIsNotKilledBelowThreshold_lifeCellIsNotRemoved() {
        constructNewSimulation();
        TestUtilities.fillModelWithWorldCell(modelStructure, ECellType.GRASS);
        societyCell = cellFactory.createSocietyCell("test2", 12, 0);
        societyPoint = new Point(60, 60);
        globalSocietyCellList.add(societyPoint);
        modelStructure.setFrontLayer(societyPoint, societyCell);
        for (int i = 0; i < 10; i++) {
            NutrientCell nutrientCell = cellFactory.createNutrientCell(societyCell);
            modelStructure.setFrontLayer(PointUtilities.calculateRandomValidCoordinates(societyPoint, 12, modelStructure, List.of(new ECellType[]{ECellType.GRASS})), nutrientCell);
            societyCell.addNutrientCells(nutrientCell);
        }

        for (int j = 0; j < societyCell.getNutrientCapacity() * c.getSimulationProperties().foodThreshold() + 0.9; j++) {
            Point e = PointUtilities.calculateRandomValidCoordinates(societyPoint, 12, modelStructure, List.of(new ECellType[]{ECellType.GRASS}));
            globalLifeCellList.add(e);
            societyCell.addLifeCells();
            modelStructure.setFrontLayer(e, cellFactory.createLifeCell(societyPoint));
        }

        int snapshot = globalLifeCellList.size();
        winnower.famine();
        assertEquals(snapshot, globalLifeCellList.size());
    }

    @Test
    void collapse_societyDoesNotCollapseUnderThreshold_societyIsNotRemoved() {
        constructNewSimulation();
        TestUtilities.fillModelWithWorldCell(modelStructure, ECellType.GRASS);
        do {
            Point e = PointUtilities.calculateRandomValidCoordinates(societyPoint, 12, modelStructure, List.of(new ECellType[]{ECellType.GRASS}));
            globalLifeCellList.add(e);
            societyCell.addLifeCells();
            modelStructure.setFrontLayer(e, cellFactory.createLifeCell(societyPoint));
        } while (societyCell.getLandPerLifeCell() > c.getSimulationProperties().landRatio() - 5);
        int societySnapshot = globalSocietyCellList.size();
        int lifeSnapshot = globalLifeCellList.size();
        winnower.collapse();
        assertEquals(societySnapshot, globalSocietyCellList.size());
        assertEquals(lifeSnapshot, globalLifeCellList.size());
    }

    @Test
    void collapse_societyCollapsesOverThreshold_societyIsNotRemoved() {
        constructNewSimulation();
        TestUtilities.fillModelWithWorldCell(modelStructure, ECellType.GRASS);
        do {
            Point e = PointUtilities.calculateRandomValidCoordinates(societyPoint, 12, modelStructure, List.of(new ECellType[]{ECellType.GRASS}));
            globalLifeCellList.add(e);
            societyCell.addLifeCells();
            modelStructure.setFrontLayer(e, cellFactory.createLifeCell(societyPoint));
        } while (societyCell.getLandPerLifeCell() > c.getSimulationProperties().landRatio() + 5);
        int societySnapshot = globalSocietyCellList.size();
        int lifeSnapshot = globalLifeCellList.size();
        winnower.collapse();
        assertNotEquals(societySnapshot, globalSocietyCellList.size());
        assertNotEquals(lifeSnapshot, globalLifeCellList.size());
    }
}
