package org.basmat.map.controller.rules;

import org.basmat.map.controller.Controller;
import org.basmat.map.model.ModelStructure;
import org.basmat.map.model.cells.SocietyCell;
import org.basmat.map.model.cells.factory.CellFactory;
import org.basmat.map.util.ECellType;
import org.basmat.map.util.PointUtilities;
import org.basmat.map.util.SimulationProperties;
import org.basmat.map.util.TestUtilities;
import org.basmat.map.util.path.Node;
import org.basmat.map.view.SimulationInteractionUI;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class WinnowerTest {

    private final ModelStructure modelStructure;
    private final CellFactory cellFactory;
    private final Winnower winnower;
    private final LinkedList<Point> globalSocietyCellList;
    private final ArrayList<Point> globalLifeCellList;
    private final ArrayList<LinkedList<Node>> listOfPaths;
    private final SocietyCell societyCell;
    private final Point societyPoint;
    private final Controller c;

    WinnowerTest() throws InterruptedException {
        cellFactory = new CellFactory();
        modelStructure = new ModelStructure();
        SimulationProperties simulationProperties = new SimulationProperties(7, 100, 1, 20, 5, 75, 0.6);
        globalSocietyCellList = new LinkedList<>();
        globalLifeCellList = new ArrayList<>();
        listOfPaths = new ArrayList<>();
        c = new Controller(150,150);
        c.constructSimulation(simulationProperties);
        TestUtilities.fillModelWithWorldCell(modelStructure, ECellType.GRASS);
        winnower = new Winnower(c, modelStructure, globalSocietyCellList, globalLifeCellList, listOfPaths);
        societyCell = cellFactory.createSocietyCell("test", 12, 0);
        societyPoint = new Point(12, 12);
        modelStructure.setFrontLayer(societyPoint, societyCell);
    }

    @Test
    void overcrowded_LifeCellsDoNotDieToOvercrowded_noDeathsOccur() {
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
        //Construct a new simulation but with a small overcrowd threshold, this should mean cells die with less around them
        c.constructSimulation(new SimulationProperties(7, 100, 1, 20, 2, 75, 0.6));
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
    void overcrowded_LifeCellstDoNotDieToOvercrowdedWithMaxParams_noDeathsOccur() {
        c.constructSimulation(new SimulationProperties(7, 100, 1, 20, 8, 75, 0.6));
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
        //because it should be greater than the threshold to die, they should not die if 4 are surrounding them with the threshold set to 4
        c.constructSimulation(new SimulationProperties(7, 100, 1, 20, 4, 75, 0.6));
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
}
