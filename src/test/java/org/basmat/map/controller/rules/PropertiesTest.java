package org.basmat.map.controller.rules;

import org.basmat.map.controller.Controller;
import org.basmat.map.model.ModelStructure;
import org.basmat.map.model.cells.SocietyCell;
import org.basmat.map.model.cells.factory.CellFactory;
import org.basmat.map.setup.ModelSetup;
import org.basmat.map.util.ECellType;
import org.basmat.map.util.SimulationProperties;
import org.basmat.map.util.TestUtilities;
import org.basmat.map.util.path.Node;
import org.basmat.map.view.SimulationInteractionUI;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PropertiesTest {

    private final ModelStructure modelStructure;
    private final CellFactory cellFactory;
    private final LinkedList<Point> globalSocietyCellList;
    private final ArrayList<Point> globalLifeCellList;
    private final ArrayList<LinkedList<Node>> listOfPaths;
    private final SimulationProperties simulationProperties;
    private final SimulationProperties simulationProperties1 = new SimulationProperties(7, 100, 1, 20, 5, 75, 0.6);
    private final Gardener gardener;
    private final Winnower winnower;

    PropertiesTest() throws InterruptedException {
        cellFactory = new CellFactory();
        modelStructure = new ModelStructure();
        simulationProperties = new SimulationProperties(7, 100, 1, 20, 5, 75, 0.6);
        SimulationInteractionUI userInteractionUi = new SimulationInteractionUI(null);
        globalSocietyCellList = new LinkedList<>();
        globalLifeCellList = new ArrayList<>();
        listOfPaths = new ArrayList<>();
        Controller c = new Controller(150,150);
        c.constructSimulation(simulationProperties);
        ModelSetup modelSetup = new ModelSetup(c, modelStructure, globalSocietyCellList, globalLifeCellList);
        modelSetup.setupMap();
        gardener = new Gardener(c, modelStructure, globalSocietyCellList, globalLifeCellList, listOfPaths);
        winnower = new Winnower(c, modelStructure, globalSocietyCellList, globalLifeCellList, listOfPaths);
    }


    @Test
    void simulationSetup_setupCorrectAmountOfSocieties_isEqualToSimProperties() {
        TestUtilities.overwriteModelWithWorldCell(modelStructure, ECellType.SOCIETY_CELL, ECellType.GRASS);
        int x = 0;
        for (int i = 0; i < 150; i++) {
            for (int j =0; j < 150; j++) {
                if (modelStructure.getCoordinate(new Point(i, j)) instanceof SocietyCell) {
                    x++;
                }
;            }
        }
        assertEquals(simulationProperties.societyCount(), x);
    }

    //@Test
    //void simulationSetup_nutrientCount
}